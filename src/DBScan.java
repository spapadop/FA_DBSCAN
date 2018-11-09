import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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

                    clusters.add(expandCluster(p, neighbours, cluster));
                }
            }
        }
        print();
    }

    private Cluster expandCluster(Point p, HashSet<Point> neighbours, Cluster cluster){
        List<Point> newPointsToAdd = new ArrayList<>();
        newPointsToAdd.addAll(neighbours);
        for(int i = 0; i < newPointsToAdd.size(); i++){
            Point k = newPointsToAdd.get(i);
            if(k.getLabel().equals("Undefined") || k.getLabel().equals("Noise")){ //point is unclassified
                HashSet<Point> neighboursPts  = regionQuery(k);
                if(neighboursPts.size() >= minPoints){ //join into neighbours
                    k.setLabel("Core");
                    for (Point k2 : neighboursPts){
                        if (!newPointsToAdd.contains(k2)) {
                            newPointsToAdd.add(k);
                        }
                    }
                }else{
                    k.setLabel("Border");
                }
            }
            if(!Cluster.isInACluster(clusters,k)) {
                cluster.addPoint(k);
            }
        }
        return cluster;
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

        System.out.println("-----------------------------");

        for(Point p: points){
            System.out.println(p.getLabel() + ": (" + p.getX() +"," + p.getY() + ")");
        }
    }
}
