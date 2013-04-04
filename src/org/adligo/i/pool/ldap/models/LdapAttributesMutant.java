package org.adligo.i.pool.ldap.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LdapAttributesMutant implements I_LdapAttributes, I_LdapAttributesMutant {
	public static final String TRY_GET_ATTRIBUTES_STRING_KEY = " try getAttributes(String key)";
	public static final String THERE_ARE_MULTIPLE_ATTRIBUTES_FOR_KEY = "There are multiple attributes for key ";
	public static final String SET_ATTRIBUTE_ERROR = "The value of a attribute may not be a ArrayList," +
			"as that is used internally for multiple attributes.";
	private Map<String, Object> attribs = new HashMap<String, Object>();
	
	public LdapAttributesMutant() {}
	
	public LdapAttributesMutant(LdapAttributesMutant p) {
		attribs.putAll(p.attribs);
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributesMutant#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String key, Object value) {
		if (value instanceof ArrayList) {
			throw new IllegalArgumentException(SET_ATTRIBUTE_ERROR);
		}
		attribs.put(key, value);
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributesMutant#setAttributes(java.lang.String, java.util.List)
	 */
	@Override
	public void setAttributes(String key, List<Object> value) {
		attribs.put(key, new ArrayList<Object>(value));
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributes#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String key) {
		Object toRet = attribs.get(key);
		if (toRet instanceof ArrayList) {
			throw new IllegalArgumentException(THERE_ARE_MULTIPLE_ATTRIBUTES_FOR_KEY + key + 
					TRY_GET_ATTRIBUTES_STRING_KEY);
		}
		return toRet;
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributes#getAttributes(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getAttributes(String key){
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		if ( !(toRet instanceof ArrayList)) {
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(toRet);
			toRet = list;
		}
		return (List<Object>) toRet;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entries = attribs.entrySet();
		for(Entry<String, Object> e: entries) {
			String key = e.getKey();
			Object val = e.getValue();
			if (val instanceof ArrayList) {
				ArrayList<Object> vals = (ArrayList<Object>) val;
				for (Object v: vals) {
					sb.append(key);
					sb.append(": ");
					sb.append(v);
					sb.append("\n");
				}
			} else {
				sb.append(key);
				sb.append(": ");
				sb.append(val);
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	@Override
	public List<String> getAttributeNames() {
		return new ArrayList<String>(attribs.keySet());
	}
	
	public boolean hasAttribute(String key, Object value) {
		if (value == null) {
			return false;
		}
		Object val = attribs.get(key);
		if (val instanceof ArrayList) {
			List<Object> vals = (List<Object>) val;
			for (Object v: vals) {
				if (value.equals(v)) {
					return true;
				}
			}
		} else if (value.equals(val)) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean getBooleanAttribute(String key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Boolean) toRet;
	}

	@Override
	public Integer getIntegerAttribute(String key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Integer) toRet;
	}

	@Override
	public List<Integer> getIntegerAttributes(String key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((Integer) obj);
		}
		return list;
	}
}
