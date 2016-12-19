package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by vladstarikov on 12/19/16.
 */
public class FormController implements Initializable {

    @FXML TableView tableConstantCosts;
    @FXML TableView tableVariableCosts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initConstantCosts();
        initVariableCosts();
    }

    private void initConstantCosts() {
        TableColumn columnName = new TableColumn("Стаття витрат");
        columnName.setPrefWidth(640);
        TableColumn columnValue = new TableColumn("Сума");
        columnValue.setPrefWidth(240);

        tableConstantCosts.getColumns().addAll(columnName, columnValue);
    }

    private void initVariableCosts() {
        TableColumn columnName = new TableColumn("Стаття витрат");
        columnName.setPrefWidth(640);
        TableColumn columnValue = new TableColumn("Сума");
        columnValue.setPrefWidth(240);

        tableVariableCosts.getColumns().addAll(columnName, columnValue);
    }
}