/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import javax.sql.*;
import java.sql.*;

public class JDBCFacade {

    private DataSource dataSource;

    public JDBCFacade(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSQLCommand(SQLCommand command) {
        try (ConnectionBuilder connection = new ConnectionBuilder(dataSource)) {
            command.execute(connection);
        } catch (SQLException ex) {
            printError(ex); // TODO remove debug output
            throw new UncheckedSQLException("SQL command failed: " + ex.getLocalizedMessage(), ex);
        }
    }

    public <T> T executeSQLQuery(SQLQuery<T> command) {
        try (ConnectionBuilder connection = new ConnectionBuilder(dataSource)) {
            return command.execute(connection);
        } catch (SQLException ex) {
            printError(ex); // TODO remove debug output
            throw new UncheckedSQLException("SQL query failed: " + ex.getLocalizedMessage(), ex);
        }
    }

    static void printError(SQLException ex) {
        for (Throwable t : ex) {
            String msg = "";
            if (t instanceof SQLException) {
                SQLException e = (SQLException) t;
                msg += "SQL state: " + e.getSQLState() + ", error code: " + e.getErrorCode() + " - ";
            }

            msg += t;
            System.err.println("ERROR: " + msg);
        }
    }

    static void printWarnings(Statement statement) throws SQLException {
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
