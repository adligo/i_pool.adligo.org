package org.adligo.i.pool.ldap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.adligo.i.adi.client.InvokerNames;
import org.adligo.i.adig.client.GRegistry;
import org.adligo.i.adig.client.I_GInvoker;
import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.i.pool.ldap.models.I_LdapEntry;
import org.adligo.i.pool.ldap.models.JavaToLdapConverters;
import org.adligo.i.pool.ldap.models.LargeFileAttributes;
import org.adligo.i.pool.ldap.models.LargeFileChunkAttributes;
import org.adligo.i.pool.ldap.models.LdapEntryMutant;


public class ReadWriteLdapConnection extends LdapConnection {
	public static final String IS_CURRENTLY_BEING_READ_SO_IT_CAN_T_BE_DELETED_RIGHT_NOW = " is currently being read, so it can't be deleted right now.";
	private static final Log log = LogFactory.getLog(ReadWriteLdapConnection.class);
	private static List<String> DEFAULT_IGNORE_ATTRIBUTE_LIST = getDefaultIgnoreAttributeList();
	private static I_GInvoker<Object, Long> CLOCK = GRegistry.getInvoker(InvokerNames.CLOCK, 
			Object.class, Long.class);
	
	private static List<String> getDefaultIgnoreAttributeList() {
		List<String> ignore = new ArrayList<String>();
		ignore.add("userPassword");
		ignore.add("objectClass");
		return Collections.unmodifiableList(ignore);
	}
	
	public ReadWriteLdapConnection(Hashtable<?, ?> env, JavaToLdapConverters ac) {
		super(env, ac);
	}

	
	@Override
	public boolean isReadWrite() {
		return true;
	}

	public boolean create(I_LdapEntry entry) {
		markActive();
		try {
			String dn = entry.getDistinguishedName();
			BasicAttributes attribs = new BasicAttributes();
			List<String> keys = entry.getAttributeNames();
			for (String key: keys) {
				List<Object> objs = entry.getAttributes(key);
				if (objs.size() == 1) {
					Object val = objs.get(0);
					val = attributeConverter.toLdap(key, val);
					BasicAttribute ba = new BasicAttribute(key, val);
					attribs.put(ba);
				} else {
					for (Object obj: objs) {
						obj = attributeConverter.toLdap(key, obj);
						BasicAttribute ba = new BasicAttribute(key, obj);
						attribs.put(ba);
					}
				}
			}
			DirContext dc = ctx.createSubcontext(dn, attribs);
			dc.close();
			return true;
		} catch (NamingException x) {
			log.error(x.getMessage(), x);
			if (isLdapServerDownException(x)) {
				reconnect();
				if (isOK()) {
					return create(entry);
				}
			}
		}
		return false;
	}

	public boolean delete(String dn) {
		markActive();
		try {
			ctx.destroySubcontext(dn);
			return true;
		} catch (NamingException x) {
			log.error(x.getMessage(), x);
			if (isLdapServerDownException(x)) {
				reconnect();
				if (isOK()) {
					return delete(dn);
				}
			}
		}
		return false;
	}
	
	public boolean update(I_LdapEntry e, List<String> ignoreAttributes) {
		markActive();
		String dn = e.getDistinguishedName();
		I_LdapEntry onDisk = super.get(dn);
		List<ModificationItem> mods = new ArrayList<ModificationItem>();
		
		List<String> newAttribs = e.getAttributeNames();
		for (String atribName: newAttribs) {
			if (!ignoreAttributes.contains(atribName)) {
				List<Object> atribVals = e.getAttributes(atribName);
				for (Object val: atribVals) {
					if (!onDisk.hasAttribute(atribName, val)) {
						val = attributeConverter.toLdap(atribName, val);
						mods.add( new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute(atribName, val)));
					}
				}
			}
		}
		
		List<String> oldAttribs = onDisk.getAttributeNames();
		for (String atribName: oldAttribs) {
			if (!ignoreAttributes.contains(atribName)) {
				List<Object> atribVals = onDisk.getAttributes(atribName);
				for (Object val: atribVals) {
					if (!e.hasAttribute(atribName, val)) {
						val = attributeConverter.toLdap(atribName, val);
						mods.add( new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(atribName, val)));
					}
				}
			}
		}
		try {
			ModificationItem [] modsArray = new ModificationItem[mods.size()];
			int i = 0;
			for (ModificationItem mi: mods) {
				modsArray[i++] = mi;
			}
			ctx.modifyAttributes(dn, modsArray);
			return true;
		}  catch (NamingException x) {
			log.error(x.getMessage(), x);
			if (isLdapServerDownException(x)) {
				reconnect();
				if (isOK()) {
					return update(e, ignoreAttributes);
				}
			}
		}
		return false;
	}
	
	public boolean update(I_LdapEntry e) {
		return update(e, DEFAULT_IGNORE_ATTRIBUTE_LIST);
	}
	/**
	 * only useful for single attributes like password
	 * @param dn
	 * @param attributeName
	 * @param val
	 * @return
	 */
	public boolean replaceAttribute(String dn, String attributeName, Object val) {
		markActive();
		ModificationItem[] mods = new ModificationItem[1];
		 
		val = attributeConverter.toLdap(attributeName, val);
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attributeName, val));
		try {
			ctx.modifyAttributes(dn, mods);
			return true;
		} catch (NamingException x) {
			log.error(x.getMessage(), x);
			if (isLdapServerDownException(x)) {
				reconnect();
				if (isOK()) {
					return replaceAttribute(dn, attributeName, val);
				}
			}
		}
		return false;
	}
	
	/**
	 * puts the large file in the ldap server as a group of entries;
	 * largeFile
	 *    largeFileChunk nbr 1
	 *    largeFileChunk nbr 2
	 *    exc;
	 *    
	 * This does NOT close the InputStream;
	 * 
	 * @param fileName
	 * @param baseDn
	 * @param size
	 * @param contentStream
	 * @return
	 * @throws IOException should be caught, logged
	 *    and the file deleted as it was not a complete write
	 *    and then try it again?
	 */
	public boolean createLargeFile(String fileName, String baseDn, long size, InputStream contentStream) throws IOException {
		markActive();
		
		LdapEntryMutant mut = new LdapEntryMutant();
	    
	     mut.setDistinguishedName(LargeFileAttributes.FN + "=" + fileName + "," + baseDn);
	     mut.setAttribute(LargeFileAttributes.FN, fileName);
	     mut.setAttribute(LargeFileAttributes.SIZE, size);
	     mut.setAttribute(LargeFileAttributes.WT, true);
	     mut.setAttribute(LargeFileAttributes.RD, 0L);
	     mut.setAttribute(LargeFileAttributes.DEL, false);
	     mut.setAttribute(LargeFileAttributes.OBJECT_CLASS, LargeFileAttributes.LF);
	     
		if (!create(mut)) {
			return false;
		}
		int whichChunk = 1;
		int chunkSize = getChunkSize();
		String fileDn = mut.getDistinguishedName();
		
		while (size - chunkSize > 0) {
			byte [] bytes = new byte[chunkSize];
			contentStream.read(bytes);
			
			LdapEntryMutant lem = new LdapEntryMutant();
			lem.setDistinguishedName(LargeFileChunkAttributes.NBR + "=" + whichChunk + "," + fileDn);
			lem.setAttribute(LargeFileChunkAttributes.OBJECT_CLASS, LargeFileChunkAttributes.LFC);
			lem.setAttribute(LargeFileChunkAttributes.NBR, whichChunk);
			lem.setAttribute(LargeFileChunkAttributes.SIZE, (long) chunkSize);
			lem.setAttribute(LargeFileChunkAttributes.BN, bytes);
			if (!create(lem)) {
				return false;
			}
			size = size - chunkSize;
			whichChunk++;
		}
		byte [] bytes = new byte[(int) size];
		contentStream.read(bytes);
		
		LdapEntryMutant lem = new LdapEntryMutant();
		lem.setDistinguishedName(LargeFileChunkAttributes.NBR + "=" + whichChunk + "," + fileDn);
		lem.setAttribute(LargeFileChunkAttributes.OBJECT_CLASS, LargeFileChunkAttributes.LFC);
		lem.setAttribute(LargeFileChunkAttributes.NBR, whichChunk);
		lem.setAttribute(LargeFileChunkAttributes.SIZE, size);
		lem.setAttribute(LargeFileChunkAttributes.BN, bytes);
		if (!create(lem)) {
			return false;
		}
		replaceAttribute(fileDn, LargeFileAttributes.WRITING, false);
		return true;
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
	public void getLargeFile(String name, OutputStream out) throws IOException {
		markActive();
		I_LdapEntry largeFile = get(name);
		if (largeFile == null) {
			throw new IllegalStateException("no file found for " + name);
		}
		Boolean deleting = largeFile.getBooleanAttribute(LargeFileAttributes.DEL);
		if (deleting == null) {
			 deleting = largeFile.getBooleanAttribute(LargeFileAttributes.DELETING);
		}
		if (deleting) {
			throw new IllegalStateException(THE_FILE + name + 
					IS_CURRENTLY_BEING_DELETED_SO_IT_CAN_NOT_BE_READ);
		}
		Long time = CLOCK.invoke(null);
		replaceAttribute(name, LargeFileAttributes.RD, time);
		I_LdapEntry chunk = null;
		 SearchControls controls =
		            new SearchControls();
		         controls.setSearchScope(
		            SearchControls.SUBTREE_SCOPE);
		
		List<String> dns = search("", "(objectClass=" + LargeFileChunkAttributes.LFC + ")", controls);
		
		for (int chunkNumber = 1; chunkNumber <= dns.size(); chunkNumber++) {
			chunk = get(LargeFileChunkAttributes.NBR +  "=" + chunkNumber + "," + name);
			if (chunk == null) {
				chunk = get(LargeFileChunkAttributes.SEQUENCED_NUMBER +  "=" + chunkNumber + "," + name);
			}
			if (chunk != null) {
				byte [] bytes = (byte []) chunk.getAttribute(LargeFileChunkAttributes.BN);
				if (bytes == null) {
					bytes = (byte []) chunk.getAttribute(LargeFileChunkAttributes.BINARY);
				}
				out.write(bytes);
				out.flush();
			}
		} 
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
	public boolean deleteLargeFile(String name) throws IOException {
		markActive();
		I_LdapEntry largeFile = get(name);
		if (largeFile == null) {
			//someone else deleted it?
			return true;
		}
		replaceAttribute(name, LargeFileAttributes.DEL, true);
		 SearchControls controls =
		            new SearchControls();
		         controls.setSearchScope(
		            SearchControls.SUBTREE_SCOPE);
		List<String> dns = search("", "(objectClass=" + LargeFileChunkAttributes.LFC + ")", controls);
		
		for (int chunkNumber = 1; chunkNumber <= dns.size(); chunkNumber++) {
			if (!delete(LargeFileChunkAttributes.NBR +  "=" + chunkNumber + "," + name)) {
				return false;
			}
		} 
		if (delete(name)) {
			return true;
		}
		return false;
	}
}
