package rtg.repositories;

import rtg.exceptions.FileOperationsException;
import rtg.exceptions.PointNotFoundException;
import rtg.model.Point;
import rtg.utils.FileOperations;

import java.util.List;

import static java.lang.Math.abs;

public class PointRepository {

    public static final double RADIUS = 8;
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

    public Point getPointByCircle(Point point) throws FileOperationsException, PointNotFoundException {
        refreshList();
        for(Point p : pointList) {
            if(abs(p.getX() - point.getX()) < RADIUS && abs(p.getY() - point.getY()) < RADIUS) {
                return p;
            }
        }
        throw new PointNotFoundException();
    }

    public List<Point> getPoints() throws FileOperationsException {
        refreshList();
        return this.pointList;
    }

    public void editPoint(Point oldPoint, Point newPoint) throws PointNotFoundException, FileOperationsException {
        refreshList();
        if(pointList.contains(oldPoint)) {
            pointList.remove(oldPoint);
            pointList.add(newPoint);
            fileOperations.savePointsToFile(this.pointList);
        } else throw new PointNotFoundException();
    }
}
