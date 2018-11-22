import java.util.ArrayList;
import java.util.List;

public class Cell {
    private List<Point> list;
    private int clusterNum;
    private boolean isCore;


    public Cell() {
        clusterNum = -1;
        list = new ArrayList<>();
        isCore = false;
    }

    public void outputListElements() {
        for (Point p : list) {
            System.out.println(p);
        }
    }

    public Point getNearestCorePoint(Point point) {
        Point nearestCorePoint = null;

        for (Point p : list) {
            if(p.isCore()) {
                if ((nearestCorePoint == null) || (point.getDistanceFrom(p) < point.getDistanceFrom(nearestCorePoint))) {
                    nearestCorePoint = p;
                }
            }
        }
        return nearestCorePoint;
    }

    public List<Point> getList() {
        return list;
    }

    public void setList(List<Point> list) {
        this.list = list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int getNumberOfPoints() {
        return list.size();
    }

    public Point getPoint(int position) {
        return list.get(position);
    }

    public void addPoint(Point newPoint) {
        list.add(newPoint);
    }

    public void setPoint(int position, Point newPoint) {
        list.set(position, newPoint);
    }

    public int getClusterNum() {
        return clusterNum;
    }

    public boolean isCore() {
        return isCore;
    }

    public void setCore(boolean core) {
        isCore = core;
    }

    public void setClusterNum(int clusterNum) {
        //isCore = true;
        this.clusterNum = clusterNum;
        for (Point p : list) {
            p.setCluster(clusterNum);
        }
    }

}
