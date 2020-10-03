package com.nhl.link.move.unit;

import com.nhl.link.move.unit.cayenne.t.*;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.cayenne.CayenneModule;
import io.bootique.cayenne.junit5.CayenneTester;
import io.bootique.jdbc.junit5.Table;
import io.bootique.jdbc.junit5.derby.DerbyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTestScope;
import io.bootique.junit5.BQTestTool;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SQLSelect;
import org.apache.cayenne.query.SQLTemplate;
import org.junit.jupiter.api.BeforeEach;

public abstract class DerbySrcTargetTest extends DerbySrcTest {

    @BQTestTool(BQTestScope.GLOBAL)
    protected static final DerbyTester targetDb = DerbyTester.db();

    @BQTestTool(BQTestScope.GLOBAL)
    protected static final CayenneTester cayenne = CayenneTester.create()
            .deleteBeforeEachTest()
            //  TODO: workaround for a CayenneTester bug: generator skips the "sub" table in vertical
            //   inheritance schema.
            .tables("ti_super", "ti_sub1")
            .entities(Etl1t.class,
                    Etl3t.class,
                    Etl2t.class,
                    Etl4t.class,
                    Etl4t_jt.class,
                    Etl5t.class,
                    Etl6t.class,
                    Etl7t.class,
                    Etl8t.class,
                    Etl9t.class,
                    Etl10t.class,
                    Etl11t.class);

    @BQApp(value = BQTestScope.GLOBAL, skipRun = true)
    protected static final BQRuntime targetApp = Bootique.app()
            .autoLoadModules()
            .module(targetDb.moduleWithTestDataSource("target_db"))
            .module(cayenne.moduleWithTestHooks())
            .module(b -> CayenneModule.extend(b).addProject("cayenne-linketl-tests-targets.xml"))
            .createRuntime();

    protected ObjectContext targetContext;

    @BeforeEach
    public void prepareTarget() {
        this.targetContext = cayenne.getRuntime().newContext();
    }

    /**
     * @deprecated replace with Table API
     */
    protected void targetRunSql(String sql) {
        targetContext.performGenericQuery(new SQLTemplate(Object.class, sql));
    }

    /**
     * @deprecated replace with Table API
     */
    protected int targetScalar(String sql) {
        SQLSelect<Integer> query = SQLSelect.scalarQuery(Integer.class, sql);
        return query.selectOne(targetContext);
    }

    protected Table etl1t() {
        return targetDb.getTable("ETL1T");
    }

    protected Table etl2t() {
        return targetDb.getTable("ETL2T");
    }

    protected Table etl3t() {
        return targetDb.getTable("ETL3T");
    }

    protected Table etl5t() {
        return targetDb.getTable("ETL5T");
    }
}
