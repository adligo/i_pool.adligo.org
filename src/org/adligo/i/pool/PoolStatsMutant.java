package org.adligo.i.pool;

public class PoolStatsMutant implements I_PoolStats {
	private int maxConnections;
	private int totalConnections;
	private int activeConnections;
	private int inactiveConnections;
	private int disposedConnections;
	private int createdConnections;
	
	public int getMaxConnections() {
		return maxConnections;
	}
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}
	public int getTotalConnections() {
		return totalConnections;
	}
	public void setTotalConnections(int totalConnections) {
		this.totalConnections = totalConnections;
	}
	public int getActiveConnections() {
		return activeConnections;
	}
	public void setActiveConnections(int activeConnections) {
		this.activeConnections = activeConnections;
	}
	public int getInactiveConnections() {
		return inactiveConnections;
	}
	public void setInactiveConnections(int inactiveConnections) {
		this.inactiveConnections = inactiveConnections;
	}
	public int getDisposedConnections() {
		return disposedConnections;
	}
	public void setDisposedConnections(int disposedConnections) {
		this.disposedConnections = disposedConnections;
	}
	public int getCreatedConnections() {
		return createdConnections;
	}
	public void setCreatedConnections(int createdConnections) {
		this.createdConnections = createdConnections;
	}
}
