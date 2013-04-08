package org.adligo.i.pool.ldap.models;

public class LdapAttributeLookup {
	private LdapAttributeLookupMutant mutant;
	
	public LdapAttributeLookup() {
		mutant = new LdapAttributeLookupMutant();
	}
	
	public LdapAttributeLookup(LdapAttributeLookupMutant m) {
		mutant = new LdapAttributeLookupMutant(m);
	}

	public I_LdapAttribute get(String name) {
		return mutant.get(name);
	}
}
