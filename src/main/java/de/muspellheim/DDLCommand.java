/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import java.sql.*;

public interface DDLCommand {

    void execute(ConnectionWrapper connection) throws SQLException;

}
