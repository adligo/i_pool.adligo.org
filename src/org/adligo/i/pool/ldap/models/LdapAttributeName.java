package org.adligo.i.pool.ldap.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LdapAttributeName implements I_LdapAttributeName {
	public static final String LDAP_ATTRIBUTE_NAME_REQUIRES_NON_NULL_NAME_VALUES = "LdapAttributeName requires non null name values";
	private List<String> aliases = new ArrayList<String>();
	
	public LdapAttributeName(String ...strings) {
		if (strings == null) {
			throw new IllegalArgumentException(LDAP_ATTRIBUTE_NAME_REQUIRES_NON_NULL_NAME_VALUES);
		}
		for (String s: strings) {
			if (s == null) {
				throw new IllegalArgumentException(LDAP_ATTRIBUTE_NAME_REQUIRES_NON_NULL_NAME_VALUES);
			}
			aliases.add(s);
		}
		aliases = Collections.unmodifiableList(aliases);
	}

	/* (non-Javadoc)
	 * @see org.adligo.i.pool.ldap.models.I_LdapAttributeName#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aliases == null) ? 0 : aliases.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LdapAttributeName other = (LdapAttributeName) obj;
		if (aliases == null) {
			if (other.aliases != null)
				return false;
		} else {
			List<String> otherAliases = other.getAliases();
			for (String oa: otherAliases) {
				if (aliases.contains(oa)) {
					return true;
				}
			}
		}
		return true;
	}

	@Override
	public String getName() {
		return aliases.get(0);
	}

	@Override
	public String toString() {
		return "LdapAttributeName [aliases=" + aliases + "]";
	}
	
	
}
