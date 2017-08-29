/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;

/**
 * Define a function, it can throw SQL exception.
 *
 * @param <T> the parameter type.
 * @param <R> the return type.
 */
@FunctionalInterface
public interface SQLFunction<T, R> {

    R call(T t) throws SQLException;

}
