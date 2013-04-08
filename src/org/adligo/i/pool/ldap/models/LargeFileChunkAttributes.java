package org.adligo.i.pool.ldap.models;

public class LargeFileChunkAttributes extends TopAttributes {
	public static final String OBJECT_CLASS_NAME = "lfc";
	
	public static final I_LdapAttribute BINARY = AdligoOrgAttributes.BINARY;
	public static final I_LdapAttribute SEQUENCED_NUMBER = AdligoOrgAttributes.SEQUENCED_NUMBER;
	public static final I_LdapAttribute SIZE = AdligoOrgAttributes.SIZE;
	
	
	private LargeFileChunkAttributes() {}
}
