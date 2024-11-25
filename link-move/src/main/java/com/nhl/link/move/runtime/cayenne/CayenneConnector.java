package com.nhl.link.move.runtime.cayenne;

import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.di.BeforeScopeEnd;

/**
 * A {@link JdbcConnector} based on externally provided Cayenne's ServerRuntime.
 * 
 * @since 3.0
 */
public class CayenneConnector implements JdbcConnector {

	private final ServerRuntime runtime;
	private final ObjectContext sharedContext;

	public CayenneConnector(ServerRuntime runtime) {
		this.runtime = runtime;
		this.sharedContext = runtime.newContext();
	}

	@Override
	public ObjectContext sharedContext() {
		return sharedContext;
	}

	@BeforeScopeEnd
	@Override
	public void shutdown() {
		runtime.shutdown();
	}
}
