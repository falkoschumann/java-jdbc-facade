/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;

/**
 * Define a SQL query.
 *
 * @param <T> the return type of the query, usually an single data structure or list of data structures.
 */
@FunctionalInterface
public interface SQLQuery<T> {

    T execute(ConnectionBuilder connection) throws SQLException;

}
