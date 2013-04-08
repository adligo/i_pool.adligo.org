package org.adligo.i.pool.ldap.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface I_LdapAttributes {

	public abstract Object getAttribute(I_LdapAttribute key);
	public abstract Boolean getBooleanAttribute(I_LdapAttribute key);
	public abstract Integer getIntegerAttribute(I_LdapAttribute key);
	public abstract Short getShortAttribute(I_LdapAttribute key);
	public abstract Float getFloatAttribute(I_LdapAttribute key);
	public abstract Double getDoubleAttribute(I_LdapAttribute key);
	public abstract Long getLongAttribute(I_LdapAttribute key);
	public abstract BigInteger getBigIntegerAttribute(I_LdapAttribute key);
	public abstract BigDecimal getBigDecimalAttribute(I_LdapAttribute key);
	public abstract Date getDateAttribute(I_LdapAttribute key);
	
	
	public abstract List<Object> getAttributes(I_LdapAttribute key);
	public abstract List<Integer> getIntegerAttributes(I_LdapAttribute key);
	public abstract List<Short> getShortAttributes(I_LdapAttribute key);
	public abstract List<Float> getFloatAttributes(I_LdapAttribute key);
	public abstract List<Double> getDoubleAttributes(I_LdapAttribute key);
	public abstract List<Long> getLongAttributes(I_LdapAttribute key);
	public abstract List<BigInteger> getBigIntegerAttributes(I_LdapAttribute key);
	public abstract List<BigDecimal> getBigDecimalAttributes(I_LdapAttribute key);
	public abstract List<Date> getDateAttributes(I_LdapAttribute key);
	
	public abstract List<I_LdapAttribute> getAttributeNames();

	public boolean hasAttribute(I_LdapAttribute key);
	public boolean hasAttribute(I_LdapAttribute key, Object value);

	public String getStringAttribute(I_LdapAttribute key);
	public List<String> getStringAttributes(I_LdapAttribute key);
}