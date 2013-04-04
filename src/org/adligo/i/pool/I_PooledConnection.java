package org.adligo.i.pool;

public interface I_PooledConnection {
	/**
	 * returns this connection to the pool for others to use
	 */
	public void returnToPool();
	/**
	 * true if can read and write
	 * false if can only read
	 * @return
	 */
	public boolean isReadWrite();
	/**
	 * implementations should return true 
	 * if the underlying connection is still usable
	 * and false if it is not
	 * @return
	 */
	public boolean isOK();
	
	/**
	 * dispose of this connection (as it was not OK)
	 * cleaning up any resources it may use
	 * @return
	 */
	public void dispose();
}
