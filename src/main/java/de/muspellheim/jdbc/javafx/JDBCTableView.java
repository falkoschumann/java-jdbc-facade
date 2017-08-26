/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc.javafx;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.*;

import java.sql.*;

public class JDBCTableView extends TableView<ObservableList<String>> {

    public void initWithResultSet(ResultSet resultSet) throws SQLException {
        setItems(FXCollections.observableArrayList());
        getColumns().clear();
        addColumns(resultSet);
        addRows(resultSet);
    }

    private void addColumns(ResultSet resultSet) throws SQLException {
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(resultSet.getMetaData().getColumnLabel(i + 1));
            col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
            getColumns().addAll(col);
        }
    }

    private void addRows(ResultSet resultSet) throws SQLException {
        ObservableList<ObservableList<String>> rows = getItems();
        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
            rows.add(row);
        }
    }

}
