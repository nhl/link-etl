package com.nhl.link.move;

import com.nhl.link.move.extractor.model.ExtractorName;
import com.nhl.link.move.log.LmLogger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A single execution of an {@link LmTask}. Tracks task parameters and execution statistics.
 */
public class Execution implements AutoCloseable {

    protected final String taskName;
    protected final ExtractorName extractorName;
    protected final Map<String, ?> parameters;
    protected final Map<String, Object> attributes;
    protected final LmLogger logger;
    protected final ExecutionStats stats;

    /**
     * @since 3.0
     */
    public Execution(String taskName, ExtractorName extractorName, Map<String, ?> params, LmLogger logger) {
        this.taskName = taskName;
        this.extractorName = extractorName;
        this.parameters = params;
        this.attributes = new ConcurrentHashMap<>();
        this.logger = logger;
        this.stats = new ExecutionStats().executionStarted();
    }

    @Override
    public void close() {
        stats.executionStopped();
    }

    @Override
    public String toString() {

        // generate JSON-ish output

        StringBuilder paramsOut = new StringBuilder("{");
        for (Entry<String, ?> p : parameters.entrySet()) {
            append(paramsOut, p.getKey(), p.getValue());
        }

        StringBuilder out = new StringBuilder("{");
        append(out, "created", stats.getCreated());
        append(out, "deleted", stats.getDeleted());
        append(out, "duration", stats.getDuration());
        append(out, "extracted", stats.getExtracted());
        append(out, "extractor", extractorName);
        append(out, "parameters", paramsOut.append("}").toString(), false);
        append(out, "startedOn", stats.getStartedOn());
        append(out, "status", stats.isStopped() ? "finished" : "in progress");
        append(out, "task", taskName);
        append(out, "updated", stats.getUpdated());

        return out.append("}").toString();
    }

    private void append(StringBuilder out, String key, Object val) {
        append(out, key, val, !(val instanceof Number));
    }

    private void append(StringBuilder out, String key, Object val, boolean quote) {
        if (val == null) {
            return;
        }

        if (out.length() > 1) {
            out.append(',');
        }

        out.append("\"").append(key).append("\":");

        if (quote) {
            out.append("\"").append(val).append("\"");

        } else {
            out.append(val);
        }
    }

    /**
     * @since 2.8
     * @deprecated since 3.0 {@link #getExtractorName()} and {@link #getTaskName()} are used instead to identify the
     * execution.
     */
    @Deprecated(since = "3.0")
    public String getName() {
        return taskName + ":" + extractorName;
    }

    /**
     * @since 3.0
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @since 3.0
     */
    public ExtractorName getExtractorName() {
        return extractorName;
    }

    /**
     * Creates task execution report as a map of labels vs. values.
     *
     * @deprecated since 3.0. Execution reports are used primarily for logging, which is now handled by
     * {@link com.nhl.link.move.log.LmLogger}, so this API is no longer useful.
     */
    @Deprecated(since = "3.0")
    public Map<String, Object> createReport() {

        // keep order of insertion consistent so that the report is easily printable
        Map<String, Object> report = new LinkedHashMap<>();

        report.put("Task", getName());

        for (Entry<String, ?> p : parameters.entrySet()) {
            report.put("Parameter[" + p.getKey() + "]", p.getValue());
        }

        if (stats.isStopped()) {
            report.put("Status", "finished");
            report.put("Started on", stats.getStartedOn());
            report.put("Duration", stats.getDuration());
        } else {
            report.put("Status", "in progress");
            report.put("Started on", stats.getStartedOn());
        }

        report.put("Extracted", stats.getExtracted());
        report.put("Created", stats.getCreated());
        report.put("Updated", stats.getUpdated());
        report.put("Deleted", stats.getDeleted());

        return report;
    }

    /**
     * @since 1.3
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * @since 1.3
     */
    public void setAttribute(String key, Object value) {
        if (value == null) {
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    /**
     * @since 1.3
     */
    public Map<String, ?> getParameters() {
        return parameters;
    }

    /**
     * @since 3.0
     */
    public LmLogger getLogger() {
        return logger;
    }

    public ExecutionStats getStats() {
        return stats;
    }
}
