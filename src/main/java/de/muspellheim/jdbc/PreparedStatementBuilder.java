/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.io.*;
import java.sql.*;
import java.time.*;

/**
 * Build and execute a prepared statement.
 * <p>The <code>withParam</code> methods set statement parameters in order its appear in the SQL.</p>
 *
 * @see #executeUpdate()
 * @see #executeQuery()
 */
public class PreparedStatementBuilder {

    private final PreparedStatement statement;
    private int parameterIndex;

    /**
     * Initialize the builder.
     *
     * @param connection the connection used to create a statement.
     * @param sql        the SQL command for the statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder(Connection connection, String sql) throws SQLException {
        this.statement = connection.prepareStatement(sql);
        resetParameterIndex();
    }

    private void resetParameterIndex() {
        parameterIndex = 1;
    }

    /**
     * Set a byte parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(byte x) throws SQLException {
        return withParam(s -> s.setByte(parameterIndex, x));
    }

    /**
     * Set a short parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(short x) throws SQLException {
        return withParam(s -> s.setShort(parameterIndex, x));
    }

    /**
     * Set an int parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(int x) throws SQLException {
        return withParam(s -> s.setInt(parameterIndex, x));
    }

    /**
     * Set a long parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(long x) throws SQLException {
        return withParam(s -> s.setLong(parameterIndex, x));
    }

    /**
     * Set a float parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(float x) throws SQLException {
        return withParam(s -> s.setFloat(parameterIndex, x));
    }

    /**
     * Set a double parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(double x) throws SQLException {
        return withParam(s -> s.setDouble(parameterIndex, x));
    }

    /**
     * Set a boolean parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(boolean x) throws SQLException {
        return withParam(s -> s.setBoolean(parameterIndex, x));
    }

    /**
     * Set a string parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(String x) throws SQLException {
        return withParam(s -> s.setString(parameterIndex, x));
    }

    /**
     * Set a blob parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(InputStream x) throws SQLException {
        return withParam(s -> s.setBlob(parameterIndex, x));
    }

    /**
     * Set a clob parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(Reader x) throws SQLException {
        return withParam(s -> s.setClob(parameterIndex, x));
    }

    /**
     * Set an UTC time stamp parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(Instant x) throws SQLException {
        return withParam(s -> s.setTimestamp(parameterIndex, Timestamp.from(x)));
    }

    /**
     * Set a local date and time parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(LocalDateTime x) throws SQLException {
        return withParam(s -> s.setTimestamp(parameterIndex, Timestamp.valueOf(x)));
    }

    /**
     * Set a local date parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(LocalDate x) throws SQLException {
        return withParam(s -> s.setDate(parameterIndex, Date.valueOf(x)));
    }

    /**
     * Set a local time parameter.
     *
     * @param x the value
     * @return this statement.
     * @throws SQLException
     */
    public PreparedStatementBuilder withParam(LocalTime x) throws SQLException {
        return withParam(s -> s.setTime(parameterIndex, Time.valueOf(x)));
    }

    private PreparedStatementBuilder withParam(SQLProcedure<PreparedStatement> setter) throws SQLException {
        setter.call(statement);
        parameterIndex++;
        return this;
    }

    /**
     * Execute an update, usually an <code>INSERT</code>, <code>UPDATE</code> or <code>DELETE</code> command.
     *
     * @return the number of updated rows.
     * @throws SQLException
     */
    public int executeUpdate() throws SQLException {
        statement.execute();
        resetParameterIndex();
        return statement.getUpdateCount();
    }

    /**
     * Execute a query, every <code>SELECT</code> command.
     *
     * @return the queries result set.
     * @throws SQLException
     */
    public ExtendedResultSet executeQuery() throws SQLException {
        ExtendedResultSet resultSet = new ExtendedResultSet(statement.executeQuery());
        resetParameterIndex();
        return resultSet;
    }

}
