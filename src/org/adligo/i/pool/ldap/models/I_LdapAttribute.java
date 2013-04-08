package org.adligo.i.pool.ldap.models;

import java.util.List;

public interface I_LdapAttribute {
	/**
	 * returns the first name/primary name (usually the shortest)
	 * @return
	 */
	public String getName();
	public abstract List<String> getAliases();
	/**
	 * the java type that the attribute values will be
	 * @return
	 */
	public Class<?> getJavaType();
}