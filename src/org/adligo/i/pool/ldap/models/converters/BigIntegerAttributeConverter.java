package org.adligo.i.pool.ldap.models.converters;

import java.math.BigInteger;

import org.adligo.i.util.client.StringUtils;

public class BigIntegerAttributeConverter implements I_AttributeConverter<BigInteger, String>{

	@Override
	public String toLdap(BigInteger p) {
		if (p == null) {
			return "";
		}
		return p.toString();
	}

	@Override
	public BigInteger toJava(String p) {
		if (StringUtils.isEmpty(p)) {
			return null;
		}
		return new BigInteger(p);
	}

	@Override
	public Class<BigInteger> getJavaClass() {
		return BigInteger.class;
	}

}
