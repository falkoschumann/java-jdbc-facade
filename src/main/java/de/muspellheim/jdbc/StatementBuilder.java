/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;

/**
 * Build and execute a statement.
 *
 * @see #execute()
 */
public class StatementBuilder {

    private final Statement statement;
    private final String sql;

    /**
     * Initialize the builder.
     *
     * @param connection the connection used to create a statement.
     * @param sql        the SQL command for the statement.
     * @throws SQLException
     */
    public StatementBuilder(Connection connection, String sql) throws SQLException {
        this.statement = connection.createStatement();
        this.sql = sql;
    }

    /**
     * Execute the statement and close it after execution.
     *
     * @throws SQLException
     */
    public void execute() throws SQLException {
        statement.execute(sql);
        statement.close();
    }

}
