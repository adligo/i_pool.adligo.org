package org.adligo.i.pool.ldap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.i.pool.ldap.models.I_LdapEntry;
import org.adligo.i.pool.ldap.models.LdapEntryMutant;


public class ReadWriteLdapConnection extends LdapConnection {
	private static final Log log = LogFactory.getLog(ReadWriteLdapConnection.class);
	private static List<String> dEFAULT_IGNORE_ATTRIBUTE_LIST = getDefaultIgnoreAttributeList();
	
	private static List<String> getDefaultIgnoreAttributeList() {
		List<String> ignore = new ArrayList<String>();
		ignore.add("userPassword");
		ignore.add("objectClass");
		return Collections.unmodifiableList(ignore);
	}
	
	public ReadWriteLdapConnection(Hashtable<?, ?> env) {
		super(env);
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
					BasicAttribute ba = new BasicAttribute(key, val);
					attribs.put(ba);
				} else {
					for (Object obj: objs) {
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
		return update(e, dEFAULT_IGNORE_ATTRIBUTE_LIST);
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
	 * writes the chunked file out as ldap entries
	 * and closes the stream
	 * 
	 * @param fileName
	 * @param baseDn
	 * @param size
	 * @param contentStream
	 * @return
	 * @throws IOException
	 */
	public boolean writeChunkedFile(String fileName, String baseDn, long size, InputStream contentStream) throws IOException {
		markActive();
		
		LdapEntryMutant mut = new LdapEntryMutant();
	     mut.setAttribute("fileName", fileName);
	     mut.setDistinguishedName("fileName=" + fileName + "," + baseDn);
	      mut.setAttribute("size", "" + size);
	     mut.setAttribute("objectClass", "chunkedFile");
	     
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
			lem.setDistinguishedName("chunkNumber=" + whichChunk + "," + fileDn);
			lem.setAttribute("objectClass", "fileChunk");
			lem.setAttribute("chunkNumber", "" + whichChunk);
			lem.setAttribute("size", "" +  chunkSize);
			lem.setAttribute("binaryPart", bytes);
			if (!create(lem)) {
				return false;
			}
			size = size - chunkSize;
			whichChunk++;
		}
		byte [] bytes = new byte[(int) size];
		contentStream.read(bytes);
		
		LdapEntryMutant lem = new LdapEntryMutant();
		lem.setDistinguishedName("chunkNumber=" + whichChunk + "," + fileDn);
		lem.setAttribute("objectClass", "fileChunk");
		lem.setAttribute("chunkNumber", "" + whichChunk);
		lem.setAttribute("size", "" + size);
		lem.setAttribute("binaryPart", bytes);
		if (!create(lem)) {
			return false;
		}
		contentStream.close();
		return true;
	}
}
