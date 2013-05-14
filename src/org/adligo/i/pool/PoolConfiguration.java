package org.adligo.i.pool;

public class PoolConfiguration<T extends I_PooledConnection> implements I_PoolConfiguration<T> {
	private int min = 0;
	private int max = 1;
	private String name;
	private I_PooledConnectionFactory<T> factory;
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getMin()
	 */
	@Override
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getMax()
	 */
	@Override
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getFactory()
	 */
	@Override
	public I_PooledConnectionFactory<T> getFactory() {
		return factory;
	}
	public void setFactory(I_PooledConnectionFactory<T> factory) {
		this.factory = factory;
	}
	
	
}
