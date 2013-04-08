package org.adligo.i.pool.ldap.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
	private Map<I_LdapAttributeName, Object> attribs = new HashMap<I_LdapAttributeName, Object>();
	
	public LdapAttributesMutant() {}
	
	public LdapAttributesMutant(LdapAttributesMutant p) {
		attribs.putAll(p.attribs);
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributesMutant#setAttribute(org.adligo.i.pool.ldap.models.I_LdapAttributeName, java.lang.Object)
	 */
	@Override
	public void setAttribute(I_LdapAttributeName key, Object value) {
		if (value instanceof ArrayList) {
			throw new IllegalArgumentException(SET_ATTRIBUTE_ERROR);
		}
		attribs.put(key, value);
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributesMutant#setAttributes(org.adligo.i.pool.ldap.models.I_LdapAttributeName, java.util.List)
	 */
	@Override
	public void setAttributes(I_LdapAttributeName name, List<Object> value) {
		attribs.put(name, new ArrayList<Object>(value));
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributes#getAttribute(org.adligo.i.pool.ldap.models.I_LdapAttributeName)
	 */
	@Override
	public Object getAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet instanceof ArrayList) {
			throw new IllegalArgumentException(THERE_ARE_MULTIPLE_ATTRIBUTES_FOR_KEY + key + 
					TRY_GET_ATTRIBUTES_STRING_KEY);
		}
		return toRet;
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributes#getAttributes(org.adligo.i.pool.ldap.models.I_LdapAttributeName)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getAttributes(I_LdapAttributeName key){
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<Object>();
		}
		if ( !(toRet instanceof ArrayList)) {
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(toRet);
			toRet = list;
		}
		return (List<Object>) toRet;
	}
	
	@SuppressWarnings("unchecked")
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<Entry<I_LdapAttributeName, Object>> entries = attribs.entrySet();
		for(Entry<I_LdapAttributeName, Object> e: entries) {
			I_LdapAttributeName keys = e.getKey();
			List<String> aliases = keys.getAliases();
			String key = aliases.get(0);
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
	public List<I_LdapAttributeName> getAttributeNames() {
		return new ArrayList<I_LdapAttributeName>(attribs.keySet());
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasAttribute(I_LdapAttributeName key, Object value) {
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
	public Boolean getBooleanAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Boolean) toRet;
	}

	@Override
	public Integer getIntegerAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Integer) toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getIntegerAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<Integer>();
		}
		List<Integer> list = new ArrayList<Integer>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((Integer) obj);
		}
		return list;
	}

	@Override
	public Short getShortAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Short) toRet;
	}

	@Override
	public Float getFloatAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Float) toRet;
	}

	@Override
	public Double getDoubleAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Double) toRet;
	}

	@Override
	public BigInteger getBigIntegerAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (BigInteger) toRet;
	}

	@Override
	public BigDecimal getBigDecimalAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (BigDecimal) toRet;
	}

	
	@Override
	public Date getDateAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Date) toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Short> getShortAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<Short>();
		}
		List<Short> list = new ArrayList<Short>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((Short) obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Float> getFloatAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<Float>();
		}
		List<Float> list = new ArrayList<Float>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((Float) obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Double> getDoubleAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<Double>();
		}
		List<Double> list = new ArrayList<Double>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((Double) obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigInteger> getBigIntegerAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<BigInteger>();
		}
		List<BigInteger> list = new ArrayList<BigInteger>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((BigInteger) obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigDecimal> getBigDecimalAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<BigDecimal>();
		}
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((BigDecimal) obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Date> getDateAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<Date>();
		}
		List<Date> list = new ArrayList<Date>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((Date) obj);
		}
		return list;
	}

	@Override
	public Long getLongAttribute(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Long) toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getLongAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<Long>();
		}
		List<Long> list = new ArrayList<Long>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((Long) obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getStringAttributes(I_LdapAttributeName key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return new ArrayList<String>();
		}
		List<String> list = new ArrayList<String>();
		List<Object> toRetList = (List<Object>) toRet;
		for (Object obj: toRetList) {
			//add one at a time to make sure we cast correctly
			list.add((String) obj);
		}
		return list;
	}
	
	@Override
	public boolean hasAttribute(I_LdapAttributeName key) {
		return attribs.containsKey(key);
	}
}
