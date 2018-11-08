import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class DBScan {

    private HashSet<Point> points;
    private HashSet<Cluster> clusters;
    private int minPoints;
    private double eps;
    private int clusterCounter;

    public DBScan(){
        eps = 10;
        minPoints=5;
        clusterCounter = 0;
        points = new HashSet<>();
        clusters = new HashSet<>();
        readData("input.txt");

        for(Point p: points){
            if(p.getLabel().equals("Undefined")){ //point is unclassified
                HashSet<Point> neighbours = regionQuery(p);
                if(neighbours.size() < minPoints){
                    p.setLabel("Noise");
                } else {
                    Cluster cluster = new Cluster(clusterCounter++);
                    p.setLabel("Core");
                    p.setCluster(cluster.getId());
                    cluster.addPoint(p);
                    expandCluster(p, neighbours, cluster);
                    clusters.add(cluster);
                }
            }
        }
        print();
    }

    private void expandCluster(Point p, HashSet<Point> neighbours, Cluster cluster){
        HashSet<Point> newPointsToAdd = new HashSet<>();
        for(Point k: neighbours){
            if(k.getLabel().equals("Undefined")){ //point is unclassified
                HashSet<Point> neighboursPts  = regionQuery(k);
                if(neighboursPts.size() >= minPoints){ //join into neighbours
                    for(Point n: neighboursPts){
                        if (!neighbours.contains(n) && !n.equals(p)){
                            newPointsToAdd.add(n);
                        }
                    }
                }
            }
            if(!Cluster.isInACluster(clusters,k)) {
                cluster.addPoint(k);
            }
        }
        neighbours.addAll(newPointsToAdd);
    }

    private HashSet<Point> regionQuery(Point origin){
        HashSet<Point> neighbours = new HashSet<>();
        for(Point p: points){
            if (!p.equals(origin) && origin.getDistanceFrom(p) < eps) {
                neighbours.add(p);
            }
        }
        return neighbours;
    }


    private void readData(String path){

        String line= "";
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] attr = null;
            line = br.readLine(); //remove header
            while ((line = br.readLine()) != null) {
                attr = line.split("\t");
                points.add(new Point(Double.parseDouble(attr[1]),Double.parseDouble(attr[2])));
            }
        } catch (IOException ex){
            System.out.println("Please enter a valid filepath.");
            System.exit(0);
        }
    }

    public void print(){
        for(Cluster cluster : clusters){
            System.out.println("----- Cluster Number: " + cluster.getId() + "-----");
            for(Point point: cluster.getPoints()){
                System.out.println(point.getLabel() + ": (" + point.getX() +"," + point.getY() + ")");
            }
        }
    }
}
