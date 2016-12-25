package sample.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import sample.Cache;
import sample.Exporter;
import sample.model.BusinessKind;
import sample.model.BusinessPlan;
import sample.model.Cost;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by vladstarikov on 12/19/16.
 */
public class FormController implements Initializable {

    @FXML TextField fName;
    @FXML TextField fCurrency;
    @FXML TextField fUnitOfMeasurement;
    @FXML TextField fOnePeacePrice;
    @FXML TextField fMonthlySales;

    @FXML TableView<Cost> tableConstantCosts;
    @FXML Button btnAddConstantCost;
    @FXML Button btnRemoveConstantCost;

    @FXML TableView<Cost> tableVariableCosts;
    @FXML Button btnAddVariableCost;
    @FXML Button btnRemoveVariableCost;

    private BusinessPlan businessPlan = new BusinessPlan();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFields();
        tableConstantCosts.getColumns().addAll(buildColumns());
        tableVariableCosts.getColumns().addAll(buildColumns());
    }

    private void initFields() {
        fName.textProperty().addListener((observable, oldValue, newValue) -> businessPlan.businessName = newValue);
        fCurrency.textProperty().addListener((observable, oldValue, newValue) -> businessPlan.currency = newValue);
        fUnitOfMeasurement.textProperty().addListener((observable, oldValue, newValue) -> businessPlan.unitOfMeasurement = newValue);
        fOnePeacePrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    businessPlan.onePeacePrice = Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    fOnePeacePrice.setText(oldValue);
                }
            }
        });
        fMonthlySales.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    businessPlan.monthlySales = Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    fMonthlySales.setText(oldValue);
                }
            }
        });
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

    @FXML
    private void export(Event e) {
        if (fName.getText().isEmpty()
                || fCurrency.getText().isEmpty()
                || fUnitOfMeasurement.getText().isEmpty()
                || fOnePeacePrice.getText().isEmpty()
                || fMonthlySales.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Будьласка заповніть необхідні поля", ButtonType.CLOSE).show();
        } else {
            Window window = ((Control) e.getSource()).getScene().getWindow();

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Вкажіть щлях до шаблону");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xls"));
            File fileTemplate = chooser.showOpenDialog(window);
            if (fileTemplate != null && fileTemplate.exists()) {
                chooser.setTitle("Вкажіть куди зберегти файл");
                chooser.setInitialFileName("business_plan.xls");
                File fileNew = chooser.showSaveDialog(window);

                if (fileNew != null) {
                    businessPlan.constantCosts = tableConstantCosts.getItems();
                    businessPlan.variableCosts = tableVariableCosts.getItems();

                    Cache cache = Cache.getInstance();
                    BusinessKind businessKind = cache.businessKinds.get(cache.selectedBusinessKind);
                    Exporter.saveTemplate(businessPlan, businessKind, fileTemplate.getPath(), fileNew.getPath());
                }
            }
        }
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

        TableColumn<Cost, Integer> columnValue = new TableColumn<>("Сума");
        columnValue.setPrefWidth(240);
        columnValue.setEditable(true);
        columnValue.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnValue.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().value));
        columnValue.setOnEditCommit(event -> {
            int row = event.getTablePosition().getRow();
            event.getTableView().getItems().get(row).value = event.getNewValue();
        });

        return Arrays.asList(columnName, columnValue);
    }
}