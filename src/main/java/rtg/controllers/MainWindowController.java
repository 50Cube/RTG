package rtg.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public MainWindowController() {
        this.pointRepository = new PointRepository();
        this.pointListView = new ListView<>();
        try {
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
    }

    private void initPointList() {
        pointListView.setItems(pointList);
        pointListView.setCellFactory(pointListView -> new ListViewCell());
    }

    private void initPictures() {
        ImageView[] images = new ImageView[4];
        images[0] = new ImageView(new Image(getClass().getResourceAsStream("/rtg/picture.png")));
        images[1] = new ImageView(new Image(getClass().getResourceAsStream("/rtg/picture.png")));
        images[2] = new ImageView(new Image(getClass().getResourceAsStream("/rtg/picture.png")));
        images[3] = new ImageView(new Image(getClass().getResourceAsStream("/rtg/picture.png")));
        tableView.add(images[0], 0, 0);
        tableView.add(images[1], 0, 1);
        tableView.add(images[2], 1, 0);
        tableView.add(images[3], 1, 1);
    }
}
