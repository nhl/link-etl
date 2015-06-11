package com.nhl.link.etl;

import com.nhl.link.etl.mapper.Mapper;

/**
 * A builder of an {@link EtlTask} that extracts all the keys from the source
 * data store.
 * 
 * @since 1.3
 */
public interface SourceKeysBuilder {

	/**
	 * Creates a new task based on the builder information.
	 */
	EtlTask task() throws IllegalStateException;

	/**
	 * Defines the location and name of the source data extractor.
	 * 
	 * @since 1.4
	 */
	SourceKeysBuilder sourceExtractor(String location, String name);

	/**
	 * Defines the location of the source data extractor. The name of extractor
	 * is assumed to be "default_extractor".
	 * 
	 * @since 1.3
	 */
	SourceKeysBuilder sourceExtractor(String location);

	/**
	 * Defines the number of records that are processed together as a single
	 * batch. If not specified, default size of 500 records is used.
	 */
	SourceKeysBuilder batchSize(int batchSize);

	SourceKeysBuilder matchBy(Mapper mapper);

	SourceKeysBuilder matchBy(String... columns);

}
