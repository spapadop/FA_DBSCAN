import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cluster {

    private int id;
    private HashSet<Point> points;

    public Cluster(int putId){
        this.id = putId;
        points = new HashSet<>();
    }

    public void addPoint(Point p){
        points.add(p);
    }

    public boolean existPoint(Point p){
        return points.contains(p);
    }

    public static boolean isInACluster(List<Cluster> clusterList, Point p){
        boolean res = false;
        for(Cluster c : clusterList){
            if(c.existPoint(p)){
                res = true;
                break;
            }
        }
        return res;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashSet<Point> getPoints() {
        return points;
    }

    public void setPoints(HashSet<Point> points) {
        this.points = points;
    }
}
