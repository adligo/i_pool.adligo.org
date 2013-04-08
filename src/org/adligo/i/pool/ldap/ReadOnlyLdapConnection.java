package org.adligo.i.pool.ldap;

import java.util.Hashtable;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.i.pool.ldap.models.JavaToLdapConverters;
import org.adligo.i.pool.ldap.models.LdapAttributContext;
import org.adligo.i.pool.ldap.models.LdapAttributeLookup;


public final class ReadOnlyLdapConnection extends LdapConnection {
	private static final Log log = LogFactory.getLog(ReadOnlyLdapConnection.class);
	
	public ReadOnlyLdapConnection(Hashtable<?,?> env, LdapAttributContext ctx) {
		super(env, ctx);
	}

	@Override
	public boolean isReadWrite() {
		return false;
	}
}
