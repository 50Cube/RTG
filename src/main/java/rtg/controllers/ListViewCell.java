package rtg.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import rtg.model.Point;

import java.io.IOException;

public class ListViewCell extends ListCell<Point> {

    @FXML
    private HBox hBox;

    @FXML
    private TextField pointX;

    @FXML
    private TextField pointY;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(Point point, boolean empty) {
        super.updateItem(point, empty);

        if(empty || point == null) {
            setText(null);
            setGraphic(null);

        } else {
            if(fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/rtg/ListCellItem.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            pointX.setText(String.valueOf(point.getX()));
            pointY.setText(String.valueOf(point.getY()));
            setText(null);
            setGraphic(hBox);
        }
    }

}
