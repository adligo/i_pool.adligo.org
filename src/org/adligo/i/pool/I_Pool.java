package org.adligo.i.pool;

public interface I_Pool<T extends PooledConnection> {

	/**
	 * obtain a connection from the pool
	 * @return
	 */
	public abstract T getConnection();

	/**
	 * mostly for junits a method to make sure
	 * the connection was returned.
	 * 
	 * @param con
	 * @return
	 */
	public boolean isConnectionInPool(T con);
	
	public abstract I_PoolStats getStats();

	public abstract String getName();

	public abstract void shutdown();

}