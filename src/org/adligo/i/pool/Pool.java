package org.adligo.i.pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;

public class Pool<T extends PooledConnection> {
	private static final Log log = LogFactory.getLog(Pool.class);
	private ArrayBlockingQueue<T> availableConnections;
	private ArrayBlockingQueue<T> activeConnections;
	private String name;
	private volatile int connectionsCreated = 0;
	private volatile int connectionsDisposed = 0;
	private int min;
	private int max;
	private I_PooledConnectionFactory<T>factory;
	/**
	 * default to 3 minutes
	 */
	private int reclaimTime = 60000 * 3;
	
	public Pool(PoolConfiguration<T> config) {
		min = config.getMin();
		max = config.getMax();
		if (max < min) {
			max = min;
		}
		name = config.getName();
		availableConnections = new ArrayBlockingQueue<T>(max);
		activeConnections = new ArrayBlockingQueue<T>(max);
		factory = config.getFactory();
		for (int i = 0; i < min; i++) {
			try {
				T t = factory.create();
				t.setPool(this);
				availableConnections.add(t);
				connectionsCreated++;
			} catch (IOException x) {
				log.error(x.getMessage(), x);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	void returnConnection(I_PooledConnection p) {
		if (!availableConnections.contains(p)) {
			availableConnections.add((T) p);
			activeConnections.remove(p);
		}
	}
	
	public T getConnection() {
		T toRet = attemptGetConnection(10, TimeUnit.MILLISECONDS);
		if (toRet != null) {
			return toRet;
		}
		addConnectionIfNotAtMax();
		toRet = attemptGetConnection(20, TimeUnit.MILLISECONDS);
		if (toRet != null) {
			return toRet;
		}
		addConnectionIfNotAtMax();
		toRet = attemptGetConnection(6, TimeUnit.SECONDS);
		if (toRet != null) {
			return toRet;
		}
		addConnectionIfNotAtMax();
		toRet = attemptGetConnection(1, TimeUnit.MINUTES);
		if (toRet == null) {
			log.error("No Connection available from pool " + name + 
						" after 1 Minute.");
		}
		return toRet;
	}

	private T attemptGetConnection(int time, TimeUnit tu) {
		try {
			T toRet = availableConnections.poll(time, tu);
			if (toRet != null) {
				if (!toRet.isOK()) {
					toRet.dispose();
					connectionsDisposed++;
				} else {
					activeConnections.add(toRet);
					toRet.markActive();
					return toRet;
				}
			}
		} catch (InterruptedException x) {
			log.error(x.getMessage(), x);
		}
		if (log.isInfoEnabled()) {
			log.info("No Connection available from pool " + name + 
					" after " + time + " " + tu);
		}
		return null;
	}
	
	private synchronized void addConnectionIfNotAtMax() {
		recliamConnections();
		int size = availableConnections.size();
		int total = size + activeConnections.size();
		if (log.isInfoEnabled()) {
			log.info("Checking if a new connection should be created currently " +total + " out of max " + max + ".");
		}
		if (total < max) {
			try {
				T t = factory.create();
				t.setPool(this);
				availableConnections.add(t);
				connectionsCreated++;
			} catch (IOException x) {
				log.error(x.getMessage(), x);
			}
		}
	}
	
	public I_PoolStats getStats() {
		PoolStatsMutant psm = new PoolStatsMutant();
		int avail = availableConnections.size();
		int act = activeConnections.size();
		psm.setActiveConnections(act);
		psm.setInactiveConnections(avail);
		psm.setDisposedConnections(connectionsDisposed);
		psm.setCreatedConnections(connectionsCreated);
		psm.setMaxConnections(max);
		psm.setTotalConnections(avail + act);
		return psm;
	}

	public String getName() {
		return name;
	}
	
	List<T> getActiveConnectionList() {
		return new ArrayList<T>(activeConnections);
	}
	
	void recliamConnections() {
		List<? extends PooledConnection> conns = getActiveConnectionList();
		for (PooledConnection con: conns) {
			if (con.reclaimAfter(reclaimTime)){ 
				log.warn("Reclaimed connection from pool " + getName());
			}
		}
	}
}
