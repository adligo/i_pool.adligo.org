package org.adligo.i.pool.ldap.models;

public class LdapAttributContext {
	private JavaToLdapConverters converters;
	private LdapAttributeLookup lookup;
	
	public LdapAttributContext(JavaToLdapConverters con, LdapAttributeLookup look) {
		converters = con;
		lookup = look;
	}
	
	public JavaToLdapConverters getConverters() {
		return converters;
	}
	public LdapAttributeLookup getLookup() {
		return lookup;
	}

	public Object toJava(String attributeName, Object value) {
		return converters.toJava(attributeName, value);
	}

	public Object toLdap(String attributeName, Object value) {
		return converters.toLdap(attributeName, value);
	}

	public I_LdapAttribute get(String name) {
		return lookup.get(name);
	}
	
}
