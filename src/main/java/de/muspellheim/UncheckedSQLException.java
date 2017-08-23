/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

public class UncheckedSQLException extends RuntimeException {

    public UncheckedSQLException(String message, Throwable cause) {
        super(message, cause);
    }

}
