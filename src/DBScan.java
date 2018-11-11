import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DBScan {

    protected List<Point> points;
    protected List<Cluster> clusters;
    protected int minPoints;
    protected double eps;
    protected int clusterCounter;

    public DBScan() {
        eps = 10;
        minPoints = 5;
        clusterCounter = 0;
        points = new ArrayList<>();
        clusters = new ArrayList<>();
        readData("input.txt");

        for (Point p : points) {
            if (p.getLabel().equals("Undefined")) { //point is unclassified
                HashSet<Point> neighbours = regionQuery(p);
                if ((neighbours.size()+1) < minPoints) {
                    p.setLabel("Noise");
                } else {
                    Cluster cluster = new Cluster(clusterCounter++);
                    p.setLabel("Core");
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
            if (k.getLabel().equals("Undefined") || k.getLabel().equals("Noise")) { //point is unclassified
                HashSet<Point> neighboursPts = regionQuery(k);
                if ((neighboursPts.size()+1) >= minPoints) { //join into neighbours
                    k.setLabel("Core");
                    for (Point k2 : neighboursPts) {
                        if (!newPointsToAdd.contains(k2)) {
                            newPointsToAdd.add(k2);
                        }
                    }
                } else {
                    k.setLabel("Border");
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

    protected void readData(String path) {
        String line = "";
        int id = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] attr = null;
            line = br.readLine(); //remove header
            while ((line = br.readLine()) != null) {
                attr = line.split("\t");
                points.add(new Point(id++, Double.parseDouble(attr[1]), Double.parseDouble(attr[2])));
            }
        } catch (IOException ex) {
            System.out.println("Please enter a valid filepath.");
            System.exit(0);
        }
    }

    public void print() {
        System.out.println("Total clusters: " + clusters.size());
        for (Cluster cluster : clusters) {
            System.out.println("Cluster_" + cluster.getId() +" size:"+ cluster.getPoints().size());
        }

//        for (Cluster cluster : clusters) {
//            System.out.println("----- Cluster Number: " + cluster.getId() + "-----");
//            for (Point p : cluster.getPoints()) {
//                System.out.println("ID: " + p.getId() + " belongs_into: " + p.getCluster() + " as " + p.getLabel() + ": (" + p.getX() + "," + p.getY() + ")");
//            }
//        }
//
//        System.out.println("-----------------------------");
//        System.out.println("-----------------------------");
//
//        for (Point p : points) {
//            System.out.println(p.getId() + "\t" + p.getCluster() + "\t" + p.getLabel() + "\t" + p.getX() + "\t" + p.getY());
//        }
    }
}
