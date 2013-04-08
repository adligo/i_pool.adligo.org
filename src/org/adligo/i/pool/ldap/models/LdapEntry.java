package org.adligo.i.pool.ldap.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LdapEntry implements I_LdapEntry {
	LdapEntryMutant mutant;
	
	public LdapEntry() {
		mutant = new LdapEntryMutant();
	}
	
	public LdapEntry(LdapEntryMutant e) {
		mutant = new LdapEntryMutant(e);
	}

	public String getDistinguishedName() {
		return mutant.getDistinguishedName();
	}

	public Object getAttribute(I_LdapAttribute key) {
		return mutant.getAttribute(key);
	}

	public List<Object> getAttributes(I_LdapAttribute key) {
		return Collections.unmodifiableList(mutant.getAttributes(key));
	}

	public boolean hasAttribute(I_LdapAttribute key, Object value) {
		return mutant.hasAttribute(key, value);
	}
	
	public List<I_LdapAttribute> getAttributeNames() {
		return mutant.getAttributeNames();
	}

	public String toString() {
		return mutant.toString();
	}

	public Boolean getBooleanAttribute(I_LdapAttribute key) {
		return mutant.getBooleanAttribute(key);
	}

	public Integer getIntegerAttribute(I_LdapAttribute key) {
		return mutant.getIntegerAttribute(key);
	}

	public List<Integer> getIntegerAttributes(I_LdapAttribute key) {
		return mutant.getIntegerAttributes(key);
	}

	public Short getShortAttribute(I_LdapAttribute key) {
		return mutant.getShortAttribute(key);
	}

	public Float getFloatAttribute(I_LdapAttribute key) {
		return mutant.getFloatAttribute(key);
	}

	public Double getDoubleAttribute(I_LdapAttribute key) {
		return mutant.getDoubleAttribute(key);
	}

	public BigInteger getBigIntegerAttribute(I_LdapAttribute key) {
		return mutant.getBigIntegerAttribute(key);
	}

	public BigDecimal getBigDecimalAttribute(I_LdapAttribute key) {
		return mutant.getBigDecimalAttribute(key);
	}

	public Date getDateAttribute(I_LdapAttribute key) {
		return mutant.getDateAttribute(key);
	}

	public List<Short> getShortAttributes(I_LdapAttribute key) {
		return mutant.getShortAttributes(key);
	}

	public List<Float> getFloatAttributes(I_LdapAttribute key) {
		return mutant.getFloatAttributes(key);
	}

	public List<Double> getDoubleAttributes(I_LdapAttribute key) {
		return mutant.getDoubleAttributes(key);
	}

	public List<BigInteger> getBigIntegerAttributes(I_LdapAttribute key) {
		return mutant.getBigIntegerAttributes(key);
	}

	public List<BigDecimal> getBigDecimalAttributes(I_LdapAttribute key) {
		return mutant.getBigDecimalAttributes(key);
	}

	public List<Date> getDateAttributes(I_LdapAttribute key) {
		return mutant.getDateAttributes(key);
	}

	public Long getLongAttribute(I_LdapAttribute key) {
		return mutant.getLongAttribute(key);
	}

	public List<Long> getLongAttributes(I_LdapAttribute key) {
		return mutant.getLongAttributes(key);
	}

	public boolean hasAttribute(I_LdapAttribute key) {
		return mutant.hasAttribute(key);
	}

	public List<String> getStringAttributes(I_LdapAttribute key) {
		return mutant.getStringAttributes(key);
	}

	public String getStringAttribute(I_LdapAttribute key) {
		return mutant.getStringAttribute(key);
	}
}
