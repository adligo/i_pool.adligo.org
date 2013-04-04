package org.adligo.i.pool.ldap.models;

import java.util.List;

public interface I_LdapAttributes {

	public abstract Object getAttribute(String key);

	public abstract List<Object> getAttributes(String key);

	public abstract List<String> getAttributeNames();

	public boolean hasAttribute(String key, Object value);
}