/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import com.mysql.jdbc.jdbc2.optional.*;
import org.junit.*;

import javax.sql.*;

public class DDLTest {

    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(8889);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        this.dataSource = dataSource;
    }

    @Test
    public void testCreateSchema() throws Exception {
        JDBCFacade jdbc = new JDBCFacade(dataSource);
        jdbc.executeDDLCommand(connection -> connection.statement("DROP SCHEMA IF EXISTS oshop").execute());
        jdbc.executeDDLCommand(connection -> connection.statement("CREATE SCHEMA oshop CHARACTER SET utf8 COLLATE utf8_unicode_ci").execute());
    }

}
