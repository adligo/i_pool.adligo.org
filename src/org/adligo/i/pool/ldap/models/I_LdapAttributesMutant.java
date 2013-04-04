package org.adligo.i.pool.ldap.models;

import java.util.List;

public interface I_LdapAttributesMutant extends I_LdapAttributes {

	/**
	 * replaces anything that was there before
	 * @param key
	 * @param value
	 */
	public abstract void setAttribute(String key, Object value);

	public abstract void setAttributes(String key, List<Object> value);
}