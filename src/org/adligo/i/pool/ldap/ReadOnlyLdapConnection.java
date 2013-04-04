package org.adligo.i.pool.ldap;

import java.util.Hashtable;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.i.pool.ldap.models.JavaToLdapConverters;


public final class ReadOnlyLdapConnection extends LdapConnection {
	private static final Log log = LogFactory.getLog(ReadOnlyLdapConnection.class);
	
	public ReadOnlyLdapConnection(Hashtable<?,?> env, JavaToLdapConverters ac) {
		super(env, ac);
	}

	@Override
	public boolean isReadWrite() {
		return false;
	}
}
