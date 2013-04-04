package org.adligo.i.pool.ldap;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;

import org.adligo.i.pool.I_PooledConnectionFactory;

public class ReadOnlyLdapConnectionFactory implements I_PooledConnectionFactory<ReadOnlyLdapConnection> {
	private Hashtable env;
	
	@SuppressWarnings("unchecked")
	public ReadOnlyLdapConnectionFactory(LdapConnectionFactoryConfig config) {
		  env = new Hashtable();
	      env.put(Context.INITIAL_CONTEXT_FACTORY,
	         config.getInitalContextFactory());
	      env.put(Context.PROVIDER_URL,
	         config.getUrl());
	      String binAttributeNames = config.getBinaryAttributeNames();
		  if (binAttributeNames != null) {
			  env.put("java.naming.ldap.attributes.binary", binAttributeNames);
		  }
		  env.put(LdapConnection.CHUNK_SIZE_KEY, config.getDefaultChunkSize());
	}

	@Override
	public ReadOnlyLdapConnection create() throws IOException {
		return new ReadOnlyLdapConnection(env);
	}
}
