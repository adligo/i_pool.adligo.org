package org.adligo.i.pool.ldap.models.converters;

import org.adligo.i.pool.ldap.models.LdapConnectionFactoryConfig;


/**
 * Implementations of this class are intended to be threadsafe!
 * 
 * note the attribute size limits are not set
 * by the schemas (like they are in rdbms')
 * so if you need BigInteger, Long, Integer
 * you will need to specify them seperatly
 * 
 * From the Java LDAP faq the JNDI ldap api only currently 
 * supports String and byte []
 * so they are the two types for L (Ldap aka Java JNDI api)
 * J is for Java so anything in Java here.
 * 
 * Also if you need a byte [] and it's coming out of LDAP (through the JNDI api)
 * as a String @see {@link LdapConnectionFactoryConfig#setBinaryAttributeNames(String)}
 * 
 * @see {@link IntegerAttributeConverter} for a example
 * @see {@line LdapConnectionFactoryConfig#addConvertedAttribute(Class, String)}
 * @see {@line LdapConnectionFactoryConfig#setAttributeConverter(Class, I_AttributeConverter)}
 * @author scott
 *
 */
public interface I_AttributeConverter<J extends Object,L extends Object> {

	public L toLdap(J p);
	public J toJava(L p);
	public Class<J> getJavaClass();
}
