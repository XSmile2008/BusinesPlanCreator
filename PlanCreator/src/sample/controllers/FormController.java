package sample.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import sample.model.Cost;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by vladstarikov on 12/19/16.
 */
public class FormController implements Initializable {

    @FXML TableView<Cost> tableConstantCosts;
    @FXML Button btnAddConstantCost;
    @FXML Button btnRemoveConstantCost;

    @FXML TableView<Cost> tableVariableCosts;
    @FXML Button btnAddVariableCost;
    @FXML Button btnRemoveVariableCost;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableConstantCosts.getColumns().addAll(buildColumns());
        tableVariableCosts.getColumns().addAll(buildColumns());
    }

    private List<TableColumn<Cost, ?>> buildColumns() {
        TableColumn<Cost, String> columnName = new TableColumn<>("Стаття витрат");
        columnName.setPrefWidth(640);
        columnName.setEditable(true);
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnName.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().name));
        columnName.setOnEditCommit(event -> {
            int row = event.getTablePosition().getRow();
            event.getTableView().getItems().get(row).name = event.getNewValue();
        });

        TableColumn<Cost, Double> columnValue = new TableColumn<>("Сума");
        columnValue.setPrefWidth(240);
        columnValue.setEditable(true);
        columnValue.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        columnValue.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().value));
        columnValue.setOnEditCommit(event -> {
            int row = event.getTablePosition().getRow();
            event.getTableView().getItems().get(row).value = event.getNewValue();
        });

        return Arrays.asList(columnName, columnValue);
    }

    @FXML
    private void onClick(Event e) {
        if (e.getSource().equals(btnAddConstantCost)) {
            tableConstantCosts.getItems().add(new Cost());
        } else if (e.getSource().equals(btnRemoveConstantCost)) {
            int index = tableConstantCosts.getSelectionModel().getSelectedIndex();
            tableConstantCosts.getItems().remove(index);
        } else if (e.getSource().equals(btnAddVariableCost)) {
            tableVariableCosts.getItems().add(new Cost());
        } else if (e.getSource().equals(btnRemoveVariableCost)) {
            int index = tableConstantCosts.getSelectionModel().getSelectedIndex();
            tableVariableCosts.getItems().remove(index);
        }
    }
}