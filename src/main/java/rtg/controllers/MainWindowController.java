package rtg.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import rtg.exceptions.FileOperationsException;
import rtg.model.Point;
import rtg.repositories.PointRepository;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class MainWindowController implements Initializable {

    private PointRepository pointRepository;

    @FXML
    private ListView<Point> pointListView;
    @FXML
    private ObservableList<Point> pointList;

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
    private List<Pane> dots = new LinkedList<>();

    public MainWindowController() {
        this.pointListView = new ListView<>();
        try {
            this.pointRepository = new PointRepository();
            pointList = FXCollections.observableArrayList(pointRepository.getPoints());
        } catch (FileOperationsException e) {
            //TODO alert
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPointList();
        initPictures();
        drawCircles();
        addPointOnMouseClick();
    }

    private void initPointList() {
        pointListView.setItems(pointList);
        pointListView.setCellFactory(pointListView -> new ListViewCell());
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
                    for(Pane pane : dots)
                        pane.getChildren().add(new Circle(event.getX(), event.getY(), 8, Color.BLACK));
                    try {
                        pointRepository.addPoint(new Point((int) event.getX(), (int) event.getY()));
                        pointList = FXCollections.observableArrayList(pointRepository.getPoints());
                    } catch (FileOperationsException e) {
                        //TODO alert
                        e.printStackTrace();
                    }
                    pointListView.setItems(pointList);
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
            for(Pane pane : dots) {
                pane.getChildren().add(new Circle(point.getX(), point.getY(), 8, Color.BLACK));
            }
        }
    }
}
