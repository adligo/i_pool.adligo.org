package org.adligo.i.pool.ldap.models;

public class LargeFileAttributes extends CommonAttributes {
	public static final String LF = "lf";
	public static final I_LdapAttributeName FILE_NAME = new LdapAttributeName("fn", "fileName");
	public static final I_LdapAttributeName WRITING = new LdapAttributeName("wt", "writing");
	public static final I_LdapAttributeName DELETING = new LdapAttributeName("del", "deleting");
	public static final I_LdapAttributeName READING = new LdapAttributeName("rd", "reading");
	public static final I_LdapAttributeName CHECKED_ON_SERVER = new LdapAttributeName("ck", "checkedOnServer");
	public static final I_LdapAttributeName SIZE = new LdapAttributeName("size");
	
	private LargeFileAttributes() {}
}
