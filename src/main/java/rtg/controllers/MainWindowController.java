package rtg.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import rtg.exceptions.FileOperationsException;
import rtg.exceptions.PointNotFoundException;
import rtg.model.Point;
import rtg.repositories.PointRepository;

import java.net.URL;
import java.util.*;

import static java.lang.Math.abs;


public class MainWindowController implements Initializable {

    private static PointRepository pointRepository;

    @FXML
    private ListView<Point> pointListView;
    @FXML
    private ObservableList<Point> pointList;

    @FXML
    private Button button;

    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView image4;
    private List<ImageView> images = new LinkedList<>();

    @FXML
    private Pane dots1;
    @FXML
    private Pane dots2;
    @FXML
    private Pane dots3;
    @FXML
    private Pane dots4;
    private static List<Pane> dots = new LinkedList<>();
    private static List<Circle> circles = new ArrayList<>();
    private Point draggedPoint = null;
    private Circle draggedCircle = null;

    public MainWindowController() {
        this.pointListView = new ListView<>();
        try {
            pointRepository = new PointRepository();
            pointList = FXCollections.observableArrayList(pointRepository.getPoints());
        } catch (FileOperationsException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPointList();
        initPictures();
        drawCircles();
        addPointOnMouseClick();
        dragCircles();
        invokeButton();
    }

    private void invokeButton() {
        button.setOnAction(event -> {

            ButtonType cancelButton = new ButtonType("Nie");
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz to zrobić?", new ButtonType("Tak"), cancelButton);
            Optional<ButtonType> result = confirmation.showAndWait();

            if(result.get() == cancelButton)
                confirmation.close();
            else {
                try {
                    pointRepository.removeAllPoints();
                } catch (FileOperationsException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                    alert.showAndWait();
                }
                for (Circle circle : circles)
                    removeCircleFromPanes(circle);
                circles = new ArrayList<>();
                this.pointList.clear();
                this.pointListView.setItems(pointList);
            }
        });
    }

    private void initPointList() {
        pointListView.setItems(pointList);
        pointListView.setCellFactory(pointListView -> {
            try {
                return new ListViewCell();
            } catch (FileOperationsException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
            return new ListCell<>();
        });
    }

    private void initPictures() {
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        for (ImageView image : images) {
            GridPane.setHalignment(image, HPos.CENTER);
            GridPane.setValignment(image, VPos.CENTER);
        }
    }

    private void addPointOnMouseClick() {
        for(Pane pane : dots) {
            pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (getCircle(event.getX(), event.getY()) == null) {
                        for (Pane pane : dots) {
                            Circle circle = new Circle(event.getX(), event.getY(), PointRepository.RADIUS, Color.BLACK);
                            circles.add(circle);
                            pane.getChildren().add(circle);
                        }
                        try {
                            pointRepository.addPoint(new Point((int) event.getX(), (int) event.getY()));
                            pointList = FXCollections.observableArrayList(pointRepository.getPoints());
                        } catch (FileOperationsException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                            alert.showAndWait();
                        }
                        pointListView.setItems(pointList);
                    }
                }
            });
        }
    }

    private void drawCircles() {
        dots.add(dots1);
        dots.add(dots2);
        dots.add(dots3);
        dots.add(dots4);

        for(Point point : pointList) {
            Circle circle = new Circle(point.getX(), point.getY(), PointRepository.RADIUS, Color.BLACK);
            circles.add(circle);
            for(Pane pane : dots) {
                pane.getChildren().add(new Circle(point.getX(), point.getY(), PointRepository.RADIUS, Color.BLACK));
            }
        }
    }

    private void dragCircles() {
        for(Pane pane : dots) {
            pane.setOnMousePressed(event -> {
                    try {
                        draggedPoint = pointRepository.getPointByCircle(new Point((int)event.getX(), (int)event.getY()));
                        draggedCircle = getCircle(event.getX(), event.getY());
                    } catch (PointNotFoundException e) {
                        // Nie znaleziono punktu
                    } catch (FileOperationsException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                        alert.showAndWait();
                    }
            });
            pane.setOnMouseReleased(event -> {
                if(draggedPoint != null && draggedCircle != null) {
                    try {
                        pointRepository.editPoint(draggedPoint, new Point((int) event.getX(), (int) event.getY()));
                        for(Pane p : dots)
                            p.getChildren().add(new Circle(event.getX(), event.getY(), PointRepository.RADIUS, Color.BLACK));
                        removeCircleFromPanes(draggedCircle);
                        circles.remove(draggedCircle);
                        circles.add(new Circle(event.getX(), event.getY(), PointRepository.RADIUS, Color.BLACK));
                        pointList = FXCollections.observableArrayList(pointRepository.getPoints());
                        pointListView.setItems(pointList);
                    } catch (PointNotFoundException e) {
                        // Nie znaleziono punktu
                    } catch (FileOperationsException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                        alert.showAndWait();
                    }
                }
            });
            draggedCircle = null;
            draggedPoint = null;
        }
    }

    private static Circle getCircle(double x, double y) {
        for(Circle circle : circles) {
            if(abs(circle.getCenterX() - x) < PointRepository.RADIUS && abs(circle.getCenterY() - y) < PointRepository.RADIUS)
                return circle;
        }
        return null;
    }

    private static void removeCircleFromPanes(Circle circle) {
        if(circle != null) {
            for (Pane pane : dots) {
                for (int i = 0; i < pane.getChildren().size(); i++) {
                    if (pane.getChildren().get(i).getBoundsInLocal().getMinX() + PointRepository.RADIUS == circle.getCenterX()
                            && pane.getChildren().get(i).getBoundsInLocal().getMinY() + PointRepository.RADIUS == circle.getCenterY())
                        pane.getChildren().remove(pane.getChildren().get(i));
                }
            }
        }
    }

    public static void moveCircle(Point oldPoint, Point newPoint) {
        for(Pane p : dots)
            p.getChildren().add(new Circle(newPoint.getX(), newPoint.getY(), PointRepository.RADIUS, Color.BLACK));
        removeCircleFromPanes(getCircle(oldPoint.getX(), oldPoint.getY()));
        circles.remove(getCircle(oldPoint.getX(), oldPoint.getY()));
        circles.add(new Circle(newPoint.getX(), newPoint.getY(), PointRepository.RADIUS, Color.BLACK));
    }
}
