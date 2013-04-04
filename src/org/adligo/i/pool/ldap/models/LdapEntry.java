package org.adligo.i.pool.ldap.models;

import java.util.Collections;
import java.util.List;

public class LdapEntry implements I_LdapEntry {
	LdapEntryMutant mutant;
	
	public LdapEntry() {
		mutant = new LdapEntryMutant();
	}
	
	public LdapEntry(LdapEntryMutant e) {
		mutant = new LdapEntryMutant(e);
	}

	public String getDistinguishedName() {
		return mutant.getDistinguishedName();
	}

	public Object getAttribute(String key) {
		return mutant.getAttribute(key);
	}

	public List<Object> getAttributes(String key) {
		return Collections.unmodifiableList(mutant.getAttributes(key));
	}

	public boolean hasAttribute(String key, Object value) {
		return mutant.hasAttribute(key, value);
	}
	
	public List<String> getAttributeNames() {
		return mutant.getAttributeNames();
	}

	public String toString() {
		return mutant.toString();
	}
}
