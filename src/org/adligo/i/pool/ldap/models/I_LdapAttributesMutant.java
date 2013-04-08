package org.adligo.i.pool.ldap.models;

import java.util.List;

public interface I_LdapAttributesMutant extends I_LdapAttributes {

	/**
	 * replaces anything that was there before
	 * @param name
	 * @param value
	 */
	public abstract void setAttribute(I_LdapAttribute name, Object value);

	/**
	 * replaces everything that was there before
	 * @param name
	 * @param value
	 */
	public abstract void setAttributes(I_LdapAttribute name, List<? extends Object> value);
}