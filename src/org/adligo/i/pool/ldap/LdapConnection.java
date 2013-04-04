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
import org.adligo.i.pool.ldap.models.JavaToLdapConverters;
import org.adligo.i.pool.ldap.models.LdapEntry;
import org.adligo.i.pool.ldap.models.LdapEntryMutant;

public abstract class LdapConnection extends PooledConnection {
	private static final Log log = LogFactory.getLog(ReadOnlyLdapConnection.class);
	public static final String CHUNK_SIZE_KEY =  "org.adligo.i_pool.chunk_size";
	InitialDirContext ctx;
	Hashtable<?, ?> initalEnv;
	private volatile boolean ok = true;
	private int chunkSize;
	JavaToLdapConverters attributeConverter;
	
	public LdapConnection(Hashtable<?,?> env, JavaToLdapConverters ac) {
		initalEnv = (Hashtable) env.clone();
		chunkSize = (Integer) env.get(CHUNK_SIZE_KEY);
		attributeConverter = ac;
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
			LdapEntryMutant lem = new LdapEntryMutant(name, attribs, attributeConverter);
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
			LdapEntryMutant lem = new LdapEntryMutant(dn, attribs, attributeConverter);
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
	 * reads the chunked data out to the output stream
	 * @param name
	 * @param out
	 * @throws IOException
	 */
	public void readChunkedFile(String name, OutputStream out) throws IOException {
		markActive();
		I_LdapEntry chunk = null;
		 SearchControls controls =
		            new SearchControls();
		         controls.setSearchScope(
		            SearchControls.SUBTREE_SCOPE);
		List<String> dns = search("", "(objectClass=fileChunk)", controls);
		
		for (int chunkNumber = 1; chunkNumber <= dns.size(); chunkNumber++) {
			chunk = get("chunkNumber=" + chunkNumber + "," + name);
			if (chunk != null) {
				byte [] bytes = (byte []) chunk.getAttribute("binaryPart");
				out.write(bytes);
				out.flush();
			}
		} 
		out.close();
	}
}
