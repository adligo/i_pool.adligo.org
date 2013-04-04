package org.adligo.i.pool.ldap;

import java.util.Hashtable;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;


public final class ReadOnlyLdapConnection extends LdapConnection {
	private static final Log log = LogFactory.getLog(ReadOnlyLdapConnection.class);
	
	public ReadOnlyLdapConnection(Hashtable<?,?> env) {
		super(env);
	}

	@Override
	public boolean isReadWrite() {
		return false;
	}
}
