/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;

public class PreparedStatementBuilder {

    private final PreparedStatement statement;
    private int parameterIndex = 1;

    PreparedStatementBuilder(Connection connection, String sql) throws SQLException {
        this.statement = connection.prepareStatement(sql);
    }

    public PreparedStatementBuilder withParam(double param) throws SQLException {
        statement.setDouble(parameterIndex, param);
        parameterIndex++;
        return this;
    }

    public int execute() throws SQLException {
        statement.execute();

        // TODO remove debug output
        JDBCFacade.printWarnings(statement);
        return statement.getUpdateCount();
    }

    public ExtendedResultSet executeQuery() throws SQLException {
        ExtendedResultSet resultSet = new ExtendedResultSet(statement.executeQuery());

        // TODO remove debug output
        JDBCFacade.printWarnings(statement);

        return resultSet;
    }

}
