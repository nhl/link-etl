package com.nhl.link.etl.mapper;

import java.util.Map;

import org.apache.cayenne.exp.Expression;

/**
 * A strategy object for calculating a "key" from source and target objects of
 * the ETL. Keys are then used during the LOAD phase of the ETL execution to
 * match sources and targets.
 */
public interface Mapper<T> {

	Object keyForTarget(T target);

	Object keyForSource(Map<String, Object> source);

	Expression expressionForKey(Object key);
}