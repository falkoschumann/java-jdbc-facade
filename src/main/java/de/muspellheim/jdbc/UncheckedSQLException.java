/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;
import java.util.*;

public class UncheckedSQLException extends RuntimeException implements Iterable<Throwable> {

    public UncheckedSQLException(SQLException cause) {
        super(cause);
    }

    public UncheckedSQLException(String message, SQLException cause) {
        super(message, cause);
    }

    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    public String getSQLState() {
        return getSQLException().getSQLState();
    }

    public int getErrorCode() {
        return getSQLException().getErrorCode();
    }

    public SQLException getNextException() {
        return getSQLException().getNextException();
    }

    public Iterator<Throwable> iterator() {
        return getSQLException().iterator();
    }

}
