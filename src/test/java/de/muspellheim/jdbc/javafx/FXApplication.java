/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc.javafx;

import com.mysql.jdbc.jdbc2.optional.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;

import java.sql.*;

public class FXApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        JDBCTableView tableView = new JDBCTableView();

        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(8889);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("oshop");

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT artikel_id AS 'Artikel-ID', bezeichnung AS 'Bezeichnung', CONCAT(FORMAT(einzelpreis, 2, 'de_DE'), ' ', waehrung) AS 'Einzelpreis' FROM artikel WHERE deleted = 0";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            tableView.initWithResultSet(resultSet);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Scene scene = new Scene(tableView);

        stage.setTitle("Demo with table kunde");
        stage.setScene(scene);
        stage.show();
    }

}
