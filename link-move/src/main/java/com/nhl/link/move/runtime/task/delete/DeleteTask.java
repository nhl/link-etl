package com.nhl.link.move.runtime.task.delete;

import com.nhl.link.move.Execution;
import com.nhl.link.move.batch.BatchProcessor;
import com.nhl.link.move.batch.BatchRunner;
import com.nhl.link.move.runtime.cayenne.ITargetCayenneService;
import com.nhl.link.move.runtime.task.BaseTask;
import com.nhl.link.move.runtime.token.ITokenManager;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.ResultIterator;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.ObjectSelect;

import java.util.Map;

/**
 * A task that allows to delete target objects not present in the source.
 * 
 * @since 1.3
 */
public class DeleteTask<T extends DataObject> extends BaseTask {

	String extractorName;
	int batchSize;
	Class<T> type;
	Expression targetFilter;

	private DeleteSegmentProcessor<T> processor;
	private ITargetCayenneService targetCayenneService;

	public DeleteTask(String extractorName, int batchSize, Class<T> type, Expression targetFilter,
			ITargetCayenneService targetCayenneService, ITokenManager tokenManager, DeleteSegmentProcessor<T> processor) {

		super(tokenManager);

		this.extractorName = extractorName;
		this.batchSize = batchSize;
		this.type = type;
		this.targetFilter = targetFilter;
		this.targetCayenneService = targetCayenneService;
		this.processor = processor;
	}

	@Override
	protected void run(Execution execution, Map<String, ?> params) {
		BatchProcessor<T> batchProcessor = createBatchProcessor(execution);
		ResultIterator<T> data = createTargetSelect();

		try {
			BatchRunner.create(batchProcessor).withBatchSize(batchSize).run(data);
		} finally {
			// TODO: ResultIterator should be made Closeable in Cayenne,
			// then we can use try-with-resources.
			data.close();
		}
	}

	@Override
	protected String name() {
		return "DeleteTask:" + extractorName;
	}

	protected ResultIterator<T> createTargetSelect() {
		ObjectSelect<T> query = ObjectSelect.query(type).where(targetFilter);
		return targetCayenneService.newContext().iterator(query);
	}

	protected BatchProcessor<T> createBatchProcessor(final Execution execution) {
		return segment -> {
            // executing in the select context..
            ObjectContext context = segment.get(0).getObjectContext();
            processor.process(execution, new DeleteSegment<>(context, segment));
        };
	}

}
