package com.alibaba.druid.bvt.filter.log;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class LogFilterTest6 extends TestCase {

    private DruidDataSource dataSource;
    private WallFilter      wallFilter;

    protected void setUp() throws Exception {
        dataSource = new DruidDataSource();

        dataSource.setUrl("jdbc:h2:mem:log_test;");

        dataSource.setDbType(JdbcConstants.MARIADB);

        dataSource.setFilters("log4j");

        Connection conn = dataSource.getConnection();

        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE t (ID INTEGER, FNAME VARCHAR(50))");
        stmt.execute("INSERT INTO t values (1,'test_name')");

        stmt.close();

        conn.close();

    }

    protected void tearDown() throws Exception {
        dataSource.close();
    }

    public void test_batchExecute() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("UPDATE t SET FNAME=? WHERE ID=1");

        for (int i = 0; i < 5; i++) {
            stmt.setString(1, "test_name_" + i);
            stmt.addBatch();
        }

        stmt.executeBatch();

        stmt.close();
        conn.close();
    }
}
