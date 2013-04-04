package org.adligo.i.pool.ldap.models;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class LdapEntryMutant implements I_LdapEntry {
	private String distinguishedName;
	private LdapAttributesMutant attributeMutant;
	
	public LdapEntryMutant() {
		attributeMutant = new LdapAttributesMutant();
	}
	
	public LdapEntryMutant(LdapEntryMutant e) {
		distinguishedName = e.getDistinguishedName();
		attributeMutant = new LdapAttributesMutant(e.attributeMutant);
	}

	public LdapEntryMutant(String dn, Attributes attribs) throws NamingException {
		distinguishedName = dn;
		attributeMutant = new LdapAttributesMutant();
		NamingEnumeration<? extends Attribute> aa =  attribs.getAll();
		while (aa.hasMoreElements()) {
			Attribute attrib = aa.next();
			String key = attrib.getID();
			NamingEnumeration<?> objs = attrib.getAll();
			List<Object> toAdd = new ArrayList<Object>();
			while (objs.hasMoreElements()) {
				Object obj = objs.next();
				toAdd.add(obj);
			}
			if (toAdd.size() == 1) {
				Object value = toAdd.get(0);
				attributeMutant.setAttribute(key, value);
			} else if (toAdd.size() >= 2) {
				attributeMutant.setAttributes(key, toAdd);
			}
		}
	}
	public String getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public void setAttribute(String key, Object value) {
		attributeMutant.setAttribute(key, value);
	}

	public void setAttributes(String key, List<Object> value) {
		attributeMutant.setAttributes(key, value);
	}

	public Object getAttribute(String key) {
		return attributeMutant.getAttribute(key);
	}

	public List<Object> getAttributes(String key) {
		return attributeMutant.getAttributes(key);
	}

	public List<String> getAttributeNames() {
		return attributeMutant.getAttributeNames();
	}
	
	
	public boolean hasAttribute(String key, Object value) {
		return attributeMutant.hasAttribute(key, value);
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("dn: ");
		sb.append(distinguishedName);
		sb.append("\n");
		sb.append(attributeMutant.toString());
		return sb.toString();
	}
	
}
