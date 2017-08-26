/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;

@FunctionalInterface
public interface SQLFunction<T, R> {

    R call(T t) throws SQLException;

}
