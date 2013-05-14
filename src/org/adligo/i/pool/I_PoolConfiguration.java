package org.adligo.i.pool;

public interface I_PoolConfiguration<T extends I_PooledConnection> {

	public abstract int getMin();

	public abstract int getMax();

	public abstract String getName();

	public abstract I_PooledConnectionFactory<T> getFactory();

}