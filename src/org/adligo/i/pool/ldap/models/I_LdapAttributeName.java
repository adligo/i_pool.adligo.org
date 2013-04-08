package org.adligo.i.pool.ldap.models;

import java.util.List;

public interface I_LdapAttributeName {
	/**
	 * returns the first name/primary name (usually the shortest)
	 * @return
	 */
	public String getName();
	public abstract List<String> getAliases();

}