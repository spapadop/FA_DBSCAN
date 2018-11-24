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

    /**
     * Implements the algorithmic logic of original DBSCAN.
     * For every point in our dataset we search for its neighbours (using regionQuery function)
     * and if its size is sufficient, point becomes a core and we call expandCluster function
     * to further expand this newly-created cluster with its points' neighbours.
     * If the number of neighbour points are not sufficient (less than minPoints) then
     * the point is marked as a Noise.
     */
    public void scan() {
        for (Point p : points) {
            if (p.isUndefined()) { //point is unclassified
                HashSet<Point> neighbours = regionQuery(p);
                if ((neighbours.size() + 1) < minPoints) { //+1 because we are ignoring the current point
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
    }

    /**
     * This function iterates over the neighbours of the examining point that were found.
     * For each point of that neighborhood:
     *  a) It marks it as a core if that points’ neighborhood also meets the minimum value (minPoints)
     *  b) It marks it as border if that points’ neighborhood does not meet the minimum value (minPoints)
     * In any case it then merges the point into the current cluster.
     *
     * @param p current point that we examine
     * @param neighbours list of neighbor points for the currently examined point
     * @param cluster the current function that we build based on examining point and its neighbors
     * @return the completed cluster
     */
    protected Cluster expandCluster(Point p, HashSet<Point> neighbours, Cluster cluster) {
        List<Point> newPointsToAdd = new ArrayList<>();
        newPointsToAdd.addAll(neighbours);
        for (int i = 0; i < newPointsToAdd.size(); i++) {
            Point k = newPointsToAdd.get(i);
            if (k.isUndefined() || k.isNoise()) { //point is unclassified
                HashSet<Point> neighboursPts = regionQuery(k);
                if ((neighboursPts.size() + 1) >= minPoints) { //join into neighbours. +1 because we ignored current point
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
            if (!Cluster.isInACluster(clusters, k)) { //checks if k belongs in a cluster.
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
     * @param origin the current point we examine
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
