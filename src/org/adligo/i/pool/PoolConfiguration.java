package org.adligo.i.pool;

public class PoolConfiguration<T extends I_PooledConnection> {
	private int min = 0;
	private int max = 1;
	private String name;
	private I_PooledConnectionFactory<T> factory;
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public I_PooledConnectionFactory<T> getFactory() {
		return factory;
	}
	public void setFactory(I_PooledConnectionFactory<T> factory) {
		this.factory = factory;
	}
	
	
}
