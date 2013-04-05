package org.adligo.i.pool.ldap.models.converters;

import org.adligo.i.util.client.StringUtils;

public class DoubleAttributeConverter implements I_AttributeConverter<Double, String>{

	@Override
	public String toLdap(Double p) {
		if (p == null) {
			return "";
		}
		return "" + p;
	}

	@Override
	public Double toJava(String p) {
		if (StringUtils.isEmpty(p)) {
			return null;
		}
		return Double.parseDouble(p);
	}

	@Override
	public Class<Double> getJavaClass() {
		return Double.class;
	}

}
