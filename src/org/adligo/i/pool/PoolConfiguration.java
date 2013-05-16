package org.adligo.i.pool;

import java.security.InvalidParameterException;

public class PoolConfiguration<T extends I_PooledConnection> implements I_PoolConfiguration<T> {
	private PoolConfigurationMutant<T> mutant;

	public PoolConfiguration() {
		mutant = new PoolConfigurationMutant<T>();
	}
	
	public PoolConfiguration( I_PoolConfiguration<T> config) throws InvalidParameterException {
		mutant = new PoolConfigurationMutant<T>(config);
	}
	
	public int getMin() {
		return mutant.getMin();
	}

	public int getMax() {
		return mutant.getMax();
	}

	public String getName() {
		return mutant.getName();
	}

	public I_PooledConnectionFactory<T> getFactory() {
		return mutant.getFactory();
	}
	
}
