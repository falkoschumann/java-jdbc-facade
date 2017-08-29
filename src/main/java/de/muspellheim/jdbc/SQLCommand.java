/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;

/**
 * Define a SQL command.
 */
@FunctionalInterface
public interface SQLCommand {

    void execute(ConnectionBuilder connection) throws SQLException;

}
