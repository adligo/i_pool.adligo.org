package org.adligo.i.pool.ldap.models.converters;

import org.adligo.i.util.client.StringUtils;

public class ShortAttributeConverter implements I_AttributeConverter<Short, String>{

	@Override
	public String toLdap(Short p) {
		if (p == null) {
			return "";
		}
		return "" + p;
	}

	@Override
	public Short toJava(String p) {
		if (StringUtils.isEmpty(p)) {
			return null;
		}
		return Short.parseShort(p);
	}

	@Override
	public Class<Short> getJavaClass() {
		return Short.class;
	}

}
