package org.adligo.i.pool;

public interface I_PoolStats {
	/**
	 * the maximum number of connections 
	 * this pool will create
	 * @return
	 */
	public int getMaxConnections();
	/**
	 * the total number of active and inactive connections in this pool
	 * @return
	 */
	public int getTotalConnections();
	/**
	 * the number of connections being used right now from the pool
	 * @return
	 */
	public int getActiveConnections();
	/**
	 * the number of connections available in the pool
	 * @return
	 */
	public int getInactiveConnections();
	
	/**
	 * the number of connections that were disposed
	 * @return
	 */
	public int getDisposedConnections();
	
	/**
	 * the number of connections that were created
	 * note this may be greater than max connections
	 * if some connections were disposed
	 * @return
	 */
	public int getCreatedConnections();
}
