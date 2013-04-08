package org.adligo.i.pool.ldap.models;

/**
 * This class intends to hold only core attributes that are common to OpenLDAP, OpenDs and ApacheDs
 * so that this api could be used for most ldap servers.
 * 
 * @author scott
 *
 */
public class CoreAttributes {
	public static final I_LdapAttribute ASSOCIATED_NAME = new LdapAttribute("associatedName");
	public static final I_LdapAttribute BUSINESS_CATEGORY = new LdapAttribute("businessCategory");
	public static final I_LdapAttribute DESCRIPTION = new LdapAttribute("description");
	public static final I_LdapAttribute DESTINATION_INDICATOR = new LdapAttribute("destinationIndicator");
	
	public static final I_LdapAttribute DISTINGUISHED_NAME = new LdapAttribute("dn", "distinguishedName");

	public static final I_LdapAttribute DOMAIN_COMPONENT = new LdapAttribute("dc", "domainComponent");
	public static final I_LdapAttribute FACSIMILE_TELEPHONE_NUMBER = new LdapAttribute("facsimileTelephoneNumber");
	public static final I_LdapAttribute INTERNATIONAL_ISDN_NUMBER = new LdapAttribute("internationaliSDNNumber");
	
	public static final I_LdapAttribute LOCALITY_NAME = new LdapAttribute("l","localityName");
	public static final I_LdapAttribute NAME = new LdapAttribute("name");
	public static final I_LdapAttribute OBJECT_CLASS = new LdapAttribute("objectClass");
	public static final I_LdapAttribute ORGANIZATION_NAME = new LdapAttribute("o","organizationName");
	
	public static final I_LdapAttribute PHYSICAL_DELIVERY_OFFICE_NAME = new LdapAttribute("physicalDeliveryOfficeName");
	public static final I_LdapAttribute PREFERRED_DELIVERY_METHOD = new LdapAttribute("preferredDeliveryMethod");
	public static final I_LdapAttribute POST_OFFICE_BOX = new LdapAttribute("postOfficeBox");
	public static final I_LdapAttribute POSTAL_ADDRESS = new LdapAttribute("postalAddress");
	public static final I_LdapAttribute POSTAL_CODE = new LdapAttribute("postalCode");
	
	public static final I_LdapAttribute REGISTERED_ADDRESS = new LdapAttribute("registeredAddress");
	public static final I_LdapAttribute SEARCH_GUIDE = new LdapAttribute("searchGuide");
	public static final I_LdapAttribute SEE_ALSO = new LdapAttribute("seeAlso");
	public static final I_LdapAttribute STATE_OR_PROVINCE_NAME = new LdapAttribute("st","stateOrProvinceName");
	public static final I_LdapAttribute STREET = new LdapAttribute("street","streetAddress");
	
	public static final I_LdapAttribute TELEX_NUMBER = new LdapAttribute("telexNumber");
	public static final I_LdapAttribute TELEX_TERMINAL_IDENTIFIER = new LdapAttribute("teletexTerminalIdentifier");
	public static final I_LdapAttribute TELEPHONE_NUMBER = new LdapAttribute("telephoneNumber");
	
	public static final I_LdapAttribute USER_PASSWORD = new LdapAttribute("userPassword");
	public static final I_LdapAttribute X121ADDRESS = new LdapAttribute("x121Address");
	
	
	private CoreAttributes() {}
}
