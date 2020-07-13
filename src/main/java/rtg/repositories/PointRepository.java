package rtg.repositories;

import rtg.exceptions.FileOperationsException;
import rtg.exceptions.PointNotFoundException;
import rtg.model.Point;
import rtg.utils.FileOperations;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class PointRepository {

    @Inject
    private FileOperations fileOperations;

    private List<Point> pointList;

    @PostConstruct
    public void init() throws FileOperationsException {
        this.pointList = fileOperations.readPointsFromFile();
    }

    public void addPoint(Point point) throws FileOperationsException {
        pointList.add(point);
        fileOperations.savePointsToFile(this.pointList);
    }

    public Point getPoint(Point point) throws PointNotFoundException {
        if(pointList.contains(point))
            return point;
        else throw new PointNotFoundException();
    }

    public List<Point> getPoints() {
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
