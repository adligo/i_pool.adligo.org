package org.adligo.i.pool.ldap.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface I_LdapAttributes {

	public abstract Object getAttribute(I_LdapAttributeName key);
	public abstract Boolean getBooleanAttribute(I_LdapAttributeName key);
	public abstract Integer getIntegerAttribute(I_LdapAttributeName key);
	public abstract Short getShortAttribute(I_LdapAttributeName key);
	public abstract Float getFloatAttribute(I_LdapAttributeName key);
	public abstract Double getDoubleAttribute(I_LdapAttributeName key);
	public abstract Long getLongAttribute(I_LdapAttributeName key);
	public abstract BigInteger getBigIntegerAttribute(I_LdapAttributeName key);
	public abstract BigDecimal getBigDecimalAttribute(I_LdapAttributeName key);
	public abstract Date getDateAttribute(I_LdapAttributeName key);
	
	
	public abstract List<Object> getAttributes(I_LdapAttributeName key);
	public abstract List<Integer> getIntegerAttributes(I_LdapAttributeName key);
	public abstract List<Short> getShortAttributes(I_LdapAttributeName key);
	public abstract List<Float> getFloatAttributes(I_LdapAttributeName key);
	public abstract List<Double> getDoubleAttributes(I_LdapAttributeName key);
	public abstract List<Long> getLongAttributes(I_LdapAttributeName key);
	public abstract List<BigInteger> getBigIntegerAttributes(I_LdapAttributeName key);
	public abstract List<BigDecimal> getBigDecimalAttributes(I_LdapAttributeName key);
	public abstract List<Date> getDateAttributes(I_LdapAttributeName key);
	
	public abstract List<I_LdapAttributeName> getAttributeNames();

	public boolean hasAttribute(I_LdapAttributeName key);
	public boolean hasAttribute(I_LdapAttributeName key, Object value);
	public List<String> getStringAttributes(I_LdapAttributeName key);
}