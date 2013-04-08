package org.adligo.i.pool.ldap.models;

public class LargeFileChunkAttributes extends CommonAttributes {
	public static final String LFC = "lfc";
	public static final I_LdapAttributeName SEQUENCED_NUMBER = new LdapAttributeName("nbr", "sequencednumber");
	public static final I_LdapAttributeName BINARY = new LdapAttributeName("bn", "binary");
	public static final I_LdapAttributeName SIZE = new LdapAttributeName("size");
	
	private LargeFileChunkAttributes() {}
}
