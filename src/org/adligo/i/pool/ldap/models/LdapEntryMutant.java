package org.adligo.i.pool.ldap.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
	
	public LdapEntryMutant(I_LdapEntry e) {
		if (e instanceof LdapEntryMutant) {
			distinguishedName = e.getDistinguishedName();
			attributeMutant = new LdapAttributesMutant(((LdapEntryMutant) e).attributeMutant);
		} else {
			distinguishedName = e.getDistinguishedName();
			attributeMutant = new LdapAttributesMutant(((LdapEntry) e).mutant.attributeMutant);
		}
	}

	public LdapEntryMutant(String dn, Attributes attribs, JavaToLdapConverters converters) throws NamingException {
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
				obj = converters.toJava(key, obj);
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

	public Boolean getBooleanAttribute(String key) {
		return attributeMutant.getBooleanAttribute(key);
	}

	public Integer getIntegerAttribute(String key) {
		return attributeMutant.getIntegerAttribute(key);
	}

	public List<Integer> getIntegerAttributes(String key) {
		return attributeMutant.getIntegerAttributes(key);
	}

	public Short getShortAttribute(String key) {
		return attributeMutant.getShortAttribute(key);
	}

	public Float getFloatAttribute(String key) {
		return attributeMutant.getFloatAttribute(key);
	}

	public Double getDoubleAttribute(String key) {
		return attributeMutant.getDoubleAttribute(key);
	}

	public BigInteger getBigIntegerAttribute(String key) {
		return attributeMutant.getBigIntegerAttribute(key);
	}

	public BigDecimal getBigDecimalAttribute(String key) {
		return attributeMutant.getBigDecimalAttribute(key);
	}

	public Date getDateAttribute(String key) {
		return attributeMutant.getDateAttribute(key);
	}

	public List<Short> getShortAttributes(String key) {
		return attributeMutant.getShortAttributes(key);
	}

	public List<Float> getFloatAttributes(String key) {
		return attributeMutant.getFloatAttributes(key);
	}

	public List<Double> getDoubleAttributes(String key) {
		return attributeMutant.getDoubleAttributes(key);
	}

	public List<BigInteger> getBigIntegerAttributes(String key) {
		return attributeMutant.getBigIntegerAttributes(key);
	}

	public List<BigDecimal> getBigDecimalAttributes(String key) {
		return attributeMutant.getBigDecimalAttributes(key);
	}

	public List<Date> getDateAttributes(String key) {
		return attributeMutant.getDateAttributes(key);
	}

	public Long getLongAttribute(String key) {
		return attributeMutant.getLongAttribute(key);
	}

	public List<Long> getLongAttributes(String key) {
		return attributeMutant.getLongAttributes(key);
	}

	public boolean hasAttribute(String key) {
		return attributeMutant.hasAttribute(key);
	}
	
}
