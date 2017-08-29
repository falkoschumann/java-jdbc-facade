/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import javax.sql.*;
import java.sql.*;

/**
 * Facade for JDBC with fluent API.
 */
public class JDBCFacade {

    private DataSource dataSource;

    /**
     * The facade use one data source.
     *
     * @param dataSource the data source using by the facade.
     */
    public JDBCFacade(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Run a SQL command, usually a DDL (Data Definition Language) command like <code>CREATE</code>,
     * <code>ALTER</code> or <code>DROP</code>.
     *
     * @param command the SQL command.
     * @throws SQLException if a database access error occurs.
     */
    public void executeSQLCommand(SQLCommand command) throws SQLException {
        try (ConnectionBuilder connection = new ConnectionBuilder(dataSource)) {
            command.execute(connection);
        }
    }

    /**
     * Run a SQL query, usually a DML (Data Manipulation Language) command like <code>SELECT</code>,
     * <code>INSERT</code>, <code>UPDATE</code> or <code>DELETE</code>.
     *
     * @param command the SQL command.
     * @param <T>     the return type of the query, usually an single data structure or list of data structures.
     * @return the query result.
     * @throws SQLException if a database access error occurs.
     */
    public <T> T executeSQLQuery(SQLQuery<T> command) throws SQLException {
        try (ConnectionBuilder connection = new ConnectionBuilder(dataSource)) {
            return command.execute(connection);
        }
    }

}
