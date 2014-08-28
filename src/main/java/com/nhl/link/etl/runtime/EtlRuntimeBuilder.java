package com.nhl.link.etl.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.DIBootstrap;
import org.apache.cayenne.di.Injector;
import org.apache.cayenne.di.MapBuilder;
import org.apache.cayenne.di.Module;

import com.nhl.link.etl.connect.Connector;
import com.nhl.link.etl.keybuilder.IKeyBuilderFactory;
import com.nhl.link.etl.keybuilder.KeyBuilderFactory;
import com.nhl.link.etl.runtime.cayenne.ITargetCayenneService;
import com.nhl.link.etl.runtime.cayenne.TargetCayenneService;
import com.nhl.link.etl.runtime.connect.ConnectorService;
import com.nhl.link.etl.runtime.connect.IConnectorFactory;
import com.nhl.link.etl.runtime.connect.IConnectorService;
import com.nhl.link.etl.runtime.extract.ClasspathExtractorConfigLoader;
import com.nhl.link.etl.runtime.extract.ExtractorService;
import com.nhl.link.etl.runtime.extract.IExtractorConfigLoader;
import com.nhl.link.etl.runtime.extract.IExtractorFactory;
import com.nhl.link.etl.runtime.extract.IExtractorService;
import com.nhl.link.etl.runtime.jdbc.JdbcExtractorFactory;
import com.nhl.link.etl.runtime.task.ITaskService;
import com.nhl.link.etl.runtime.task.TaskService;
import com.nhl.link.etl.runtime.token.ITokenManager;
import com.nhl.link.etl.runtime.token.InMemoryTokenManager;
import com.nhl.link.etl.runtime.xml.HttpXmlExtractorFactory;

/**
 * A builder class that helps to assemble working LinkEtl stack.
 */
public class EtlRuntimeBuilder {

	public static final String CONNECTORS_MAP = "com.nhl.link.etl.connectors";
	public static final String CONNECTOR_FACTORIES_MAP = "com.nhl.link.etl.connector.factories";
	public static final String EXTRACTOR_FACTORIES_MAP = "com.nhl.link.etl.extractor.factories";

	public static final String JDBC_EXTRACTOR_TYPE = "jdbc";
	public static final String HTTP_XML_EXTRACTOR_TYPE = "httpXml";

	public static final String START_TOKEN_VAR = "startToken";
	public static final String END_TOKEN_VAR = "endToken";

	private Map<String, Connector> connectors;
	private Map<String, IConnectorFactory> connectorFactories;
	private Map<String, Class<? extends IConnectorFactory>> connectorFactoryTypes;
	private Map<String, IExtractorFactory> extractorFactories;
	private Map<String, Class<? extends IExtractorFactory>> extractorFactoryTypes;
	private IExtractorConfigLoader extractorConfigLoader;
	private ITokenManager tokenManager;
	private ServerRuntime targetRuntime;

	public EtlRuntimeBuilder() {
		this.connectors = new HashMap<>();
		this.connectorFactories = new HashMap<>();
		this.connectorFactoryTypes = new HashMap<>();
		this.extractorFactories = new HashMap<>();
		this.extractorFactoryTypes = new HashMap<>();

		// always add JDBC extractors...
		extractorFactoryTypes.put(JDBC_EXTRACTOR_TYPE, JdbcExtractorFactory.class);
		extractorFactoryTypes.put(HTTP_XML_EXTRACTOR_TYPE, HttpXmlExtractorFactory.class);
	}

	/**
	 * Sets a target Cayenne runtime for this ETL stack.
	 */
	public EtlRuntimeBuilder withTargetRuntime(ServerRuntime targetRuntime) {
		this.targetRuntime = targetRuntime;
		return this;
	}

	public EtlRuntimeBuilder withTokenManager(ITokenManager tokenManager) {
		this.tokenManager = tokenManager;
		return this;
	}

	public EtlRuntimeBuilder withConnector(String id, Connector connector) {
		connectors.put(id, connector);
		return this;
	}

	public EtlRuntimeBuilder withConnectorFactory(Class<? extends Connector> connectorType, IConnectorFactory factory) {
		connectorFactories.put(connectorType.getName(), factory);
		return this;
	}

	public EtlRuntimeBuilder withConnectorFactory(Class<? extends Connector> connectorType,
			Class<? extends IConnectorFactory> factoryType) {
		connectorFactoryTypes.put(connectorType.getName(), factoryType);
		return this;
	}

	/**
	 * Adds an extra factory to the map of extractor factories. Note that
	 * {@link JdbcExtractorFactory} is loaded by default and does not have to be
	 * configured explicitly.
	 */
	public EtlRuntimeBuilder withExtractorFactory(String extractorType, Class<? extends IExtractorFactory> factoryType) {
		extractorFactoryTypes.put(extractorType, factoryType);
		return this;
	}

	/**
	 * Adds an extra factory to the map of extractor factories. Note that
	 * {@link JdbcExtractorFactory} is loaded by default and does not have to be
	 * configured explicitly.
	 */
	public EtlRuntimeBuilder withExtractorFactory(String extractorType, IExtractorFactory factory) {
		extractorFactories.put(extractorType, factory);
		return this;
	}

	public EtlRuntimeBuilder withExtractorConfigLoader(IExtractorConfigLoader extractorConfigLoader) {
		this.extractorConfigLoader = extractorConfigLoader;
		return this;
	}

	public EtlRuntime build() throws IllegalStateException {

		if (targetRuntime == null) {
			throw new IllegalStateException("Required Cayenne 'targetRuntime' is not set");
		}

		if (extractorConfigLoader == null) {
			extractorConfigLoader = new ClasspathExtractorConfigLoader();
		}

		if (tokenManager == null) {
			tokenManager = new InMemoryTokenManager();
		}

		Module etlModule = new Module() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void configure(Binder binder) {

				binder.<Connector> bindMap(EtlRuntimeBuilder.CONNECTORS_MAP).putAll(connectors);

				MapBuilder<IConnectorFactory> connectorFactories = binder
						.<IConnectorFactory> bindMap(EtlRuntimeBuilder.CONNECTOR_FACTORIES_MAP);

				connectorFactories.putAll(EtlRuntimeBuilder.this.connectorFactories);

				for (Entry<String, Class<? extends IConnectorFactory>> e : connectorFactoryTypes.entrySet()) {

					// a bit ugly - need to bind all factory types explicitly
					// before placing then in a map .. also must drop
					// parameterization to be able to bind with non-specific
					// boundaries (<? extends ...>)
					Class efType = e.getValue();
					binder.bind(efType).to(efType);

					connectorFactories.put(e.getKey(), e.getValue());
				}

				MapBuilder<IExtractorFactory> extractorFactories = binder
						.<IExtractorFactory> bindMap(EtlRuntimeBuilder.EXTRACTOR_FACTORIES_MAP);
				extractorFactories.putAll(EtlRuntimeBuilder.this.extractorFactories);

				for (Entry<String, Class<? extends IExtractorFactory>> e : extractorFactoryTypes.entrySet()) {

					// a bit ugly - need to bind all factory types explicitly
					// before placing then in a map .. also must drop
					// parameterization to be able to bind with non-specific
					// boundaries (<? extends ...>)
					Class efType = e.getValue();
					binder.bind(efType).to(efType);

					extractorFactories.put(e.getKey(), e.getValue());
				}

				// Binding CayenneService for the *target*... Note that binding
				// ServerRuntime directly would result in undesired shutdown
				// when the ETL module is shutdown.
				binder.bind(ITargetCayenneService.class).toInstance(new TargetCayenneService(targetRuntime));

				binder.bind(IExtractorConfigLoader.class).toInstance(extractorConfigLoader);
				binder.bind(IExtractorService.class).to(ExtractorService.class);
				binder.bind(IConnectorService.class).to(ConnectorService.class);
				binder.bind(ITaskService.class).to(TaskService.class);
				binder.bind(ITokenManager.class).toInstance(tokenManager);
				binder.bind(IKeyBuilderFactory.class).to(KeyBuilderFactory.class);
			}
		};

		final Injector injector = DIBootstrap.createInjector(etlModule);

		return new EtlRuntime() {

			@Override
			public ITaskService getTaskService() {
				return injector.getInstance(ITaskService.class);
			}

			@Override
			public void shutdown() {
				injector.shutdown();
			}
		};
	}

}
