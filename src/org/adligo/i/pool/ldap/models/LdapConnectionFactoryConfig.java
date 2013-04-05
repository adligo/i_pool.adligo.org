package org.adligo.i.pool.ldap.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.adligo.i.pool.ldap.models.converters.BigDecimalAttributeConverter;
import org.adligo.i.pool.ldap.models.converters.BigIntegerAttributeConverter;
import org.adligo.i.pool.ldap.models.converters.BooleanAttributeConverter;
import org.adligo.i.pool.ldap.models.converters.DateAttributeConverter;
import org.adligo.i.pool.ldap.models.converters.DoubleAttributeConverter;
import org.adligo.i.pool.ldap.models.converters.FloatAttributeConverter;
import org.adligo.i.pool.ldap.models.converters.I_AttributeConverter;
import org.adligo.i.pool.ldap.models.converters.IntegerAttributeConverter;
import org.adligo.i.pool.ldap.models.converters.LongAttributeConverter;
import org.adligo.i.pool.ldap.models.converters.ShortAttributeConverter;


/**
 * this class is not thread safe,
 * and the variables here are converted into the thread safe
 * 
 * @author scott
 *
 */
public class LdapConnectionFactoryConfig {
	private String protocol = "ldap";
	private String host = "127.0.0.1";
	private int port = 389;
	private String initalContextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
	private String userDn;
	private String userPassword;
	private String binaryAttributeNames = "binaryPart";
	private int defaultChunkSize = 65536;
	/**
	 * the key is a Java class, the value is a list of ldap attribute names
	 */
	private Map<Class<?>, Set<String>> convertedAttributes = new HashMap<Class<?>, Set<String>>();
	private Map<Class<?>, I_AttributeConverter<?, ?>> attributeConverters = new HashMap<Class<?>, I_AttributeConverter<?, ?>>();
	
	public LdapConnectionFactoryConfig() {
		attributeConverters.put(Integer.class, new IntegerAttributeConverter());
		attributeConverters.put(Boolean.class, new BooleanAttributeConverter());
		attributeConverters.put(Short.class, new ShortAttributeConverter());
		attributeConverters.put(Float.class, new FloatAttributeConverter());
		attributeConverters.put(Double.class, new DoubleAttributeConverter());
		attributeConverters.put(BigInteger.class, new BigIntegerAttributeConverter());
		attributeConverters.put(BigDecimal.class, new BigDecimalAttributeConverter());
		attributeConverters.put(Long.class, new LongAttributeConverter());
		attributeConverters.put(Date.class, new DateAttributeConverter());
	}
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getInitalContextFactory() {
		return initalContextFactory;
	}
	public void setInitalContextFactory(String initalContextFactory) {
		this.initalContextFactory = initalContextFactory;
	}
	
	public String getUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(protocol);
		sb.append("://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		return sb.toString();
	}
	public String getUserDn() {
		return userDn;
	}
	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPass) {
		this.userPassword = userPass;
	}
	public String getBinaryAttributeNames() {
		return binaryAttributeNames;
	}
	public void setBinaryAttributeNames(String binaryAttributeNames) {
		this.binaryAttributeNames = binaryAttributeNames;
	}
	public int getDefaultChunkSize() {
		return defaultChunkSize;
	}
	public void setDefaultChunkSize(int defaultChunkSize) {
		this.defaultChunkSize = defaultChunkSize;
	}
	
	public void addConvertedAttribute(Class<?> clazz, String attributeName) {
		Set<String> attribs = convertedAttributes.get(clazz);
		if (attribs == null) {
			attribs = new HashSet<String>();
			convertedAttributes.put(clazz, attribs);
		}
		attribs.add(attributeName);
	}
	
	public void removeConvertedAttribute(Class<?> clazz, String attributeName) {
		Set<String> attribs = convertedAttributes.get(clazz);
		if (attribs == null) {
			return;
		}
		attribs.remove(attributeName);
	}
	
	public Map<Class<?>, Set<String>> getConvertedAttributes() {
		return Collections.unmodifiableMap(convertedAttributes);
	}
	
	public void setAttributeConverter(Class<?> clazz, I_AttributeConverter<?, ?> converter) {
		attributeConverters.put(clazz, converter);
	}
	
	public I_AttributeConverter<?, ?> getAttributeConverter(Class<?> clazz) {
		return attributeConverters.get(clazz);
	}
	
	public Map<Class<?>, I_AttributeConverter<?, ?>> getAttributeConverters() {
		return Collections.unmodifiableMap(attributeConverters);
	}
}
