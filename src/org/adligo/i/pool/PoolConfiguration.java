package org.adligo.i.pool;

public class PoolConfiguration<T extends I_PooledConnection> implements I_PoolConfiguration<T> {
	private int _min = 0;
	private int _max = 1;
	private String _name;
	private I_PooledConnectionFactory<T> _factory;
	
	public PoolConfiguration() {}
	
	public PoolConfiguration(String name, I_PooledConnectionFactory<T> factory, int max) {
		_name = name;
		_factory = factory;
		_max = max;
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getMin()
	 */
	@Override
	public int getMin() {
		return _min;
	}
	public void setMin(int min) {
		this._min = min;
	}
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getMax()
	 */
	@Override
	public int getMax() {
		return _max;
	}
	public void setMax(int max) {
		this._max = max;
	}
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getName()
	 */
	@Override
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getFactory()
	 */
	@Override
	public I_PooledConnectionFactory<T> getFactory() {
		return _factory;
	}
	public void setFactory(I_PooledConnectionFactory<T> factory) {
		this._factory = factory;
	}
	
	
}
