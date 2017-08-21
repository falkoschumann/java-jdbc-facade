/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import com.mysql.jdbc.jdbc2.optional.*;
import org.junit.*;

public class MySQLTest {

    @Before
    public void createSchema() throws Exception {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(8889);
        dataSource.setUser("root");
        dataSource.setPassword("root");

        JDBCFacade jdbc = new JDBCFacade(dataSource);
        jdbc.executeDDLCommand(connection -> connection.statement("DROP SCHEMA IF EXISTS oshop").execute());
        jdbc.executeDDLCommand(connection -> connection.statement("CREATE SCHEMA oshop CHARACTER SET utf8 COLLATE utf8_unicode_ci").execute());
    }

    @Test
    public void createTablesKundenverwaltung() throws Exception {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(8889);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("oshop");

        JDBCFacade jdbc = new JDBCFacade(dataSource);

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE adresse ("
                        + "adresse_id INT UNSIGNED AUTO_INCREMENT,"
                        + "strasse VARCHAR(255),"
                        + "hnr VARCHAR(255),"
                        + "lkz CHAR(2),"
                        + "plz CHAR(9),"
                        + "ort VARCHAR(255),"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(adresse_id)"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE kunde ("
                        + "kunde_id INT UNSIGNED AUTO_INCREMENT,"
                        + "nachname VARCHAR(255),"
                        + "vorname VARCHAR(255),"
                        + "rechnung_adresse_id INT UNSIGNED,"
                        + "liefer_adresse_id INT UNSIGNED,"
                        + "bezahlart INT UNSIGNED NOT NULL DEFAULT 0,"
                        + "art INT UNSIGNED NOT NULL DEFAULT 0,"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(kunde_id)"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE bank ("
                        + "bank_id CHAR(12),"
                        + "bankname VARCHAR(255),"
                        + "lkz CHAR(2),"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(bank_id)"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE bankverbindung ("
                        + "kunde_id INT UNSIGNED,"
                        + "bankverbindung_nr INT UNSIGNED,"
                        + "bank_id CHAR(12),"
                        + "kontonummer CHAR(25),"
                        + "iban CHAR(34),"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(kunde_id, bankverbindung_nr)"
                        + ")"
        ).execute());
    }

}
