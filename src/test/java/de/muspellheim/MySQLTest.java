/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import com.mysql.jdbc.jdbc2.optional.*;
import org.junit.*;

public class MySQLTest {

    private JDBCFacade jdbc;

    @Before
    public void createSchema() {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(8889);
        dataSource.setUser("root");
        dataSource.setPassword("root");

        jdbc = new JDBCFacade(dataSource);
        jdbc.executeDDLCommand(connection -> connection.statement("DROP SCHEMA IF EXISTS oshop").execute());
        jdbc.executeDDLCommand(connection -> connection.statement("CREATE SCHEMA oshop CHARACTER SET utf8 COLLATE utf8_unicode_ci").execute());

        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(8889);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("oshop");
        jdbc = new JDBCFacade(dataSource);
    }

    @Test
    public void createAndInitializeDatabase() {
        createTablesKundenverwaltung();
        createTablesArtikelverwaltung();
    }

    public void createTablesKundenverwaltung() {
        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE adresse ("
                        + "adresse_id INT UNSIGNED AUTO_INCREMENT,"
                        + "strasse VARCHAR(255) NOT NULL DEFAULT '',"
                        + "hnr VARCHAR(255) NOT NULL DEFAULT '',"
                        + "lkz CHAR(2) NOT NULL DEFAULT '',"
                        + "plz CHAR(9) NOT NULL DEFAULT '',"
                        + "ort VARCHAR(255) NOT NULL DEFAULT '',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(adresse_id)"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE kunde ("
                        + "kunde_id INT UNSIGNED AUTO_INCREMENT,"
                        + "nachname VARCHAR(255) NOT NULL DEFAULT '',"
                        + "vorname VARCHAR(255) NOT NULL DEFAULT '',"
                        + "rechnung_adresse_id INT UNSIGNED NOT NULL,"
                        + "liefer_adresse_id INT UNSIGNED,"
                        + "bezahlart INT UNSIGNED NOT NULL DEFAULT 0,"
                        + "art ENUM('unb', 'prv', 'gsch') NOT NULL DEFAULT 'unb',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(kunde_id),"
                        + "FOREIGN KEY (rechnung_adresse_id)"
                        + "  REFERENCES adresse(adresse_id)"
                        + "  ON UPDATE CASCADE"
                        + "  ON DELETE RESTRICT,"
                        + "FOREIGN KEY (liefer_adresse_id)"
                        + "REFERENCES adresse(adresse_id)"
                        + "  ON UPDATE CASCADE"
                        + "  ON DELETE SET NULL"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE bank ("
                        + "bank_id CHAR(12),"
                        + "bankname VARCHAR(255) NOT NULL DEFAULT '',"
                        + "lkz CHAR(2) NOT NULL DEFAULT '',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(bank_id)"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE bankverbindung ("
                        + "kunde_id INT UNSIGNED,"
                        + "bankverbindung_nr INT UNSIGNED,"
                        + "bank_id CHAR(12) NOT NULL,"
                        + "kontonummer CHAR(25) NOT NULL DEFAULT '',"
                        + "iban CHAR(34) NOT NULL DEFAULT '',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(kunde_id, bankverbindung_nr),"
                        + "FOREIGN KEY (kunde_id)"
                        + "  REFERENCES kunde(kunde_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT,"
                        + "FOREIGN KEY (bank_id)"
                        + "  REFERENCES bank(bank_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT"
                        + ")"
        ).execute());
    }

    public void createTablesArtikelverwaltung() {
        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE artikel ("
                        + "artikel_id INT UNSIGNED AUTO_INCREMENT,"
                        + "bezeichnung VARCHAR(255) NOT NULL DEFAULT '',"
                        + "einzelpreis DECIMAL NOT NULL DEFAULT 0,"
                        + "waehrung CHAR(3) NOT NULL DEFAULT '',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(artikel_id)"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE warengruppe ("
                        + "warengruppe_id INT UNSIGNED AUTO_INCREMENT,"
                        + "bezeichnung VARCHAR(255) NOT NULL DEFAULT '',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(warengruppe_id)"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE artikel_nm_warengruppe ("
                        + "artikel_id INT UNSIGNED,"
                        + "warengruppe_id INT UNSIGNED,"
                        + "PRIMARY KEY(artikel_id, warengruppe_id),"
                        + "FOREIGN KEY (artikel_id)"
                        + "  REFERENCES artikel(artikel_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT,"
                        + "FOREIGN KEY (warengruppe_id)"
                        + "  REFERENCES warengruppe(warengruppe_id)"
                        + "  ON UPDATE CASCADE"
                        + "  ON DELETE RESTRICT"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE lieferant ("
                        + "lieferant_id INT UNSIGNED AUTO_INCREMENT,"
                        + "nachname VARCHAR(255) NOT NULL DEFAULT '',"
                        + "vorname VARCHAR(255) NOT NULL DEFAULT '',"
                        + "firmenname VARCHAR(255) NOT NULL DEFAULT '',"
                        + "adresse_id INT UNSIGNED NOT NULL,"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(lieferant_id),"
                        + "FOREIGN KEY (adresse_id)"
                        + "REFERENCES adresse(adresse_id)"
                        + "  ON UPDATE CASCADE"
                        + "  ON DELETE RESTRICT"
                        + ")"
        ).execute());

        jdbc.executeDDLCommand(connection -> connection.statement(
                "CREATE TABLE artikel_nm_lieferant ("
                        + "artikel_id INT UNSIGNED,"
                        + "lieferant_id INT UNSIGNED,"
                        + "PRIMARY KEY(artikel_id, lieferant_id),"
                        + "FOREIGN KEY (artikel_id)"
                        + "  REFERENCES artikel(artikel_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT,"
                        + "FOREIGN KEY (lieferant_id)"
                        + "  REFERENCES lieferant(lieferant_id)"
                        + "  ON UPDATE CASCADE"
                        + "  ON DELETE RESTRICT"
                        + ")"
        ).execute());
    }

}
