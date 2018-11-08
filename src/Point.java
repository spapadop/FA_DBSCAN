import java.util.Objects;

public class Point {

    private double x;
    private double y;
    //private double z;
    private boolean visited;
    private String label;

    public Point(){
        x = 0; y = 0;
        visited = false;
        label = "";
    }

    public Point(double dimX, double dimY){
        this.x = dimX;
        this.y = dimY;
        visited = false;
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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
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
