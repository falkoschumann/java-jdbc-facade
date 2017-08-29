/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import com.mysql.jdbc.jdbc2.optional.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.sql.*;

public class FXExampleApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(8889);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("oshop");

        TableView tableView = new TableView();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT " +
                    "    artikel_id AS 'Artikel-ID'," +
                    "    bezeichnung AS 'Bezeichnung'," +
                    "    CONCAT(FORMAT(einzelpreis, 2, 'de_DE'), ' ', waehrung) AS 'Einzelpreis' " +
                    "FROM artikel " +
                    "WHERE deleted = 0";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            new JDBCTableViewBuilder(tableView).initWithResultSet(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Scene scene = new Scene(tableView);

        stage.setTitle("Demo with table kunde");
        stage.setWidth(400);
        stage.setScene(scene);
        stage.show();
    }

}
