package org.adligo.i.pool.ldap.models.converters;

import org.adligo.i.util.client.StringUtils;

public class LongAttributeConverter implements I_AttributeConverter<Long, String>{

	@Override
	public String toLdap(Long p) {
		if (p == null) {
			return "";
		}
		return "" + p;
	}

	@Override
	public Long toJava(String p) {
		if (StringUtils.isEmpty(p)) {
			return null;
		}
		return Long.parseLong(p);
	}

	@Override
	public Class<Long> getJavaClass() {
		return Long.class;
	}

}
