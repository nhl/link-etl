package com.nhl.link.etl.runtime.task.delete;

import java.util.List;
import java.util.Map;

import org.apache.cayenne.ObjectContext;

public class DeleteSegment<T> {

	private ObjectContext context;
	private List<T> targets;

	private Map<Object, T> mappedTargets;
	private List<Map<String, Object>> matchedSources;
	private List<T> missingTargets;

	public DeleteSegment(ObjectContext context, List<T> targets) {
		this.targets = targets;
		this.context = context;
	}

	public ObjectContext getContext() {
		return context;
	}

	public List<T> getTargets() {
		return targets;
	}

	public Map<Object, T> getMappedTargets() {
		return mappedTargets;
	}

	public void setMappedTargets(Map<Object, T> mappedTargets) {
		this.mappedTargets = mappedTargets;
	}

	public List<Map<String, Object>> getMatchedSources() {
		return matchedSources;
	}

	public void setMatchedSources(List<Map<String, Object>> convertedRows) {
		this.matchedSources = convertedRows;
	}

	public List<T> getMissingTargets() {
		return missingTargets;
	}

	public void setMissingTargets(List<T> deleted) {
		this.missingTargets = deleted;
	}
}
