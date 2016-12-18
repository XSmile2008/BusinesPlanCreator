package sample.controllers;

import com.google.gson.Gson;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import sample.Cache;
import sample.model.BusinessKind;
import sample.model.JsonTable;
import sample.widgets.WrapTableColumn;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML TableView<BusinessKind> tableViewNames;
    @FXML TableView<BusinessKind> tableViewData;

    private Gson gson = new Gson();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<BusinessKind, String> columnName = new TableColumn<>("Галузь");
        columnName.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().name));
        columnName.setMinWidth(240);

        tableViewNames.getColumns().add(columnName);
        tableViewNames.widthProperty().addListener((source, oldWidth, newWidth) -> {
            Pane header = (Pane) tableViewNames.lookup("TableHeaderRow");
            if (header.isVisible()){
                header.setMinHeight(78);
                header.setMaxHeight(78);
                header.setPrefHeight(78);
            }

            ScrollBar sb1 = (ScrollBar) tableViewNames.lookup(".scroll-bar:vertical");
            sb1.setMinWidth(0);//TODO:
            sb1.setPrefWidth(0);
            sb1.setMaxWidth(0);
            sb1.setOpacity(0);
            ScrollBar sb2 = (ScrollBar) tableViewData.lookup(".scroll-bar:vertical");
            sb1.valueProperty().bindBidirectional(sb2.valueProperty());

            final TableHeaderRow headerRow = (TableHeaderRow) tableViewData.lookup("TableHeaderRow");
            headerRow.reorderingProperty().addListener((o, oldVal, newVal) -> {
                if (newVal) {
                    headerRow.setReordering(false);
                }
            });
        });

        tableViewData.setColumnResizePolicy(param -> false);
        tableViewData.getColumns().addAll(
                buildSgCountColumn(),
                buildEmployersColumn(),
                buildOVPColumn(),
                buildORPColumn(),
                buildProfitabilityColumn()
        );

        tableViewNames.setItems(tableViewData.getItems());//Synchronize values
        tableViewNames.selectionModelProperty().bindBidirectional(tableViewData.selectionModelProperty());
    }

    @FXML
    private void onLoadDataClick(Event e) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Directory with datafiles");
        File file = chooser.showDialog(((Control) e.getSource()).getScene().getWindow());
        if (file.exists()) {
            File[] files = file.listFiles((dir, name) -> (name.toLowerCase().endsWith(".json")));
            loadDataFromFiles(files);
        }
    }

    private void loadDataFromFiles(File... files) {
        for (File f : files) {
            JsonTable table = null;
            try {
                Reader reader = new InputStreamReader(new FileInputStream(f), "windows-1251");
                table = gson.fromJson(reader, JsonTable.class);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (table != null) {
                Cache.getInstance().mapTable(table);
            }
        }
        tableViewData.getItems().clear();
        tableViewData.getItems().addAll(Cache.getInstance().businessKinds);
    }

    private TableColumn buildSgCountColumn() {
        TableColumn column = new TableColumn("Кількість субєктів господорювання");

        TableColumn<BusinessKind, Integer> columnSgSum = new TableColumn<>("Усього");
        columnSgSum.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().largeCompaniesCount
                        + param.getValue().middleCompaniesCount
                        + param.getValue().smallCompaniesCount
                        + param.getValue().privateCount
        ));

        TableColumn columnCompaniesCount = new TableColumn("Кількість підприємств");
        TableColumn<BusinessKind, Integer> columnCompaniesSum = new TableColumn<>("Усього");
        columnCompaniesSum.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().largeCompaniesCount
                        + param.getValue().middleCompaniesCount
                        + param.getValue().smallCompaniesCount
        ));
        TableColumn<BusinessKind, Integer> columnLargeCompanies = new TableColumn<>("Великі");
        columnLargeCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().largeCompaniesCount));
        TableColumn<BusinessKind, Integer> columnMiddleCompanies = new TableColumn<>("Середні");
        columnMiddleCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().middleCompaniesCount));
        TableColumn<BusinessKind, Integer> columnSmallCompanies = new TableColumn<>("Малі");
        columnSmallCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().smallCompaniesCount));
        columnCompaniesCount.getColumns().addAll(columnCompaniesSum, columnLargeCompanies, columnMiddleCompanies, columnSmallCompanies);

        TableColumn<BusinessKind, Integer> columnPrivate = new WrapTableColumn<>("Приватні підприємці");
        columnPrivate.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().privateCount));
        columnPrivate.setMinWidth(112);

        column.getColumns().addAll(columnSgSum, columnCompaniesCount, columnPrivate);

        return column;
    }

    private TableColumn buildEmployersColumn() {
        TableColumn column = new TableColumn("Кількість працівників");

        TableColumn<BusinessKind, Integer> columnSum = new TableColumn<>("Усього");
        columnSum.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().employers + param.getValue().privateEmployers
        ));
        TableColumn<BusinessKind, Integer> columnCompanies = new WrapTableColumn<>("Підприємства");
        columnCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().employers));
        TableColumn<BusinessKind, Integer> columnPrivate = new WrapTableColumn<>("Приватні підприємці");
        columnPrivate.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().privateEmployers));

        column.getColumns().addAll(columnSum, columnCompanies, columnPrivate);
        column.getColumns().stream().skip(1).forEach(o -> ((TableColumn) o).setMinWidth(112));

        return column;
    }

    private TableColumn buildOVPColumn() {
        TableColumn column = new TableColumn("Обсяг виробленої продукції");

        TableColumn<BusinessKind, Long> columnSum = new TableColumn<>("Усього");
        columnSum.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().largeCompaniesProducedGoods
                        + param.getValue().middleCompaniesProducedGoods
                        + param.getValue().smallCompaniesProducedGoods
        ));

        TableColumn<BusinessKind, Long> columnLargeCompanies = new WrapTableColumn<>("Великі підприємства");
        columnLargeCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().largeCompaniesProducedGoods));

        TableColumn<BusinessKind, Long> columnMiddleCompanies = new WrapTableColumn<>("Середні підприємства");
        columnMiddleCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().middleCompaniesProducedGoods));

        TableColumn<BusinessKind, Long> columnSmallCompanies = new WrapTableColumn<>("Малі підприємства");
        columnSmallCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().smallCompaniesProducedGoods));

        column.getColumns().addAll(columnSum, columnLargeCompanies, columnMiddleCompanies, columnSmallCompanies);
        column.getColumns().stream().skip(1).forEach(o -> ((TableColumn) o).setMinWidth(112));

        return column;
    }

    private TableColumn buildORPColumn() {
        TableColumn column = new TableColumn("Обсяг реалізованої продукції");

        TableColumn<BusinessKind, Long> columnSum = new TableColumn<>("Усього");
        columnSum.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().largeCompaniesSales
                        + param.getValue().middleCompaniesSales
                        + param.getValue().smallCompaniesSales
        ));

        TableColumn<BusinessKind, Long> columnLargeCompanies = new WrapTableColumn<>("Великі підприємства");
        columnLargeCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().largeCompaniesSales));

        TableColumn<BusinessKind, Long> columnMiddleCompanies = new WrapTableColumn<>("Середні підприємства");
        columnMiddleCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().middleCompaniesSales));

        TableColumn<BusinessKind, Long> columnSmallCompanies = new WrapTableColumn<>("Малі підприємства");
        columnSmallCompanies.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().smallCompaniesSales));

        column.getColumns().addAll(columnSum, columnLargeCompanies, columnMiddleCompanies, columnSmallCompanies);
        column.getColumns().stream().skip(1).forEach(o -> ((TableColumn) o).setMinWidth(112));

        return column;
    }

    private TableColumn buildProfitabilityColumn() {
        TableColumn column = new TableColumn("Рентабельність");

        TableColumn<BusinessKind, Long> columnOAR = new WrapTableColumn<>("Результат від операційної діяльності");
        columnOAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().operatingActivitiesResult));

        TableColumn<BusinessKind, Long> columnOC = new WrapTableColumn<>("Витрати операційної діяльності");
        columnOC.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().operatingCost));

        TableColumn<BusinessKind, Double> columnProfitability = new WrapTableColumn<>("Рівень рентабельності, %");
        columnProfitability.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().profitabilityLevel));

        column.getColumns().addAll(columnOAR, columnOC, columnProfitability);
        column.getColumns().forEach(o -> ((TableColumn) o).setMinWidth(160));

        return column;
    }
}