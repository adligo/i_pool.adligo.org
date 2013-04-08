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
	private Map<I_LdapAttribute, Object> attribs = new HashMap<I_LdapAttribute, Object>();
	
	public LdapAttributesMutant() {}
	
	public LdapAttributesMutant(LdapAttributesMutant p) {
		attribs.putAll(p.attribs);
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributesMutant#setAttribute(org.adligo.i.pool.ldap.models.I_LdapAttributeName, java.lang.Object)
	 */
	@Override
	public void setAttribute(I_LdapAttribute key, Object value) {
		if (value instanceof ArrayList) {
			throw new IllegalArgumentException(SET_ATTRIBUTE_ERROR);
		}
		attribs.put(key, value);
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributesMutant#setAttributes(org.adligo.i.pool.ldap.models.I_LdapAttributeName, java.util.List)
	 */
	@Override
	public void setAttributes(I_LdapAttribute name, List<? extends Object> value) {
		attribs.put(name, new ArrayList<Object>(value));
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.ldap.I_LdapAttributes#getAttribute(org.adligo.i.pool.ldap.models.I_LdapAttributeName)
	 */
	@Override
	public Object getAttribute(I_LdapAttribute key) {
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
	public List<Object> getAttributes(I_LdapAttribute key){
		List<Object> toRet = new ArrayList<Object>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<Object>();
		} if (val instanceof ArrayList) {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				toRet.add(obj);
			}
		} else {
			toRet.add(val);
		}
		return toRet;
	}
	
	@SuppressWarnings("unchecked")
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<Entry<I_LdapAttribute, Object>> entries = attribs.entrySet();
		for(Entry<I_LdapAttribute, Object> e: entries) {
			I_LdapAttribute keys = e.getKey();
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
	public List<I_LdapAttribute> getAttributeNames() {
		return new ArrayList<I_LdapAttribute>(attribs.keySet());
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasAttribute(I_LdapAttribute key, Object value) {
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
	public Boolean getBooleanAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Boolean) toRet;
	}

	@Override
	public Integer getIntegerAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Integer) toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getIntegerAttributes(I_LdapAttribute key) {
		List<Integer> toRet = new ArrayList<Integer>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<Integer>();
		} if (val instanceof Integer) {
			toRet.add((Integer) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((Integer) obj);
			}
		}
		return toRet;
	}

	@Override
	public Short getShortAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Short) toRet;
	}

	@Override
	public Float getFloatAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Float) toRet;
	}

	@Override
	public Double getDoubleAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Double) toRet;
	}

	@Override
	public BigInteger getBigIntegerAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (BigInteger) toRet;
	}

	@Override
	public BigDecimal getBigDecimalAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (BigDecimal) toRet;
	}

	
	@Override
	public Date getDateAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Date) toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Short> getShortAttributes(I_LdapAttribute key) {
		List<Short> toRet = new ArrayList<Short>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<Short>();
		} if (val instanceof Short) {
			toRet.add((Short) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((Short) obj);
			}
		}
		return toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Float> getFloatAttributes(I_LdapAttribute key) {
		List<Float> toRet = new ArrayList<Float>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<Float>();
		} if (val instanceof Float) {
			toRet.add((Float) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((Float) obj);
			}
		}
		return toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Double> getDoubleAttributes(I_LdapAttribute key) {
		List<Double> toRet = new ArrayList<Double>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<Double>();
		} if (val instanceof Double) {
			toRet.add((Double) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((Double) obj);
			}
		}
		return toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigInteger> getBigIntegerAttributes(I_LdapAttribute key) {
		List<BigInteger> toRet = new ArrayList<BigInteger>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<BigInteger>();
		} if (val instanceof BigInteger) {
			toRet.add((BigInteger) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((BigInteger) obj);
			}
		}
		return toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigDecimal> getBigDecimalAttributes(I_LdapAttribute key) {
		List<BigDecimal> toRet = new ArrayList<BigDecimal>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<BigDecimal>();
		} if (val instanceof BigDecimal) {
			toRet.add((BigDecimal) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((BigDecimal) obj);
			}
		}
		return toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Date> getDateAttributes(I_LdapAttribute key) {
		List<Date> toRet = new ArrayList<Date>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<Date>();
		} if (val instanceof Date) {
			toRet.add((Date) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((Date) obj);
			}
		}
		return toRet;
	}

	@Override
	public Long getLongAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet == null) {
			return null;
		}
		return (Long) toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getLongAttributes(I_LdapAttribute key) {
		List<Long> toRet = new ArrayList<Long>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<Long>();
		} if (val instanceof Long) {
			toRet.add((Long) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((Long) obj);
			}
		}
		return toRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getStringAttributes(I_LdapAttribute key) {
		List<String> toRet = new ArrayList<String>();
		Object val = attribs.get(key);
		if (val == null) {
			return new ArrayList<String>();
		} if (val instanceof String) {
			toRet.add((String) val);
		} else {
			List<Object> toRetList = (List<Object>) val;
			for (Object obj: toRetList) {
				//add one at a time to make sure we cast correctly
				toRet.add((String) obj);
			}
		}
		return toRet;
	}
	
	@Override
	public boolean hasAttribute(I_LdapAttribute key) {
		return attribs.containsKey(key);
	}
	
	public String getStringAttribute(I_LdapAttribute key) {
		Object toRet = attribs.get(key);
		if (toRet instanceof ArrayList) {
			throw new IllegalArgumentException(THERE_ARE_MULTIPLE_ATTRIBUTES_FOR_KEY + key + 
					TRY_GET_ATTRIBUTES_STRING_KEY);
		}
		return (String) toRet;
	}
}
