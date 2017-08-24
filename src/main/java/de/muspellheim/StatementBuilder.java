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

        // TODO remove debug output
        if (statement.getWarnings() != null) {
            for (Throwable t : statement.getWarnings()) {
                String msg = "";
                if (t instanceof SQLException) {
                    SQLException ex = (SQLException) t;
                    msg += "SQL state: " + ex.getSQLState() + ", error code: " + ex.getErrorCode() + " - ";
                }

                msg += t;
                System.err.println("WARNING: " + msg);
            }
        }
    }

}
