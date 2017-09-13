/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.*;

import java.sql.*;

/**
 * Fill a table with an JDBC result set.
 * <p>The table columns named by the column labels of the result set.</p>
 */
public class JDBCTableViewBuilder {

    private final TableView<ObservableList<String>> tableView;

    /**
     * Initialize the builder.
     *
     * @param tableView the table view to fill.
     */
    public JDBCTableViewBuilder(TableView<ObservableList<String>> tableView) {
        this.tableView = tableView;
    }

    /**
     * Replace the table columns and rows with result set content.
     *
     * @param resultSet a result set.
     * @throws DataAccessException if a database access error occurs.
     */
    public void initWithResultSet(ResultSet resultSet) throws DataAccessException {
        try {
            removeRows();
            removeColumns();
            addColumns(resultSet);
            addRows(resultSet);
        } catch (SQLException ex) {
            throw new DataAccessException("Could not init table with database data.", ex);
        }
    }

    private void removeRows() {
        tableView.setItems(FXCollections.observableArrayList());
    }

    private void removeColumns() {
        tableView.getColumns().clear();
    }

    private void addColumns(ResultSet resultSet) throws SQLException {
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnLabel(i + 1));
            configureTableColumn(column, i);
            tableView.getColumns().addAll(column);
        }
    }

    /**
     * Overwrite to configure column with given index.
     * <p>The default implementation sets a simple string property as cell value factory.</p>
     *
     * @param column      the column to configure.
     * @param columnIndex the column index.
     */
    protected void configureTableColumn(TableColumn<ObservableList<String>, String> column, int columnIndex) {
        column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(columnIndex)));
    }

    private void addRows(ResultSet resultSet) throws SQLException {
        ObservableList<ObservableList<String>> rows = tableView.getItems();
        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
            rows.add(row);
        }
    }

}
