/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import javax.sql.*;
import java.sql.*;

/**
 * Build statements with a connection.
 */
public class ConnectionBuilder implements AutoCloseable {

    private final Connection connection;

    /**
     * Initialize the builder.
     *
     * @param dataSource the builder use this data source to create the underlying connection.
     * @throws SQLException if a database access error occurs.
     */
    public ConnectionBuilder(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    /**
     * Build a statement, usually a DDL (Data Definition Language) command like <code>CREATE</code>,
     * <code>ALTER</code> or <code>DROP</code>.
     *
     * @param sql a DDL command.
     * @return a builder for a statement.
     * @throws SQLException if a database access error occurs.
     */
    public StatementBuilder statement(String sql) throws SQLException {
        return new StatementBuilder(connection, sql);
    }

    /**
     * Build a prepared statement, usually a DML (Data Manipulation Language) command like <code>SELECT</code>,
     * <code>INSERT</code>, <code>UPDATE</code> or <code>DELETE</code>.
     *
     * @param sql a DML command.
     * @return a builder for a prepared statement.
     * @throws SQLException if a database access error occurs.
     */
    public PreparedStatementBuilder preparedStatement(String sql) throws SQLException {
        return new PreparedStatementBuilder(connection, sql);
    }

    /**
     * Close the underlying connection.
     *
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void close() throws SQLException {
        connection.close();
    }

}
