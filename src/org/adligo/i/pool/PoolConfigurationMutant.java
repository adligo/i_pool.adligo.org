package org.adligo.i.pool;

import java.security.InvalidParameterException;

import org.adligo.i.util.client.StringUtils;

public class PoolConfigurationMutant<T extends I_PooledConnection> implements I_PoolConfiguration<T> {
	public static final String POOL_CONFIGURATION_REQUIRES_A_NON_NULL_FACTORY = "PoolConfiguration requires a non null factory.";
	public static final String POOL_CONFIGURATION_REQUIRES_A_NON_EMPTY_NAME = "PoolConfiguration requires a non empty name.";
	private int _min = 0;
	private int _max = 1;
	private String _name;
	private I_PooledConnectionFactory<T> _factory;
	
	public PoolConfigurationMutant() {}
	
	public PoolConfigurationMutant(I_PoolConfiguration<T> p) 
			throws InvalidParameterException {
		_min = p.getMin();
		_max = p.getMax();
		setName(p.getName());
		setFactory(p.getFactory());
	}
	
	public PoolConfigurationMutant(String name, I_PooledConnectionFactory<T> factory, int max) 
				throws InvalidParameterException {
		setName(name);
		setFactory(factory);
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
	public void setName(String name)  throws InvalidParameterException {
		if (StringUtils.isEmpty(name)) {
			throw new InvalidParameterException(POOL_CONFIGURATION_REQUIRES_A_NON_EMPTY_NAME);
		} else {
			this._name = name;
		}
	}
	/* (non-Javadoc)
	 * @see org.adligo.i.pool.I_PoolConfiguration#getFactory()
	 */
	@Override
	public I_PooledConnectionFactory<T> getFactory() {
		return _factory;
	}
	public void setFactory(I_PooledConnectionFactory<T> factory) throws InvalidParameterException {
		if (factory == null) {
			throw new InvalidParameterException(POOL_CONFIGURATION_REQUIRES_A_NON_NULL_FACTORY);
		} else {
			this._factory = factory;
		}
	}
	
	
}
