package org.adligo.i.pool.ldap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ServiceUnavailableException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.i.pool.PooledConnection;
import org.adligo.i.pool.ldap.models.I_LdapEntry;
import org.adligo.i.pool.ldap.models.LargeFileAttributes;
import org.adligo.i.pool.ldap.models.LargeFileChunkAttributes;
import org.adligo.i.pool.ldap.models.LdapAttributContext;
import org.adligo.i.pool.ldap.models.LdapEntry;
import org.adligo.i.pool.ldap.models.LdapEntryMutant;

public abstract class LdapConnection extends PooledConnection {
	private static final String PLEASE_CHECK_IT_FIRST = " please check it first.";
	public static final String HAS_NOT_BEEN_CHECKED_ON_THE_SERVER = " has not been checked on the server ";
	public static final String IS_CURRENTLY_BEING_DELETED_SO_IT_CAN_NOT_BE_READ = " is currently being deleted so it can NOT be read.";
	public static final String THE_FILE = "The file ";
	private static final Log log = LogFactory.getLog(ReadOnlyLdapConnection.class);
	
	public static final String CHUNK_SIZE_KEY =  "org.adligo.i_pool.chunk_size";
	InitialDirContext ctx;
	Hashtable<?, ?> initalEnv;
	private volatile boolean ok = true;
	private int chunkSize;
	LdapAttributContext attributeCtx;
	
	public LdapConnection(Hashtable<?,?> env,LdapAttributContext context) {
		initalEnv = (Hashtable) env.clone();
		chunkSize = (Integer) env.get(CHUNK_SIZE_KEY);
		attributeCtx = context;
		reconnect();
		
	}


	void reconnect() {
		InitialDirContext prevCtx = null;
		if (ctx != null) {
			prevCtx = ctx;
		}
		try {
	       ctx = new InitialDirContext(initalEnv);
	       ok = true;
	    } catch (NamingException x) {
	       log.error(x.getMessage(), x);
	       ok = false;
	    }
		if (prevCtx != null) {
			try {
				prevCtx.close();
			} catch (NamingException x) {
				log.error(x.getMessage(), x);
			}
		}
	}


	@Override
	public boolean isOK() {
		return ok;
	}
	
	public void dispose() {
		try {
			if (ctx != null) {
				ctx.close();
			}
		} catch (NamingException ne) {
			log.error(ne.getMessage(), ne);
		}
	}
	
	/**
	 * search the ldap server for something
	 * @param name
	 * @param filter
	 * @param sc
	 * @return the relative names
	 */
	public List<String> search(String name, String filter, SearchControls sc) {
		markActive();
		List<String> toRet = new ArrayList<String>();
		try {
			NamingEnumeration<SearchResult> results = ctx.search(name, filter, sc);
			while (results.hasMoreElements()) {
				SearchResult entry  = results.next();
				String dn = entry.getName();
				toRet.add(dn);
			}
			results.close();
		} catch (NamingException ne) {
			log.error(ne.getMessage(), ne);
			if (isLdapServerDownException(ne)) {
				reconnect();
				if (isOK()) {
					return search(name, filter, sc);
				}
			}
		}
		return toRet;
	}
	
	/**
	 * returns the ldap entry with all of it's attributes
	 * @param name
	 * @return
	 */
	public I_LdapEntry get(String name) {
		markActive();
		try {
			Attributes attribs =  ctx.getAttributes(name);
			LdapEntryMutant lem = new LdapEntryMutant(name, attribs, attributeCtx);
			return new LdapEntry(lem);
		} catch (NamingException ne) {
			log.error(ne.getMessage(), ne);
			if (isLdapServerDownException(ne)) {
				reconnect();
				if (isOK()) {
					return get(name);
				}
			}
		}
		return null;
	}
	
	public boolean isLdapServerDownException(NamingException x) {
		if (x instanceof CommunicationException) {
			return true;
		}
		if (x instanceof ServiceUnavailableException) {
			return true;
		}
		return false;
	}
	/**
	 * @param dn
	 * @param password
	 * @return the entry that corresponds to the user
	 *   or null
	 *   
	 *   This actually creates a second ldap connection just to authenticate.
	 */
	@SuppressWarnings("unchecked")
	public I_LdapEntry authenticate(String dn, String password) {
		markActive();
		@SuppressWarnings("rawtypes")
		Hashtable authHash = (Hashtable) initalEnv.clone();
		authHash.put(Context.SECURITY_AUTHENTICATION, "simple");
		authHash.put(Context.SECURITY_PRINCIPAL, dn);
		authHash.put(Context.SECURITY_CREDENTIALS, password);
		try {
			InitialDirContext authCtx = new InitialDirContext(authHash);
			Attributes attribs =  authCtx.getAttributes(dn);
			authCtx.close();
			LdapEntryMutant lem = new LdapEntryMutant(dn, attribs, attributeCtx);
			return new LdapEntry(lem);
		} catch (NamingException x) {
			if (log.isDebugEnabled()) {
				log.debug(x.getMessage(), x);
			}
			if (isLdapServerDownException(x)) {
				reconnect();
				if (isOK()) {
					return authenticate(dn, password);
				}
			}
		}
		return null;
		
	}


	int getChunkSize() {
		return chunkSize;
	}


	void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	
	/**
	 * reads the large file data out to the output stream
	 * note this is in the ReadWriteLdapConnection
	 * because it writes the last time a read of file started
	 * this does not close the OutputStream
	 * 
	 * @param name
	 * @param out
	 * @throws IOException this indicates a problem writing the output through the OutputStream
	 * 
	 */
	public void getLargeFile(String name, String server, OutputStream out) throws IOException {
		markActive();
		I_LdapEntry largeFile = get(name);
		if (largeFile == null) {
			throw new IllegalStateException("no file found for " + name);
		}
		Boolean deleting = largeFile.getBooleanAttribute(LargeFileAttributes.DELETING);
		if (deleting) {
			throw new IllegalStateException(THE_FILE + name + 
					IS_CURRENTLY_BEING_DELETED_SO_IT_CAN_NOT_BE_READ);
		}
		List<String> checkedOnServers =  largeFile.getStringAttributes(LargeFileAttributes.CHECKED_ON_SERVER);
		if (!checkedOnServers.contains(server)) {
			throw new IllegalStateException(THE_FILE + name + 
					HAS_NOT_BEEN_CHECKED_ON_THE_SERVER + server + PLEASE_CHECK_IT_FIRST);
		}
		I_LdapEntry chunk = null;
		 SearchControls controls =
		            new SearchControls();
		         controls.setSearchScope(
		            SearchControls.SUBTREE_SCOPE);
		
		List<String> dns = search("", "(objectClass=" + LargeFileChunkAttributes.OBJECT_CLASS_NAME + ")", controls);
		
		for (int chunkNumber = 1; chunkNumber <= dns.size(); chunkNumber++) {
			chunk = get(LargeFileChunkAttributes.SEQUENCED_NUMBER.getName() +  "=" + chunkNumber + "," + name);
			if (chunk != null) {
				byte [] bytes = (byte []) chunk.getAttribute(LargeFileChunkAttributes.BINARY);
				out.write(bytes);
				out.flush();
			}
		} 
	}
}
