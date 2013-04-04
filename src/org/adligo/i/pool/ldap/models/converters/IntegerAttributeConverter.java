package org.adligo.i.pool.ldap.models.converters;

import org.adligo.i.util.client.StringUtils;

public class IntegerAttributeConverter implements I_AttributeConverter<Integer, String>{

	@Override
	public String toLdap(Integer p) {
		if (p == null) {
			return "";
		}
		return "" + p;
	}

	@Override
	public Integer toJava(String p) {
		if (StringUtils.isEmpty(p)) {
			return null;
		}
		return Integer.parseInt(p);
	}

	@Override
	public Class<Integer> getJavaClass() {
		return Integer.class;
	}

}
