package com.nhl.link.move.runtime.task.sourcekeys;

import com.nhl.link.move.CountingRowReader;
import com.nhl.link.move.Execution;
import com.nhl.link.move.LmTask;
import com.nhl.link.move.Row;
import com.nhl.link.move.RowReader;
import com.nhl.link.move.batch.BatchProcessor;
import com.nhl.link.move.batch.BatchRunner;
import com.nhl.link.move.extractor.Extractor;
import com.nhl.link.move.extractor.model.ExtractorName;
import com.nhl.link.move.runtime.extractor.IExtractorService;
import com.nhl.link.move.runtime.task.BaseTask;
import com.nhl.link.move.runtime.token.ITokenManager;

import java.util.HashSet;
import java.util.Map;

/**
 * An {@link LmTask} that extracts all the keys from the source data store. The
 * result is stored in memory in the Execution object.
 * 
 * @since 1.3
 */
public class SourceKeysTask extends BaseTask {

	public static final String RESULT_KEY = SourceKeysTask.class.getName() + ".RESULT";

	private int batchSize;
	private IExtractorService extractorService;
	private ExtractorName sourceExtractorName;
	private SourceKeysSegmentProcessor processor;

	public SourceKeysTask(ExtractorName sourceExtractorName, int batchSize, IExtractorService extractorService,
			ITokenManager tokenManager, SourceKeysSegmentProcessor processor) {
		super(tokenManager);

		this.sourceExtractorName = sourceExtractorName;
		this.batchSize = batchSize;
		this.extractorService = extractorService;
		this.processor = processor;
	}

	@Override
	protected void run(Execution execution, Map<String, ?> params) {
		execution.setAttribute(RESULT_KEY, new HashSet<>());

		BatchProcessor<Row> batchProcessor = createBatchProcessor(execution);

		try (RowReader data = getRowReader(execution, params)) {
			BatchRunner.create(batchProcessor).withBatchSize(batchSize).run(data);
		}
	}

	@Override
	protected String name() {
		return "SourceKeysTask:" + sourceExtractorName;
	}

	protected BatchProcessor<Row> createBatchProcessor(final Execution execution) {
		return rows -> processor.process(execution, new SourceKeysSegment(rows));
	}

	/**
	 * Returns a RowReader obtained from a named extractor and wrapped in a read
	 * stats counter.
	 */
	protected RowReader getRowReader(final Execution execution, Map<String, ?> extractorParams) {
		Extractor extractor = extractorService.getExtractor(sourceExtractorName);
		return new CountingRowReader(extractor.getReader(extractorParams), execution.getStats());
	}
}
