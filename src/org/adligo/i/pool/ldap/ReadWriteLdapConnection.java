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
import org.adligo.i.pool.ldap.models.I_LdapAttribute;
import org.adligo.i.pool.ldap.models.I_LdapEntry;
import org.adligo.i.pool.ldap.models.JavaToLdapConverters;
import org.adligo.i.pool.ldap.models.LargeFileAttributes;
import org.adligo.i.pool.ldap.models.LargeFileChunkAttributes;
import org.adligo.i.pool.ldap.models.LargeFileCreationToken;
import org.adligo.i.pool.ldap.models.LdapAttributContext;
import org.adligo.i.pool.ldap.models.LdapAttributeLookup;
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
	
	public ReadWriteLdapConnection(Hashtable<?, ?> env, LdapAttributContext attributeCtx) {
		super(env, attributeCtx);
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
			List<I_LdapAttribute> keys = entry.getAttributeNames();
			for (I_LdapAttribute name: keys) {
				List<String> aliases = name.getAliases();
				String key = aliases.get(0);
				List<Object> objs = entry.getAttributes(name);
				if (objs.size() == 1) {
					Object val = objs.get(0);
					val = attributeCtx.toLdap(key, val);
					BasicAttribute ba = new BasicAttribute(key, val);
					attribs.put(ba);
				} else {
					for (Object obj: objs) {
						obj = attributeCtx.toLdap(key, obj);
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
		
		List<I_LdapAttribute> newAttribs = e.getAttributeNames();
		for (I_LdapAttribute atribName: newAttribs) {
			if (!ignoreAttributes.contains(atribName)) {
				List<Object> atribVals = e.getAttributes(atribName);
				for (Object val: atribVals) {
					if (!onDisk.hasAttribute(atribName, val)) {
						String sName = atribName.getName();
						val = attributeCtx.toLdap(sName, val);
						mods.add( new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute(sName, val)));
					}
				}
			}
		}
		
		List<I_LdapAttribute> oldAttribs = onDisk.getAttributeNames();
		for (I_LdapAttribute atribName: oldAttribs) {
			if (!ignoreAttributes.contains(atribName)) {
				List<Object> atribVals = onDisk.getAttributes(atribName);
				for (Object val: atribVals) {
					if (!e.hasAttribute(atribName, val)) {
						String sName = atribName.getName();
						val = attributeCtx.toLdap(sName, val);
						mods.add( new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(sName, val)));
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
	public boolean replaceAttribute(String dn, I_LdapAttribute attributeName, Object val) {
		markActive();
		ModificationItem[] mods = new ModificationItem[1];
		 
		String name = attributeName.getName();
		val = attributeCtx.toLdap(name, val);
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(name, val));
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
	
	public boolean addAttribute(String dn, I_LdapAttribute attributeName, Object val) {
		markActive();
		ModificationItem[] mods = new ModificationItem[1];
		 
		String name = attributeName.getName();
		val = attributeCtx.toLdap(name, val);
		mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute(name, val));
		try {
			ctx.modifyAttributes(dn, mods);
			return true;
		} catch (NamingException x) {
			log.error(x.getMessage(), x);
			if (isLdapServerDownException(x)) {
				reconnect();
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
	public boolean createLargeFile(LargeFileCreationToken token) throws IOException {
		
		markActive();
		
		String fileName = token.getFileName();
		String baseDn = token.getBaseDn();
		long size = token.getSize();
		InputStream contentStream = token.getContentStream();
		String serverCreatedOn = token.getServerCheckedOn();
		
		LdapEntryMutant mut = new LdapEntryMutant();
	    
	     mut.setDistinguishedName(LargeFileAttributes.FILE_NAME.getName() + "=" + fileName + "," + baseDn);
	     mut.setAttribute(LargeFileAttributes.FILE_NAME, fileName);
	     mut.setAttribute(LargeFileAttributes.SIZE, size);
	     mut.setAttribute(LargeFileAttributes.WRITING, true);
	     mut.setAttribute(LargeFileAttributes.OBJECT_CLASS, LargeFileAttributes.OBJECT_CLASS_NAME);
	     
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
			lem.setDistinguishedName(LargeFileChunkAttributes.SEQUENCED_NUMBER.getName() + "=" + whichChunk + "," + fileDn);
			lem.setAttribute(LargeFileChunkAttributes.OBJECT_CLASS, LargeFileChunkAttributes.OBJECT_CLASS_NAME);
			lem.setAttribute(LargeFileChunkAttributes.SEQUENCED_NUMBER, whichChunk);
			lem.setAttribute(LargeFileChunkAttributes.SIZE, (long) chunkSize);
			lem.setAttribute(LargeFileChunkAttributes.BINARY, bytes);
			if (!create(lem)) {
				return false;
			}
			size = size - chunkSize;
			whichChunk++;
		}
		byte [] bytes = new byte[(int) size];
		contentStream.read(bytes);
		
		LdapEntryMutant lem = new LdapEntryMutant();
		lem.setDistinguishedName(LargeFileChunkAttributes.SEQUENCED_NUMBER.getName() + "=" + whichChunk + "," + fileDn);
		lem.setAttribute(LargeFileChunkAttributes.OBJECT_CLASS, LargeFileChunkAttributes.OBJECT_CLASS_NAME);
		lem.setAttribute(LargeFileChunkAttributes.SEQUENCED_NUMBER, whichChunk);
		lem.setAttribute(LargeFileChunkAttributes.SIZE, size);
		lem.setAttribute(LargeFileChunkAttributes.BINARY, bytes);
		if (!create(lem)) {
			return false;
		}
		replaceAttribute(fileDn, LargeFileAttributes.WRITING, false);
		addAttribute(fileDn, LargeFileAttributes.CHECKED_ON_SERVER, serverCreatedOn);
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
	public boolean deleteLargeFile(String name) throws IOException {
		markActive();
		I_LdapEntry largeFile = get(name);
		if (largeFile == null) {
			//someone else deleted it?
			return true;
		}
		replaceAttribute(name, LargeFileAttributes.DELETING, true);
		 SearchControls controls =
		            new SearchControls();
		         controls.setSearchScope(
		            SearchControls.SUBTREE_SCOPE);
		List<String> dns = search("", "(objectClass=" + LargeFileChunkAttributes.OBJECT_CLASS_NAME + ")", controls);
		
		for (int chunkNumber = 1; chunkNumber <= dns.size(); chunkNumber++) {
			if (!delete(LargeFileChunkAttributes.SEQUENCED_NUMBER +  "=" + chunkNumber + "," + name)) {
				return false;
			}
		} 
		if (delete(name)) {
			return true;
		}
		return false;
	}
}
