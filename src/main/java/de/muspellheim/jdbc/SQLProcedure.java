/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;

/**
 * Define a procedure or function without a return value, it can throw SQL exception.
 *
 * @param <T> the parameter type.
 */
@FunctionalInterface
public interface SQLProcedure<T> {

    void call(T t) throws SQLException;

}
