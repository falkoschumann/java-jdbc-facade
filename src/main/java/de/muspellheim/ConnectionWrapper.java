/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import java.sql.*;

public class ConnectionWrapper {

    private final Connection connection;

    public ConnectionWrapper(Connection connection) {
        this.connection = connection;
    }

    public StatementBuilder statement(String sql) throws SQLException {
        return new StatementBuilder(connection, sql);
    }

}
