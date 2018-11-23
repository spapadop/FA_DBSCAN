import java.util.Objects;

public class Point implements Comparable<Object> {

    private int id;
    private double x;
    private double y;
    private int cluster;
    private PointLabel label;


    public Point() {
        x = 0;
        y = 0;
        setLabelUndefined();
        this.cluster = -1;
    }

    public Point(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        setLabelUndefined();
        this.cluster = -1;
    }

    /**
     * Computes the euclidean distance of current point with the point given in parameter.
     * @param otherPoint
     * @return the distance between the 2 points
     */
    public double getDistanceFrom(Point otherPoint) {
        double dist = Math.sqrt(Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2));
        return dist;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", cluster=" + cluster +
                ", label=" + label +
                '}';
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

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public void setLabelBorder() {
        label = PointLabel.BORDER;
    }

    public void setLabelCore() {
        label = PointLabel.CORE;
    }

    public void setLabelNoise() {
        label = PointLabel.NOISE;
    }

    public void setLabelUndefined() {
        label = PointLabel.UNDEFINED;
    }

    public boolean isCore() {
        return label.equals(PointLabel.CORE);
    }

    public boolean isUndefined() {
        return label.equals(PointLabel.UNDEFINED);
    }

    public boolean isNoise() {
        return label.equals(PointLabel.NOISE);
    }

    public boolean isBorder() {
        return label.equals(PointLabel.BORDER);
    }

    public PointLabel getLabel() {
        return label;
    }

    @Override
    public int compareTo(Object o) {
        Point p = (Point) o;
        if (this.getX() < p.getX()) return -1;
        if (this.getX() > p.getX()) return 1;
        return 0;
    }

}
