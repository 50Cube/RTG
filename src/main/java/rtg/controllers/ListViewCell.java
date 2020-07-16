package rtg.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import rtg.exceptions.FileOperationsException;
import rtg.exceptions.PointNotFoundException;
import rtg.model.Point;
import rtg.repositories.PointRepository;

import java.io.IOException;

public class ListViewCell extends ListCell<Point> {

    @FXML
    private HBox hBox;

    @FXML
    private TextField pointX;
    @FXML
    private TextField pointY;

    @FXML
    private Button buttonX;
    @FXML
    private Button buttonY;

    private FXMLLoader fxmlLoader;
    private PointRepository pointRepository;

    public ListViewCell() throws FileOperationsException {
        pointRepository = new PointRepository();
    }

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

            addListeners();

            setText(null);
            setGraphic(hBox);
        }
    }


    private void addListeners() {
        String tmpX = pointX.getText();
        String tmpY = pointY.getText();

        pointX.textProperty().addListener((observable, oldValue, newValue) -> {
            buttonX.setOnAction(event -> {
                if(validateData(pointX.getText())) {
                    if (tmpY.equals(pointY.getText())) {
                        Point newPoint = new Point(Integer.parseInt(newValue), Integer.parseInt(tmpY));
                        Point oldPoint = new Point(Integer.parseInt(oldValue), Integer.parseInt(tmpY));
                        try {
                            pointRepository.editPoint(oldPoint, newPoint);
                        } catch (PointNotFoundException e) {
                            // Nie znaleziono punktu
                        } catch (FileOperationsException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                            alert.showAndWait();
                        }
                        MainWindowController.moveCircle(oldPoint, newPoint);
                    }
                }
            });
        });

        pointY.textProperty().addListener((observable, oldValue, newValue) -> {
            buttonY.setOnAction(event -> {
                if(validateData(pointY.getText())) {
                    if (tmpX.equals(pointX.getText())) {
                        Point newPoint = new Point(Integer.parseInt(tmpX), Integer.parseInt(newValue));
                        Point oldPoint = new Point(Integer.parseInt(tmpX), Integer.parseInt(oldValue));
                        try {
                            pointRepository.editPoint(oldPoint, newPoint);
                        } catch (PointNotFoundException e) {
                            // Nie znaleziono punktu
                        } catch (FileOperationsException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                            alert.showAndWait();
                        }
                        MainWindowController.moveCircle(oldPoint, newPoint);
                    }
                }
            });
        });
    }

    private boolean validateData(String input) {
        if(!input.matches("[0-9]{1,3}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wprowadzone dane maja nieprawidlowy format");
            alert.showAndWait();
            return false;
        } else return true;
    }
}
