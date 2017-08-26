/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;
import java.time.*;

public class PreparedStatementBuilder {

    private final PreparedStatement statement;
    private int parameterIndex = 1;

    PreparedStatementBuilder(Connection connection, String sql) throws SQLException {
        this.statement = connection.prepareStatement(sql);
    }

    public PreparedStatementBuilder withParam(int x) throws SQLException {
        return withParam(s -> s.setInt(parameterIndex, x));
    }

    public PreparedStatementBuilder withParam(double x) throws SQLException {
        return withParam(s -> s.setDouble(parameterIndex, x));
    }

    public PreparedStatementBuilder withParam(String x) throws SQLException {
        return withParam(s -> s.setString(parameterIndex, x));
    }

    public PreparedStatementBuilder withParam(Instant x) throws SQLException {
        return withParam(s -> s.setTimestamp(parameterIndex, Timestamp.from(x)));
    }

    public PreparedStatementBuilder withParam(LocalDateTime x) throws SQLException {
        return withParam(s -> s.setTimestamp(parameterIndex, Timestamp.valueOf(x)));
    }

    public PreparedStatementBuilder withParam(LocalDate x) throws SQLException {
        return withParam(s -> s.setDate(parameterIndex, Date.valueOf(x)));
    }

    public PreparedStatementBuilder withParam(LocalTime x) throws SQLException {
        return withParam(s -> s.setTime(parameterIndex, Time.valueOf(x)));
    }

    private PreparedStatementBuilder withParam(SQLProcedure<PreparedStatement> setter) throws SQLException {
        setter.call(statement);
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
