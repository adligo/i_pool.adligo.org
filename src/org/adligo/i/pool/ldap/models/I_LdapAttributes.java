package org.adligo.i.pool.ldap.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface I_LdapAttributes {

	public abstract Object getAttribute(String key);
	public abstract Boolean getBooleanAttribute(String key);
	public abstract Integer getIntegerAttribute(String key);
	public abstract Short getShortAttribute(String key);
	public abstract Float getFloatAttribute(String key);
	public abstract Double getDoubleAttribute(String key);
	public abstract Long getLongAttribute(String key);
	public abstract BigInteger getBigIntegerAttribute(String key);
	public abstract BigDecimal getBigDecimalAttribute(String key);
	public abstract Date getDateAttribute(String key);
	
	
	public abstract List<Object> getAttributes(String key);
	public abstract List<Integer> getIntegerAttributes(String key);
	public abstract List<Short> getShortAttributes(String key);
	public abstract List<Float> getFloatAttributes(String key);
	public abstract List<Double> getDoubleAttributes(String key);
	public abstract List<Long> getLongAttributes(String key);
	public abstract List<BigInteger> getBigIntegerAttributes(String key);
	public abstract List<BigDecimal> getBigDecimalAttributes(String key);
	public abstract List<Date> getDateAttributes(String key);
	
	public abstract List<String> getAttributeNames();

	public boolean hasAttribute(String key);
	public boolean hasAttribute(String key, Object value);
}