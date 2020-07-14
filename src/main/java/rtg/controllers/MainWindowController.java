package rtg.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
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

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPointList();
    }

    private void initPointList() {
        pointListView.setItems(pointList);
        pointListView.setCellFactory(pointListView -> new ListViewCell());
    }

}
