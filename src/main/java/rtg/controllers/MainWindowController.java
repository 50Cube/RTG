package rtg.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import rtg.exceptions.FileOperationsException;
import rtg.model.Point;
import rtg.repositories.PointRepository;

import java.net.URL;
import java.util.ResourceBundle;


public class MainWindowController implements Initializable {

    private PointRepository pointRepository;

    @FXML
    private ListView<Point> pointListView;
    private ObservableList<Point> pointList;
    @FXML
    private GridPane tableView;
    ImageView[] images = new ImageView[4];

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
        addPointOnMouseClick();
    }

    private void initPointList() {
        pointListView.setItems(pointList);
        pointListView.setCellFactory(pointListView -> new ListViewCell());
    }

    private void initPictures() {
        for(int i=0; i<images.length; i++) {
            images[i] = new ImageView(new Image(getClass().getResourceAsStream("/rtg/picture.png")));
        }
        tableView.add(images[0], 0, 0);
        tableView.add(images[1], 0, 1);
        tableView.add(images[2], 1, 0);
        tableView.add(images[3], 1, 1);
    }

    private void addPointOnMouseClick() {
        for (ImageView image : images) {
            image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println(event.getX() + " " + event.getY());
                    try {
                        pointRepository.addPoint(new Point((int) event.getX(), (int) event.getY()));
                        pointList = FXCollections.observableArrayList(pointRepository.getPoints());
                        pointListView.setItems(pointList);
                    } catch (FileOperationsException e) {
                        //TODO alert
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
