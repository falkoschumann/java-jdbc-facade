/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.sql.*;

public class ArtikelMapper {

    public static Artikel map(ResultSetWrapper resultSet) throws SQLException {
        Artikel artikel = new Artikel();
        artikel.setId(resultSet.getInt("artikel_id"));
        artikel.setBezeichnung(resultSet.getString("bezeichnung"));
        artikel.setEinzelpreis(Money.of(resultSet.getDouble("einzelpreis"), resultSet.getString("waehrung")));
        artikel.setDeleted(resultSet.getBoolean("deleted"));
        return artikel;
    }

}
