package org.adligo.i.pool;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.adligo.i.log.shared.Log;
import org.adligo.i.log.shared.LogFactory;

public class ConnectionReclaimer implements Runnable {
	private static final Log log = LogFactory.getLog(ConnectionReclaimer.class);
	private CopyOnWriteArrayList<Pool<?>> pools;

	
	public ConnectionReclaimer() {
		pools = new CopyOnWriteArrayList<>();
	}
	
	/**
	 * trigger through quartz hourly or so unless you know you have a bug?
	 */
	public void run() {
		for (Pool pool: pools) {
			pool.recliamConnections();
		}
	}
	
	public void addPool(Pool<?> p) {
		pools.add(p);
	}
	
	public void removePool(I_Pool<?> p) {
		pools.remove(p);
	}
}
