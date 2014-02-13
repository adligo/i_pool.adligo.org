package org.adligo.i.pool;

import org.adligo.i.adi.shared.InvokerNames;
import org.adligo.i.adig.shared.GRegistry;
import org.adligo.i.adig.shared.I_GInvoker;

public abstract class PooledConnection implements I_PooledConnection {
	private static I_GInvoker<Object, Long> CLOCK = 
			GRegistry.getInvoker(InvokerNames.CLOCK, Object.class, Long.class);
	private Pool<?> pool;
	private long lastActiveTime;
	
	I_Pool<?> getPool() {
		return pool;
	}

	void setPool(Pool<?> pool) {
		this.pool = pool;
	}

	@Override
	public void returnToPool() {
		pool.returnConnection( this);
	}

	boolean reclaimAfter(int millis) {
		long now = CLOCK.invoke(null);
		long inactive = now - lastActiveTime;
		if (inactive >= millis) {
			returnToPool();
			return true;
		}
		return false;
	}
	
	long getLastActiveTime() {
		return lastActiveTime;
	}

	protected void markActive() {
		lastActiveTime = CLOCK.invoke(null);
	}
}
