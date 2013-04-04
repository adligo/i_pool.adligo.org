package org.adligo.i.pool.ldap;

public class LdapConnectionFactoryConfig {
	private String protocol = "ldap";
	private String host = "127.0.0.1";
	private int port = 389;
	private String initalContextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
	private String userDn;
	private String userPassword;
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getInitalContextFactory() {
		return initalContextFactory;
	}
	public void setInitalContextFactory(String initalContextFactory) {
		this.initalContextFactory = initalContextFactory;
	}
	
	public String getUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(protocol);
		sb.append("://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		return sb.toString();
	}
	public String getUserDn() {
		return userDn;
	}
	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPass) {
		this.userPassword = userPass;
	}
}
