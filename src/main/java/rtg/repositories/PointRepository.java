package rtg.repositories;

import rtg.exceptions.FileOperationsException;
import rtg.exceptions.PointNotFoundException;
import rtg.model.Point;
import rtg.utils.FileOperations;

import java.util.List;

public class PointRepository {

    private FileOperations fileOperations;
    private List<Point> pointList;

    public PointRepository() throws FileOperationsException {
        this.fileOperations = new FileOperations();
        refreshList();
    }

    public void refreshList() throws FileOperationsException {
        this.pointList = fileOperations.readPointsFromFile();
    }

    public void addPoint(Point point) throws FileOperationsException {
        pointList.add(point);
        fileOperations.savePointsToFile(this.pointList);
    }

    public Point getPoint(Point point) throws PointNotFoundException, FileOperationsException {
        refreshList();
        if(pointList.contains(point))
            return point;
        else throw new PointNotFoundException();
    }

    public List<Point> getPoints() throws FileOperationsException {
        refreshList();
        return this.pointList;
    }

    public void editPoint(Point oldPoint, Point newPoint) throws PointNotFoundException, FileOperationsException {
        if(pointList.contains(oldPoint)) {
            pointList.remove(oldPoint);
            pointList.add(newPoint);
            fileOperations.savePointsToFile(this.pointList);
        } else throw new PointNotFoundException();
    }
}
