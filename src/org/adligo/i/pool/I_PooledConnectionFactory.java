package org.adligo.i.pool;

import java.io.IOException;

public interface I_PooledConnectionFactory<T extends I_PooledConnection> {
	public T create() throws IOException;
}
