/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import javax.sql.*;
import java.sql.*;

public class ConnectionBuilder implements AutoCloseable {

    private final Connection connection;

    public ConnectionBuilder(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public StatementBuilder statement(String sql) throws SQLException {
        return new StatementBuilder(connection, sql);
    }

    public PreparedStatementBuilder preparedStatement(String sql) throws SQLException {
        return new PreparedStatementBuilder(connection, sql);
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

}
