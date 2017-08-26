/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import com.mysql.jdbc.jdbc2.optional.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.util.*;

import java.sql.*;

public class FXApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        TableView tableView = new TableView();

        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPort(8889);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("oshop");

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT artikel_id AS 'Artikel-ID', bezeichnung AS 'Bezeichnung', CONCAT(FORMAT(einzelpreis, 2, 'de_DE'), ' ', waehrung) AS 'Einzelpreis' FROM artikel WHERE deleted = 0";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(resultSet.getMetaData().getColumnLabel(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                tableView.getColumns().addAll(col);
            }

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                data.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tableView.setItems(data);

        Scene scene = new Scene(tableView);

        stage.setTitle("Demo with table kunde");
        stage.setScene(scene);
        stage.show();
    }

}
