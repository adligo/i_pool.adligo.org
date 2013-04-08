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

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;

public class LdapEntryMutant implements I_LdapEntry {
	private static final Log log = LogFactory.getLog(LdapEntryMutant.class);
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

	public LdapEntryMutant(String dn, Attributes attribs, LdapAttributContext ctx) throws NamingException {
		distinguishedName = dn;
		attributeMutant = new LdapAttributesMutant();
		NamingEnumeration<? extends Attribute> aa =  attribs.getAll();
		while (aa.hasMoreElements()) {
			Attribute attrib = aa.next();
			String key = attrib.getID();
			I_LdapAttribute attributeName = ctx.get(key);
			if (attributeName == null) {
				log.warn("Unable to find a I_AttributeName in the config for name " + key + 
						" creating one dynamicly which can cause problems with update, and getting values by I_AttributeName.");
				attributeName = new LdapAttribute(key);
			}
			
			NamingEnumeration<?> objs = attrib.getAll();
			List<Object> toAdd = new ArrayList<Object>();
			while (objs.hasMoreElements()) {
				Object obj = objs.next();
				obj = ctx.toJava(key, obj);
				toAdd.add(obj);
			}
			if (toAdd.size() == 1) {
				Object value = toAdd.get(0);
				attributeMutant.setAttribute(attributeName, value);
			} else if (toAdd.size() >= 2) {
				attributeMutant.setAttributes(attributeName, toAdd);
			}
		}
	}
	public String getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public void setAttribute(I_LdapAttribute key, Object value) {
		attributeMutant.setAttribute(key, value);
	}

	public void setAttributes(I_LdapAttribute key, List<Object> value) {
		attributeMutant.setAttributes(key, value);
	}

	public Object getAttribute(I_LdapAttribute key) {
		return attributeMutant.getAttribute(key);
	}

	public List<Object> getAttributes(I_LdapAttribute key) {
		return attributeMutant.getAttributes(key);
	}

	public List<I_LdapAttribute> getAttributeNames() {
		return attributeMutant.getAttributeNames();
	}
	
	
	public boolean hasAttribute(I_LdapAttribute key, Object value) {
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

	public Boolean getBooleanAttribute(I_LdapAttribute key) {
		return attributeMutant.getBooleanAttribute(key);
	}

	public Integer getIntegerAttribute(I_LdapAttribute key) {
		return attributeMutant.getIntegerAttribute(key);
	}

	public List<Integer> getIntegerAttributes(I_LdapAttribute key) {
		return attributeMutant.getIntegerAttributes(key);
	}

	public Short getShortAttribute(I_LdapAttribute key) {
		return attributeMutant.getShortAttribute(key);
	}

	public Float getFloatAttribute(I_LdapAttribute key) {
		return attributeMutant.getFloatAttribute(key);
	}

	public Double getDoubleAttribute(I_LdapAttribute key) {
		return attributeMutant.getDoubleAttribute(key);
	}

	public BigInteger getBigIntegerAttribute(I_LdapAttribute key) {
		return attributeMutant.getBigIntegerAttribute(key);
	}

	public BigDecimal getBigDecimalAttribute(I_LdapAttribute key) {
		return attributeMutant.getBigDecimalAttribute(key);
	}

	public Date getDateAttribute(I_LdapAttribute key) {
		return attributeMutant.getDateAttribute(key);
	}

	public List<Short> getShortAttributes(I_LdapAttribute key) {
		return attributeMutant.getShortAttributes(key);
	}

	public List<Float> getFloatAttributes(I_LdapAttribute key) {
		return attributeMutant.getFloatAttributes(key);
	}

	public List<Double> getDoubleAttributes(I_LdapAttribute key) {
		return attributeMutant.getDoubleAttributes(key);
	}

	public List<BigInteger> getBigIntegerAttributes(I_LdapAttribute key) {
		return attributeMutant.getBigIntegerAttributes(key);
	}

	public List<BigDecimal> getBigDecimalAttributes(I_LdapAttribute key) {
		return attributeMutant.getBigDecimalAttributes(key);
	}

	public List<Date> getDateAttributes(I_LdapAttribute key) {
		return attributeMutant.getDateAttributes(key);
	}

	public Long getLongAttribute(I_LdapAttribute key) {
		return attributeMutant.getLongAttribute(key);
	}

	public List<Long> getLongAttributes(I_LdapAttribute key) {
		return attributeMutant.getLongAttributes(key);
	}

	public boolean hasAttribute(I_LdapAttribute key) {
		return attributeMutant.hasAttribute(key);
	}

	public List<String> getStringAttributes(I_LdapAttribute key) {
		return attributeMutant.getStringAttributes(key);
	}

	public String getStringAttribute(I_LdapAttribute key) {
		return attributeMutant.getStringAttribute(key);
	}
	
}
