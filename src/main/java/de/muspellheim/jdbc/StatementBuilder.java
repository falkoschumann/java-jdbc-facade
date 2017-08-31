/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;
import java.util.logging.*;

/**
 * Build and execute a statement.
 *
 * @see #execute()
 */
public class StatementBuilder {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Statement statement;
    private final String sql;

    /**
     * Initialize the builder.
     *
     * @param connection the connection used to create a statement.
     * @param sql        the SQL command for the statement.
     * @throws SQLException if a database access error occurs.
     */
    public StatementBuilder(Connection connection, String sql) throws SQLException {
        this.statement = connection.createStatement();
        this.sql = sql;
    }

    /**
     * Execute the statement and close it after execution.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void execute() throws SQLException {
        log.fine("Execute command: " + sql);
        statement.execute(sql);
        statement.close();
    }

}
