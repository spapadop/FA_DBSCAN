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

        Cluster currentCluster;
        for(Point p: points){
            if(p.isVisited() == false){ //unvisited point
                HashSet<Point> neighbours = regionQuery(p);
                if(neighbours.size()< minPoints){
                    p.setVisited(true);
                } else {
                    Cluster cluster = new Cluster(clusterCounter++);
                    expandCluster(p,neighbours, cluster);
                }
            }
        }

        print();
    }

    private void expandCluster(Point p, HashSet<Point> neighbours, Cluster cluster){
        p.setVisited(true);
        cluster.addPoint(p);
        Iterator<Point> it = neighbours.iterator();
        Point currentPoint;
        while(it.hasNext()){
            currentPoint = it.next();
            if(currentPoint.isVisited() == false){
                currentPoint.setVisited(true);
                HashSet<Point> neighboursPts  = regionQuery(currentPoint);
                if(neighboursPts.size() >= minPoints){
                    neighbours.addAll(neighboursPts);
                }
            } if(Cluster.isInACluster(clusters,currentPoint)) {
                cluster.addPoint(currentPoint);
            }
        }

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
            System.out.println("-----" + cluster.getId() + "-----");
            for(Point point: cluster.getPoints()){
                System.out.println("(" + point.getX() +"," + point.getY() + ")");
            }
        }
    }
}
