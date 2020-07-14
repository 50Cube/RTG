package rtg.repositories;

import rtg.controllers.ListViewCell;
import rtg.exceptions.FileOperationsException;
import rtg.exceptions.PointNotFoundException;
import rtg.model.Point;
import rtg.utils.FileOperations;

import java.util.ArrayList;
import java.util.List;

public class PointRepository {

    private FileOperations fileOperations;
    private List<Point> pointList;

    public PointRepository() {
        this.fileOperations = new FileOperations();
        this.pointList = new ArrayList<>();
    }

    public void addPoint(Point point) throws FileOperationsException {
        pointList.add(point);
        fileOperations.savePointsToFile(this.pointList);
    }

    public Point getPoint(Point point) throws PointNotFoundException, FileOperationsException {
        this.pointList = fileOperations.readPointsFromFile();
        if(pointList.contains(point))
            return point;
        else throw new PointNotFoundException();
    }

    public List<Point> getPoints() throws FileOperationsException {
        return fileOperations.readPointsFromFile();
    }

    public void editPoint(Point oldPoint, Point newPoint) throws PointNotFoundException, FileOperationsException {
        if(pointList.contains(oldPoint)) {
            pointList.remove(oldPoint);
            pointList.add(newPoint);
            fileOperations.savePointsToFile(this.pointList);
        } else throw new PointNotFoundException();
    }
}
