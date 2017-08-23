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

    public void executeDDLCommand(DDLCommand command) {
        try (Connection connection = dataSource.getConnection()) {
            executeCommand(command, connection);
        } catch (SQLException ex) {
            handleSQLException(ex);
        }
    }

    protected void executeCommand(DDLCommand command, Connection connection) throws SQLException {
        ConnectionWrapper connectionWrapper = new ConnectionWrapper(connection);
        command.execute(connectionWrapper);
    }

    protected void handleSQLException(SQLException ex) {
        throw new UncheckedSQLException("Error executing DDL command", ex);
    }

}
