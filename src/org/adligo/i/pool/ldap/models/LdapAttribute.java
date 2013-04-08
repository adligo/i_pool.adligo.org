package org.adligo.i.pool.ldap.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This the instances of class are immutable;
 * 
 * Note due to implementation details
 * all names must be present in the list of aliases for most of the 
 * code in this project to work correctly, therefore
 * a 
 * @author scott
 *
 */
public class LdapAttribute implements I_LdapAttribute {
	public static final String LDAP_ATTRIBUTE_NAME_REQUIRES_NON_NULL_NAME_VALUES = "LdapAttributeName requires non null name values";
	private List<String> aliases = new ArrayList<String>();
	private Class<?> javaType = String.class;
	private int hashCode;
	
	public LdapAttribute(String ...strings) {
		setup(strings);
	}
	
	public LdapAttribute(Class<?> pJavaType, String ...strings) {
		javaType = pJavaType;
		setup(strings);
	}

	private void setup(String... strings) {
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
		hashCode = calcHashCode();
	}

	/* (non-Javadoc)
	 * @see org.adligo.i.pool.ldap.models.I_LdapAttributeName#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LdapAttribute other = (LdapAttribute) obj;
		if (aliases == null) {
			if (other.aliases != null)
				return false;
		} else {
			if (!other.getJavaType().equals(javaType)) {
				return false;
			}
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
	public int hashCode() {
		return hashCode;
	}
	
	public int calcHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + javaType.hashCode();
		for (String alias: aliases) {
			result = prime * result + alias.hashCode();
		}
		return result;
	}
	
	@Override
	public String getName() {
		return aliases.get(0);
	}

	@Override
	public String toString() {
		return "LdapAttributeName [aliases=" + aliases + "]";
	}

	public Class<?> getJavaType() {
		return javaType;
	}
	
	
}
