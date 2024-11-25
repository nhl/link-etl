package com.nhl.link.move.runtime.cayenne;

import com.nhl.link.move.BaseRowAttribute;
import com.nhl.link.move.Execution;
import com.nhl.link.move.RowAttribute;
import com.nhl.link.move.RowReader;
import com.nhl.link.move.extractor.Extractor;
import com.nhl.link.move.runtime.jdbc.DataRowIterator;
import com.nhl.link.move.runtime.jdbc.JdbcRowReader;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.parser.ASTDbPath;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.query.ObjectSelect;

import java.util.Objects;

/**
 * Extractor that uses Cayenne to stream data row-by-row from the specified database entity.
 *
 * @since 3.0
 */
public class CayenneExtractor implements Extractor {

    private final ObjectContext context;
    private final RowAttribute[] rowHeader;
    private final String objEntityName;

    public CayenneExtractor(ObjectContext context, RowAttribute[] rowHeader, String objEntityName) {
        this.context = context;
        this.rowHeader = rowHeader;
        this.objEntityName = Objects.requireNonNull(objEntityName);
    }

    @Override
    public RowReader getReader(Execution exec) {
        DbEntity sourceEntity = context.getEntityResolver().getObjEntity(objEntityName).getDbEntity();
        ObjectSelect<DataRow> select = ObjectSelect.dbQuery(sourceEntity.getName());

        DataRowIterator it = new DataRowIterator(context.iterator(select));
        RowAttribute[] rowHeader;
        if (this.rowHeader != null) {
            rowHeader = this.rowHeader;
        } else {
            rowHeader = new RowAttribute[sourceEntity.getAttributes().size()];
            int ordinal = 0;
            for (DbAttribute attr : sourceEntity.getAttributes()) {
                rowHeader[ordinal] = new BaseRowAttribute(
                        null,
                        attr.getName(),
                        convertToDbPath(attr.getName()),
                        ordinal
                );
                ordinal++;
            }
        }

        exec.getLogger().extractorStarted(rowHeader, select);
        return new JdbcRowReader(rowHeader, it);
    }

    private static String convertToDbPath(String path) {
        return ASTDbPath.DB_PREFIX + path;
    }
}
