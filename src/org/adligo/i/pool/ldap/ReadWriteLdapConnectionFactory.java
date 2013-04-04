package org.adligo.i.pool.ldap;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;

import org.adligo.i.pool.I_PooledConnectionFactory;
import org.adligo.i.pool.ldap.models.JavaToLdapConverters;
import org.adligo.i.pool.ldap.models.LdapConnectionFactoryConfig;

public class ReadWriteLdapConnectionFactory implements I_PooledConnectionFactory<ReadWriteLdapConnection> {
	private Hashtable env;
	private JavaToLdapConverters converters;
	
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
		  String binAttributeNames = config.getBinaryAttributeNames();
		  if (binAttributeNames != null) {
			  env.put("java.naming.ldap.attributes.binary", binAttributeNames);
		  }
		  env.put(LdapConnection.CHUNK_SIZE_KEY, config.getDefaultChunkSize());
		  converters = new JavaToLdapConverters(config);
	}

	@Override
	public ReadWriteLdapConnection create() throws IOException {
		return new ReadWriteLdapConnection(env, converters);
	}
}
