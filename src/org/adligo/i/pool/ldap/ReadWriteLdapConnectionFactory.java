package org.adligo.i.pool.ldap;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;

import org.adligo.i.pool.I_PooledConnectionFactory;

public class ReadWriteLdapConnectionFactory implements I_PooledConnectionFactory<ReadWriteLdapConnection> {
	private Hashtable env;
	
	@SuppressWarnings("unchecked")
	public ReadWriteLdapConnectionFactory(LdapConnectionFactoryConfig config) {
		  env = new Hashtable();
	      env.put(Context.INITIAL_CONTEXT_FACTORY,
	         config.getInitalContextFactory());
	      env.put(Context.PROVIDER_URL,
	         config.getUrl());
	      env.put(Context.SECURITY_AUTHENTICATION, "simple");
		  env.put(Context.SECURITY_PRINCIPAL, config.getUserDn());
		  env.put(Context.SECURITY_CREDENTIALS, config.getUserPassword());
	}

	@Override
	public ReadWriteLdapConnection create() throws IOException {
		return new ReadWriteLdapConnection(env);
	}
}
