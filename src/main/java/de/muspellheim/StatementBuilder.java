/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import java.sql.*;

public class StatementBuilder {

    private final Statement statement;
    private final String sql;

    StatementBuilder(Connection connection, String sql) throws SQLException {
        this.statement = connection.createStatement();
        this.sql = sql;
    }

    public void execute() throws SQLException {
        statement.execute(sql);
    }

}
