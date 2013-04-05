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

	public Object getAttribute(String key) {
		return mutant.getAttribute(key);
	}

	public List<Object> getAttributes(String key) {
		return Collections.unmodifiableList(mutant.getAttributes(key));
	}

	public boolean hasAttribute(String key, Object value) {
		return mutant.hasAttribute(key, value);
	}
	
	public List<String> getAttributeNames() {
		return mutant.getAttributeNames();
	}

	public String toString() {
		return mutant.toString();
	}

	public Boolean getBooleanAttribute(String key) {
		return mutant.getBooleanAttribute(key);
	}

	public Integer getIntegerAttribute(String key) {
		return mutant.getIntegerAttribute(key);
	}

	public List<Integer> getIntegerAttributes(String key) {
		return mutant.getIntegerAttributes(key);
	}

	public Short getShortAttribute(String key) {
		return mutant.getShortAttribute(key);
	}

	public Float getFloatAttribute(String key) {
		return mutant.getFloatAttribute(key);
	}

	public Double getDoubleAttribute(String key) {
		return mutant.getDoubleAttribute(key);
	}

	public BigInteger getBigIntegerAttribute(String key) {
		return mutant.getBigIntegerAttribute(key);
	}

	public BigDecimal getBigDecimalAttribute(String key) {
		return mutant.getBigDecimalAttribute(key);
	}

	public Date getDateAttribute(String key) {
		return mutant.getDateAttribute(key);
	}

	public List<Short> getShortAttributes(String key) {
		return mutant.getShortAttributes(key);
	}

	public List<Float> getFloatAttributes(String key) {
		return mutant.getFloatAttributes(key);
	}

	public List<Double> getDoubleAttributes(String key) {
		return mutant.getDoubleAttributes(key);
	}

	public List<BigInteger> getBigIntegerAttributes(String key) {
		return mutant.getBigIntegerAttributes(key);
	}

	public List<BigDecimal> getBigDecimalAttributes(String key) {
		return mutant.getBigDecimalAttributes(key);
	}

	public List<Date> getDateAttributes(String key) {
		return mutant.getDateAttributes(key);
	}

	public Long getLongAttribute(String key) {
		return mutant.getLongAttribute(key);
	}

	public List<Long> getLongAttributes(String key) {
		return mutant.getLongAttributes(key);
	}

	public boolean hasAttribute(String key) {
		return mutant.hasAttribute(key);
	}
}
