package org.adligo.i.pool.ldap;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;

import org.adligo.i.pool.I_PooledConnectionFactory;
import org.adligo.i.pool.ldap.models.JavaToLdapConverters;
import org.adligo.i.pool.ldap.models.LdapAttributContext;
import org.adligo.i.pool.ldap.models.LdapAttributeLookup;
import org.adligo.i.pool.ldap.models.LdapConnectionFactoryConfig;

public class ReadWriteLdapConnectionFactory implements I_PooledConnectionFactory<ReadWriteLdapConnection> {
	private Hashtable env;
	private LdapAttributContext attribCtx;
	
	@SuppressWarnings("unchecked")
	public ReadWriteLdapConnectionFactory(LdapConnectionFactoryConfig config) {
		  env = new Hashtable();
	      env.put(Context.INITIAL_CONTEXT_FACTORY,
	         config.getInitalContextFactory());
	      env.put(Context.PROVIDER_URL,
	         config.getUrl());
	      env.put(Context.SECURITY_AUTHENTICATION, "simple");
	      String userDn = config.getUserDn();
	      if (userDn != null) {
	    	  env.put(Context.SECURITY_PRINCIPAL, userDn);
	      }
	      String passwd = config.getUserPassword();
	      if (passwd != null) {
	    	  env.put(Context.SECURITY_CREDENTIALS, passwd);
	      }
		  String binAttributeNames = config.getBinaryAttributeNames();
		  if (binAttributeNames != null) {
			  env.put("java.naming.ldap.attributes.binary", binAttributeNames);
		  }
		  env.put(LdapConnection.CHUNK_SIZE_KEY, config.getDefaultChunkSize());
		  JavaToLdapConverters converters = new JavaToLdapConverters(config);
		  LdapAttributeLookup lookup = new LdapAttributeLookup(config.getAttributeLookup());
		  attribCtx = new LdapAttributContext(converters, lookup);
	}

	@Override
	public ReadWriteLdapConnection create() throws IOException {
		return new ReadWriteLdapConnection(env, attribCtx);
	}
}
