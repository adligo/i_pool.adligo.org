package org.adligo.i.pool.ldap.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.adligo.i.pool.ldap.models.converters.I_AttributeConverter;


/**
 * this class is thread safe
 * and shared by all threads reading/writing to ldap server
 * for a LDAP pool
 * 
 * @author scott
 *
 */
public class JavaToLdapConverters {
	/**
	 * note that only the thread that created this instance
	 * would modify this map, so multithreaded reads will be ok
	 */
	@SuppressWarnings("rawtypes")
	private Map<String,I_AttributeConverter> attributesToConverters = 
			new HashMap<String,I_AttributeConverter>();
	
	public JavaToLdapConverters(LdapConnectionFactoryConfig config) {
		Map<Class<?>, Set<String>> converterdAttributes = config.getConvertedAttributes();
		Map<Class<?>, I_AttributeConverter<?, ?>>  converters = config.getAttributeConverters();
		
		Set<Entry<Class<?>, Set<String>>> entries = converterdAttributes.entrySet();
		for (Entry<Class<?>, Set<String>> e: entries) {
			Class<?> clazz = e.getKey();
			Set<String> attributes = e.getValue();
			for (String attribute: attributes) {
			I_AttributeConverter<?, ?> converter = attributesToConverters.get(attribute);
				if (converter != null) {
					throw new IllegalArgumentException("The attribute " + attribute +
							" has been entered twice under the classes " + clazz + " and " +
							converter.getJavaClass());
				} else {
					if ((new byte [0]).getClass().equals(clazz) || String.class.equals(clazz)) {
						//do nothing
					} else {
						converter = converters.get(clazz);
						if (converter == null) {
							throw new IllegalArgumentException("The attribute " + attribute +
									" requires a converter to the java class " + clazz + 
									" however one can not be found in the config's AttributeConverters");
						} else {
							attributesToConverters.put(attribute, converter);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object toJava(String attributeName, Object value) {
		I_AttributeConverter converter =  attributesToConverters.get(attributeName);
		if (converter == null) {
			return value;
		}
		return converter.toJava(value);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object toLdap(String attributeName, Object value) {
		I_AttributeConverter converter =  attributesToConverters.get(attributeName);
		if (converter == null) {
			return value;
		}
		return converter.toLdap(value);
	}
}
