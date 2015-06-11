package com.nhl.link.etl.runtime.task.sourcekeys;

import com.nhl.link.etl.EtlTask;
import com.nhl.link.etl.SourceKeysBuilder;
import com.nhl.link.etl.extractor.model.ExtractorModel;
import com.nhl.link.etl.extractor.model.ExtractorName;
import com.nhl.link.etl.mapper.Mapper;
import com.nhl.link.etl.runtime.extractor.IExtractorService;
import com.nhl.link.etl.runtime.key.IKeyAdapterFactory;
import com.nhl.link.etl.runtime.path.EntityPathNormalizer;
import com.nhl.link.etl.runtime.task.BaseTaskBuilder;
import com.nhl.link.etl.runtime.task.SourceMapperBuilder;
import com.nhl.link.etl.runtime.task.createorupdate.RowConverter;
import com.nhl.link.etl.runtime.token.ITokenManager;

/**
 * @since 1.3
 */
public class DefaultSourceKeysBuilder extends BaseTaskBuilder implements SourceKeysBuilder {

	private IExtractorService extractorService;
	private ITokenManager tokenManager;
	private SourceMapperBuilder mapperBuilder;
	private EntityPathNormalizer pathNormalizer;

	private ExtractorName sourceExtractorName;

	public DefaultSourceKeysBuilder(EntityPathNormalizer pathNormalizer, IExtractorService extractorService,
			ITokenManager tokenManager, IKeyAdapterFactory keyAdapterFactory) {
		this.extractorService = extractorService;
		this.tokenManager = tokenManager;
		this.mapperBuilder = new SourceMapperBuilder(pathNormalizer, keyAdapterFactory);
		this.pathNormalizer = pathNormalizer;
	}

	@Override
	public EtlTask task() throws IllegalStateException {

		if (sourceExtractorName == null) {
			throw new IllegalStateException("Required 'extractorName' is not set");
		}

		return new SourceKeysTask(sourceExtractorName, batchSize, extractorService, tokenManager, createProcessor());
	}

	private SourceKeysSegmentProcessor createProcessor() {

		Mapper mapper = mapperBuilder.build();
		SourceKeysCollector sourceMapper = new SourceKeysCollector(mapper);
		RowConverter converter = new RowConverter(pathNormalizer);
		return new SourceKeysSegmentProcessor(converter, sourceMapper);
	}

	@Override
	public SourceKeysBuilder sourceExtractor(String location, String name) {
		this.sourceExtractorName = ExtractorName.create(location, name);
		return this;
	}

	@Override
	public SourceKeysBuilder sourceExtractor(String location) {
		// v.1 style naming...
		return sourceExtractor(location, ExtractorModel.DEFAULT_NAME);
	}

	@Override
	public SourceKeysBuilder batchSize(int batchSize) {
		this.batchSize = batchSize;
		return this;
	}

	@Override
	public SourceKeysBuilder matchBy(Mapper mapper) {
		mapperBuilder.matchBy(mapper);
		return this;
	}

	@Override
	public SourceKeysBuilder matchBy(String... columns) {
		mapperBuilder.matchBy(columns);
		return this;
	}
}
