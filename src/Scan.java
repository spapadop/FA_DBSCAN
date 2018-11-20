import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public abstract class Scan {
	
    protected List<Point> points;
    protected List<Cluster> clusters;
    protected int minPoints;
    protected double eps;
    protected int clusterCounter;
    
    abstract void scan ();

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

    protected void print() {
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
