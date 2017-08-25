/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import java.sql.*;

public interface SQLQuery<T> {

    T execute(ConnectionBuilder connection) throws SQLException;

}
