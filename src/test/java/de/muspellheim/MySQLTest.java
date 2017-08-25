/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim;

import com.mysql.jdbc.jdbc2.optional.*;
import org.junit.*;
import org.junit.runners.*;

import java.util.*;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MySQLTest {

    private JDBCFacade jdbc;

    @BeforeClass
    public static void createSchema() {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(getPort());
        dataSource.setUser(getUser());
        dataSource.setPassword(getPassword());

        JDBCFacade jdbc = new JDBCFacade(dataSource);
        jdbc.executeSQLCommand(connection -> connection.statement("DROP SCHEMA IF EXISTS oshop").execute());
        jdbc.executeSQLCommand(connection -> connection.statement("CREATE SCHEMA oshop CHARACTER SET utf8 COLLATE utf8_unicode_ci").execute());

        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(getPort());
        dataSource.setUser(getUser());
        dataSource.setPassword(getPassword());
        dataSource.setDatabaseName("oshop");

        jdbc = new JDBCFacade(dataSource);
        createTablesKundenverwaltung(jdbc);
        createTablesArtikelverwaltung(jdbc);
        createTablesBestellwesen(jdbc);
    }

    private static int getPort() {
        String port = System.getenv("MYSQL_PORT");
        return port != null ? Integer.parseInt(port) : 3306;
    }

    private static String getUser() {
        String user = System.getenv("MYSQL_USER");
        return user != null ? user : "root";
    }

    private static String getPassword() {
        String password = System.getenv("MYSQL_PASSWORD");
        return password != null ? password : "";
    }

    private static void createTablesKundenverwaltung(JDBCFacade jdbc) {
        jdbc.executeSQLCommand(connection -> connection.statement(
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
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_adresse_dublette ON adresse(strasse(100), hnr(100), plz)"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
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
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_kunde_nachname_vorname ON kunde(nachname, vorname)"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE TABLE bank ("
                        + "bank_id CHAR(12),"
                        + "bankname VARCHAR(255) NOT NULL DEFAULT '',"
                        + "lkz CHAR(2) NOT NULL DEFAULT '',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(bank_id)"
                        + ")"
        ).execute());
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_bank_bankname ON bank(bankname)"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
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
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_bankverbindung_bankid_kontonummer ON bankverbindung(bank_id, kontonummer)"
        ).execute());
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE UNIQUE INDEX idx_bankverbindung_iban ON bankverbindung(iban)"
        ).execute());
    }

    private static void createTablesArtikelverwaltung(JDBCFacade jdbc) {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE TABLE artikel ("
                        + "artikel_id INT UNSIGNED AUTO_INCREMENT,"
                        + "bezeichnung VARCHAR(255) NOT NULL DEFAULT '',"
                        + "einzelpreis DECIMAL(14,6) NOT NULL DEFAULT 0.0,"
                        + "waehrung CHAR(3) NOT NULL DEFAULT 'EUR',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(artikel_id)"
                        + ")"
        ).execute());
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_artikel_bezeichnung ON artikel(bezeichnung)"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE TABLE warengruppe ("
                        + "warengruppe_id INT UNSIGNED AUTO_INCREMENT,"
                        + "bezeichnung VARCHAR(255) NOT NULL DEFAULT '',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(warengruppe_id)"
                        + ")"
        ).execute());
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_warengruppe_bezeichnung ON warengruppe(bezeichnung)"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
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

        jdbc.executeSQLCommand(connection -> connection.statement(
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
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_lieferant_firmenname ON lieferant(firmenname)"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
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

    private static void createTablesBestellwesen(JDBCFacade jdbc) {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE TABLE bestellung ("
                        + "bestellung_id INT UNSIGNED AUTO_INCREMENT,"
                        + "kunde_id INT UNSIGNED NULL DEFAULT 0,"
                        + "adresse_id INT UNSIGNED NULL DEFAULT 0,"
                        + "datum DATETIME NOT NULL DEFAULT 0,"
                        + "status ENUM('offen', 'versendet', 'angekommen', 'retour', 'bezahlt') NOT NULL DEFAULT 'offen',"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(bestellung_id),"
                        + "FOREIGN KEY (kunde_id)"
                        + "  REFERENCES kunde(kunde_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT,"
                        + "FOREIGN KEY (adresse_id)"
                        + "  REFERENCES adresse(adresse_id)"
                        + "  ON UPDATE CASCADE"
                        + "  ON DELETE RESTRICT"
                        + ")"
        ).execute());
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_bestellung_kundeid_datum ON bestellung(kunde_id, datum)"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE TABLE bestellung_position ("
                        + "bestellung_id INT UNSIGNED,"
                        + "position_nr INT UNSIGNED,"
                        + "artikel_id INT UNSIGNED DEFAULT 0,"
                        + "menge DECIMAL(14,6) NOT NULL DEFAULT 0.0,"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(bestellung_id, position_nr),"
                        + "FOREIGN KEY (bestellung_id)"
                        + "  REFERENCES bestellung(bestellung_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT,"
                        + "FOREIGN KEY (artikel_id)"
                        + "  REFERENCES artikel(artikel_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT"
                        + ")"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE TABLE rechnung ("
                        + "rechnung_id INT UNSIGNED AUTO_INCREMENT,"
                        + "kunde_id INT UNSIGNED NULL DEFAULT 0,"
                        + "adresse_id INT UNSIGNED NULL DEFAULT 0,"
                        + "bezahlart ENUM('lastschrift', 'rechnung', 'kredit', 'bar') NOT NULL DEFAULT 'lastschrift',"
                        + "datum DATETIME NOT NULL DEFAULT 0,"
                        + "status ENUM('offen', 'versendet', 'angekommen', 'retour', 'bezahlt') NOT NULL DEFAULT 'offen',"
                        + "rabatt DECIMAL(6,2) NOT NULL DEFAULT 0.0,"
                        + "skonto DECIMAL(6,2) NOT NULL DEFAULT 0.0,"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(rechnung_id),"
                        + "FOREIGN KEY (kunde_id)"
                        + "  REFERENCES kunde(kunde_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT,"
                        + "FOREIGN KEY (adresse_id)"
                        + "  REFERENCES adresse(adresse_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT"
                        + ")"
        ).execute());
        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE INDEX idx_rechnung_kundeid_datum ON rechnung(kunde_id, datum)"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
                "CREATE TABLE rechnung_position ("
                        + "rechnung_id INT UNSIGNED,"
                        + "position_nr INT UNSIGNED,"
                        + "artikel_id INT UNSIGNED DEFAULT 0,"
                        + "menge DECIMAL(14,6) NOT NULL DEFAULT 0.0,"
                        + "rabatt DECIMAL(6,2) NOT NULL DEFAULT 0.0,"
                        + "deleted TINYINT UNSIGNED NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(rechnung_id, position_nr),"
                        + "FOREIGN KEY (rechnung_id)"
                        + "  REFERENCES rechnung(rechnung_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT,"
                        + "FOREIGN KEY (artikel_id)"
                        + "  REFERENCES artikel(artikel_id)"
                        + "  ON UPDATE RESTRICT"
                        + "  ON DELETE RESTRICT"
                        + ")"
        ).execute());
    }

    @Before
    public void connectWithDatabase() {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(getPort());
        dataSource.setUser(getUser());
        dataSource.setPassword(getPassword());
        dataSource.setDatabaseName("oshop");
        this.jdbc = new JDBCFacade(dataSource);
    }

    @Test
    public void test01ImportArtikel() {
        importArtikel01();
        importArtikel02();
    }

    private void importArtikel01() {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "LOAD DATA LOCAL INFILE 'src/example/data/artikel01.csv' "
                        + "INTO TABLE artikel "
                        + "FIELDS TERMINATED BY ';' "
                        + "LINES TERMINATED BY '\n' "
                        + "IGNORE 1 LINES "
                        + "(artikel_id, bezeichnung, einzelpreis, waehrung) "
                        + "SET deleted=0"
        ).execute());
    }

    private void importArtikel02() {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "LOAD DATA LOCAL INFILE 'src/example/data/artikel02.csv' REPLACE "
                        + "INTO TABLE artikel "
                        + "FIELDS TERMINATED BY ';' "
                        + "LINES TERMINATED BY '\n' "
                        + "IGNORE 1 LINES "
                        + "(artikel_id, bezeichnung, einzelpreis, waehrung) "
                        + "SET deleted=0"
        ).execute());
    }

    @Test
    public void test02InsertWarengruppen() {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "INSERT INTO warengruppe (bezeichnung, deleted) "
                        + "VALUES "
                        + "('Bürobedarf', 0),"
                        + "('Pflanzen', 0),"
                        + "('Gartenbedarf', 0),"
                        + "('Werkzeug', 0)"
        ).execute());
    }

    @Test(expected = UncheckedSQLException.class)
    public void test03InsertWarengruppen() {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "INSERT INTO warengruppe (warengruppe_id, bezeichnung) "
                        + "VALUES "
                        + "(1, 'Bürobedarf'),"
                        + "(2, 'Pflanzen'),"
                        + "(3, 'Gartenbedarf'),"
                        + "(4, 'Werkzeug')"
        ).execute());
    }

    @Test
    public void test04InsertArtikelWarengruppen() {
        jdbc.executeSQLCommand(connection -> {
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=3001,"
                            + "warengruppe_id=1"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=3005,"
                            + "warengruppe_id=1"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=3006,"
                            + "warengruppe_id=1"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=3007,"
                            + "warengruppe_id=1"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=3010,"
                            + "warengruppe_id=1"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=7856,"
                            + "warengruppe_id=2"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=7856,"
                            + "warengruppe_id=3"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=7863,"
                            + "warengruppe_id=2"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=7863,"
                            + "warengruppe_id=3"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=9010,"
                            + "warengruppe_id=3"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=9010,"
                            + "warengruppe_id=4"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=9015,"
                            + "warengruppe_id=3"
            ).execute();
            connection.statement(
                    "INSERT INTO artikel_nm_warengruppe "
                            + "SET "
                            + "artikel_id=9015,"
                            + "warengruppe_id=4"
            ).execute();
        });
    }

    @Test
    public void test05AlterTableKundeAlterColumnBezahlart() {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "ALTER TABLE kunde "
                        + "MODIFY "
                        + "bezahlart ENUM('rechnung', 'bankeinzug', 'nachname') DEFAULT 'rechnung'"
        ).execute());
    }

    @Test
    public void test06InsertAdressen() {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "INSERT INTO adresse (adresse_id, strasse, hnr, lkz, plz, ort) " +
                        "VALUES " +
                        "(1, 'Beutelhaldenweg', '5', 'AL', '67676', 'Hobbingen')," +
                        "(2, 'Beutelhaldenweg', '1', 'AL', '67676', 'Hobbingen')," +
                        "(3, 'Auf der Feste',   '1', 'GO', '54786', 'Minas Tirith')," +
                        "(4, 'Letztes Haus',    '4', 'ER', '87567', 'Bruchtal')," +
                        "(5, 'Baradur',         '1', 'MO', '62519', 'Lugburz')," +
                        "(10, 'Hochstrasse',    '4a','DE', '44879', 'Bochum')," +
                        "(11, 'Industriegebiet','8', 'DE', '44878', 'Bochum')"
        ).execute());

        jdbc.executeSQLCommand(connection -> connection.statement(
                "INSERT INTO kunde (kunde_id, rechnung_adresse_id, nachname, vorname, art) " +
                        "VALUES " +
                        "(1, 1, 'Gamdschie',   'Samweis', 'prv')," +
                        "(2, 2, 'Beutlin',     'Frodo',   'prv')," +
                        "(3, 2, 'Beutlin',     'Bilbo',   'prv')," +
                        "(4, 3, 'Telcontar',   'Elessar', 'prv')," +
                        "(5, 4, 'Earendilionn','Elrond',  'gsch')"
        ).execute());

        jdbc.executeSQLCommand(connection -> {
            connection.statement(
                    "UPDATE kunde SET liefer_adresse_id = 2 WHERE kunde_id = 1"
            ).execute();
            connection.statement(
                    "UPDATE kunde SET liefer_adresse_id = 4 WHERE kunde_id = 3;"
            ).execute();
        });
    }

    @Test
    public void test07InsertBestellungen() {
        jdbc.executeSQLCommand(connection -> connection.statement(
                "INSERT INTO bestellung (kunde_id, adresse_id, datum) " +
                        "VALUES " +
                        "(1, 1, '2012-03-24 17:41:00')," +
                        "(2, 2, '2012-03-23 16:11:00')"
        ).execute());

        int updateCount = jdbc.executeSQLQuery(connection -> connection.preparedStatement(
                "INSERT INTO bestellung_position (bestellung_id, position_nr, artikel_id, menge) " +
                        "VALUES " +
                        "(1, 1, 7856, 30)," +
                        "(1, 2, 7863, 50)," +
                        "(1, 3, 9015, 1)," +
                        "(2, 1, 7856, 10)," +
                        "(2, 2, 9010, 5)"
        ).execute());

        assertEquals(5, updateCount);
    }
/*
    @Test
    public void test08SelectKunde() {
        List result = jdbc.executeSQLQuery(connection -> connection.preparedStatement(
                "SELECT * FROM artikel "
                        + "WHERE einzelpreis BETWEEN ? AND ?"
        )
                .withParam(1.00)
                .withParam(15.00)
                .executeQuery());

        assertEquals(6, result.size());
    }
*/
}
