/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;
import java.util.*;

public class ORMapper {

    // TODO How to handle null value?

    // TODO Hold map of type and adapter
    // TODO Add custom adapter

    // TODO byte adapter
    // TODO short adapter
    // TODO long adapter
    // TODO float adapter
    // TODO double adapter
    // TODO Instant adapter
    // TODO LocalDateTime adapter
    // TODO LocalDate adapter
    // TODO LocalTime adapter

    private final Map<Class, JDBCAdapter> adapters = new HashMap<>();

    public ORMapper() {
        setAdapter(Integer.class, new IntegerAdapter());
        setAdapter(Boolean.class, new BooleanAdapter());
        setAdapter(String.class, new StringAdapter());
    }

    public <A> JDBCAdapter<A> getAdapter(Class<A> type) {
        return adapters.get(type);
    }

    public <A> void setAdapter(Class<A> type, JDBCAdapter<A> adapter) {
        adapters.put(type, adapter);
    }

    public ResultSet marshal(Object object) {
        return null;
    }

    public <T> T unmarshal(ResultSet resultSet, Class<T> type) {
        return null;
    }

    public interface JDBCAdapter<T> {

        void marshal(T value, ResultSet resultSet, String columnLabel) throws SQLException;

        T unmarshal(ResultSet resultSet, String columnLabel) throws SQLException;

    }

    private static class IntegerAdapter implements JDBCAdapter<Integer> {

        @Override
        public void marshal(Integer value, ResultSet resultSet, String columnLabel) throws SQLException {
            resultSet.updateInt(columnLabel, value);
        }

        @Override
        public Integer unmarshal(ResultSet resultSet, String columnLabel) throws SQLException {
            return resultSet.getInt(columnLabel);
        }

    }

    private static class BooleanAdapter implements JDBCAdapter<Boolean> {

        @Override
        public void marshal(Boolean value, ResultSet resultSet, String columnLabel) throws SQLException {
            resultSet.updateBoolean(columnLabel, value);
        }

        @Override
        public Boolean unmarshal(ResultSet resultSet, String columnLabel) throws SQLException {
            return resultSet.getBoolean(columnLabel);
        }

    }

    private static class StringAdapter implements JDBCAdapter<String> {

        @Override
        public void marshal(String value, ResultSet resultSet, String columnLabel) throws SQLException {
            resultSet.updateString(columnLabel, value);
        }

        @Override
        public String unmarshal(ResultSet resultSet, String columnLabel) throws SQLException {
            return resultSet.getString(columnLabel);
        }

    }

}
