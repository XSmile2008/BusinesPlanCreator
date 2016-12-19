package sample.widgets;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.text.TextAlignment;

/**
 * Created by vladstarikov on 12/18/16.
 */
public class WrapTableColumn<S, T> extends TableColumn<S, T> {

    @SuppressWarnings("unused")
    public WrapTableColumn() {
        super();
        buildGraphic();
    }

    public WrapTableColumn(String text) {
        super(text);
        buildGraphic();
    }

    private void buildGraphic() {
        Label columnTitle = new Label(getText());
        columnTitle.setPrefWidth(getWidth());
        columnTitle.setPrefHeight(50);
        columnTitle.setWrapText(true);
        columnTitle.setPadding(new Insets(2, 2, 2, 2));
        columnTitle.setTextAlignment(TextAlignment.CENTER);

        textProperty().bindBidirectional(columnTitle.textProperty());
        columnTitle.minWidthProperty().bind(widthProperty());

        setGraphic(columnTitle);
    }


}
