/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

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
            handleSQLException(ex);
        }
    }

    protected void handleSQLException(SQLException ex) {
        // TODO remove debug output
        for (Throwable t : ex) {
            String msg = "";
            if (t instanceof SQLException) {
                SQLException e = (SQLException) t;
                msg += "SQL state: " + e.getSQLState() + ", error code: " + e.getErrorCode() + " - ";
            }

            msg += t;
            System.err.println("ERROR: " + msg);
        }

        throw new UncheckedSQLException("SQL command failed: " + ex.getLocalizedMessage(), ex);
    }

}
