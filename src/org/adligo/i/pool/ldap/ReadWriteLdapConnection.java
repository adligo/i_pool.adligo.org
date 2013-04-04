package org.adligo.i.pool.ldap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.i.pool.ldap.models.I_LdapEntry;


public class ReadWriteLdapConnection extends LdapConnection {
	private static final Log log = LogFactory.getLog(ReadWriteLdapConnection.class);
	private static List<String> dEFAULT_IGNORE_ATTRIBUTE_LIST = getDefaultIgnoreAttributeList();
	
	private static List<String> getDefaultIgnoreAttributeList() {
		List<String> ignore = new ArrayList<String>();
		ignore.add("password");
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
		} catch (CommunicationException g) {
			reconnect();
			if (isOK()) {
				return create(entry);
			}
		} catch (NamingException x) {
			log.error(x.getMessage(), x);
		}
		return false;
	}

	public boolean delete(String dn) {
		return false;
	}
	
	public boolean update(I_LdapEntry e, List<String> ignoreAttributes) {
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
		return false;
	}
}
