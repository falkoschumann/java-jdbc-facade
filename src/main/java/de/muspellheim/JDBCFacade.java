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
            ConnectionWrapper connectionWrapper = new ConnectionWrapper(connection);
            command.execute(connectionWrapper);
        } catch (SQLException ex) {
            throw new JDBCFacadeException("Error executing DDL command", ex);
        }
    }

}
