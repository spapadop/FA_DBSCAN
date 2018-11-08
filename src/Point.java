import java.util.Objects;

public class Point {

    private double x;
    private double y;
    private String label;
    private int cluster;

    public Point(){
        x = 0; y = 0;
        label = "";
    }

    public Point(double x, double y){
        this.x = x;
        this.y = y;
        this.label = "Undefined";
        this.cluster = -1;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getDistanceFrom(Point otherPoint){
        double dist = Math.sqrt(Math.pow(this.x - otherPoint.x,2) + Math.pow(this.y - otherPoint.y,2));
        return dist;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
