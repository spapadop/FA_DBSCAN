import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DBScan extends Scan {

    public DBScan() {
        eps = 10;
        minPoints = 5;
        clusterCounter = 0;
        points = new ArrayList<>();
        clusters = new ArrayList<>();
        readData("input.txt");
        scan();
    }


    public DBScan(double eps, int minPoints, String filename) {
        this.eps = eps;
        this.minPoints = minPoints;
        clusterCounter = 0;
        points = new ArrayList<>();
        clusters = new ArrayList<>();
        readData(filename);
        scan();
    }

    public void scan() {
        for (Point p : points) {
            if (p.isUndefined()) { //point is unclassified
                HashSet<Point> neighbours = regionQuery(p);
                if ((neighbours.size() + 1) < minPoints) {
                    p.setLabelNoise();
                } else {
                    Cluster cluster = new Cluster(clusterCounter++);
                    p.setLabelCore();
                    p.setCluster(cluster.getId());
                    cluster.addPoint(p);
                    clusters.add(expandCluster(p, neighbours, cluster));
                }
            }
        }
        print();
    }

    protected Cluster expandCluster(Point p, HashSet<Point> neighbours, Cluster cluster) {
        List<Point> newPointsToAdd = new ArrayList<>();
        newPointsToAdd.addAll(neighbours);
        for (int i = 0; i < newPointsToAdd.size(); i++) {
            Point k = newPointsToAdd.get(i);
            if (k.isUndefined() || k.isNoise()) { //point is unclassified
                HashSet<Point> neighboursPts = regionQuery(k);
                if ((neighboursPts.size() + 1) >= minPoints) { //join into neighbours
                    k.setLabelCore();
                    for (Point k2 : neighboursPts) {
                        if (!newPointsToAdd.contains(k2)) {
                            newPointsToAdd.add(k2);
                        }
                    }
                } else {
                    k.setLabelBorder();
                }
            }
            if (!Cluster.isInACluster(clusters, k)) {
                k.setCluster(cluster.getId());
                cluster.addPoint(k);
            }
        }
        neighbours.addAll(newPointsToAdd);
        return cluster;
    }

    /**
     * Given a point "origin" returns all its neighbours according to the eps and minPoints thresholds.
     *
     * @param origin
     * @return a set of neighbour points of given "origin" point
     */
    protected HashSet<Point> regionQuery(Point origin) {
        HashSet<Point> neighbours = new HashSet<>();
        for (Point p : points) {
            if (!p.equals(origin) && origin.getDistanceFrom(p) <= eps) {
                neighbours.add(p);
            }
        }
        return neighbours;
    }

}
