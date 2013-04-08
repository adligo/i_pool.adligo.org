package org.adligo.i.pool.ldap.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LdapAttributeLookupMutant {
	private Map<String, I_LdapAttribute> attributeLookup = new HashMap<String, I_LdapAttribute>();
	
	public LdapAttributeLookupMutant() {}
	
	public LdapAttributeLookupMutant(LdapAttributeLookupMutant o) {
		attributeLookup.putAll(o.attributeLookup);
	}
	
	public void add(I_LdapAttribute p) {
		List<String> aliases = p.getAliases();
		for (String name: aliases) {
			attributeLookup.put(name, p);
		}
	}
	
	public I_LdapAttribute get(String name) {
		return attributeLookup.get(name);
	}
	
	public void remove(I_LdapAttribute p) {
		List<String> aliases = p.getAliases();
		for (String name: aliases) {
			attributeLookup.remove(name);
		}
	}
}
