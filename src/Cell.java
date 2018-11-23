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

    /**
     * Given a point of the cell it returns the nearest point in its neighborhood.
     * @param point
     * @return
     */
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

    /**
     * Sets the given cluster number to the relevant cell attribute but also to all points of the cell.
     * @param clusterNum
     */
    public void setClusterNum(int clusterNum) {
        //isCore = true;
        this.clusterNum = clusterNum;
        for (Point p : list) {
            p.setCluster(clusterNum);
        }
    }

    public void removeAllPoints(){
        list.clear();
    }

    public List<Point> getList() {
        return list;
    }

    public void setList(List<Point> list) {
        this.list = list;
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

    public void outputListElements() {
        for (Point p : list) {
            System.out.println(p);
        }
    }

}
