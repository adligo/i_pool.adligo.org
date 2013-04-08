package org.adligo.i.pool.ldap;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;

import org.adligo.i.pool.I_PooledConnectionFactory;
import org.adligo.i.pool.ldap.models.JavaToLdapConverters;
import org.adligo.i.pool.ldap.models.LdapAttributContext;
import org.adligo.i.pool.ldap.models.LdapAttributeLookup;
import org.adligo.i.pool.ldap.models.LdapConnectionFactoryConfig;

public class ReadOnlyLdapConnectionFactory implements I_PooledConnectionFactory<ReadOnlyLdapConnection> {
	private Hashtable env;
	private LdapAttributContext attributCtx;
	
	
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
		  JavaToLdapConverters converters = new JavaToLdapConverters(config);
		  LdapAttributeLookup lookup = new LdapAttributeLookup(config.getAttributeLookup());
		  attributCtx = new LdapAttributContext(converters, lookup);
	}

	@Override
	public ReadOnlyLdapConnection create() throws IOException {
		return new ReadOnlyLdapConnection(env, attributCtx);
	}
}
