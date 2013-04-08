package org.adligo.i.pool.ldap.models;

public class AdligoOrgAttributes {

	public static final I_LdapAttribute BINARY = new LdapAttribute((new byte [0]).getClass(), "bn", "binary");
	public static final I_LdapAttribute CHECKED_ON_SERVER = new LdapAttribute("ck", "checkedOnServer");
	public static final I_LdapAttribute DELETING = new LdapAttribute(Long.class, "del", "deleting");
	
	public static final I_LdapAttribute FILE_NAME = new LdapAttribute("fn", "fileName");
	public static final I_LdapAttribute SIZE = new LdapAttribute(Long.class, "size");
	public static final I_LdapAttribute SEQUENCED_NUMBER = new LdapAttribute(Integer.class, "nbr", "sequencednumber");
	public static final I_LdapAttribute WRITING = new LdapAttribute(Boolean.class, "wt", "writing");
}
