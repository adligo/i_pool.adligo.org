package org.adligo.i.pool.ldap.models;

public class LargeFileAttributes extends TopAttributes {
	public static final String OBJECT_CLASS_NAME = "lf";

	public static final I_LdapAttribute CHECKED_ON_SERVER = AdligoOrgAttributes.CHECKED_ON_SERVER;
	public static final I_LdapAttribute DELETING = AdligoOrgAttributes.DELETING;

	public static final I_LdapAttribute FILE_NAME = AdligoOrgAttributes.FILE_NAME;

	public static final I_LdapAttribute SIZE = AdligoOrgAttributes.SIZE;

	public static final I_LdapAttribute WRITING = AdligoOrgAttributes.WRITING;
	
	private LargeFileAttributes() {}
}
