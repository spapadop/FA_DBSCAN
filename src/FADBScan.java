import java.util.*;

public class FADBScan extends Scan {

    private double cellWidth;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private int nRows;
    private int nCols;
    private Grid grid;


    public FADBScan() {
        eps = 10;
        minPoints = 5;
        clusterCounter = 0;
        points = new ArrayList<>();
        clusters = new ArrayList<>();
        readData("input.txt");
        scan();
    }

    public FADBScan(double eps, int minPoints, String filename) {
        this.eps = eps;
        this.minPoints = minPoints;
        clusterCounter = 0;
        points = new ArrayList<>();
        clusters = new ArrayList<>();
        readData(filename);
        scan();
    }

    /**
     * Execute the FADBSCAN algorithm, calling one function for each implementation step:
     * 1. Partitioning using HashMap
     * 2. Determine core points
     * 3. Merge clusters
     * 4. Determine border & noise points
     */
    public void scan() {
        constructGrid();
        determineCorePoints();
        mergingClusters();
        DetermineBorderPoint();
    }

    /**
     * Implements the first step of FA-DBScan.
     * It initializes appropriate thresholds (cellWidth, nRows,nCols) and calling calculateMinMaxDimensions()
     * and classifies all data points into the proper cell in the grid.
     */
    private void constructGrid() {
        cellWidth = eps / Math.sqrt(2);
        calculateMinMaxDimensions();
        nRows = (int) ((maxX - minX) / cellWidth + 1);
        nCols = (int) ((maxY - minY) / cellWidth + 1);
        grid = new Grid(nRows, nCols, eps);
        for (Point p : points) {
            int tempx;
            int tempy;
            tempx = (int) ((p.getX() - minX) / cellWidth);
            tempy = (int) ((p.getY() - minY) / cellWidth);
            grid.setPointInCell(tempx, tempy, p);
        }
    }

    /**
     * Determines the core points of our dataset.
     */
    private void determineCorePoints() {
        for (int i = 0; i < grid.nrows; i++) { // for all rows of grid
            for (int j = 0; j < grid.ncols; j++) { // for all columns of grid
                if (!grid.hasCell(i, j)) {
                    continue;
                }
                if (grid.getCell(i, j).isEmpty()) {
                    continue;
                }
                int cellPoints = grid.getCell(i, j).getList().size();
                if (cellPoints >= minPoints) { //set all points of cell as Core
                    for (Point p : grid.getCell(i, j).getList()) {
                        p.setLabelCore();
                    }
                    grid.getCell(i,j).setCore(true);
                } else if (cellPoints != 0) {
                    for (Point p : grid.getCell(i, j).getList()) { //of every point of the current cell
                        Set<Point> numPoints = new HashSet<>(); //calculates number of neighbours (points with distance less than eps)
                        List<Cell> nCells = grid.calculateNeighboringCells(i, j); //compute the cells within eps distance that can provide possible neighbor points
                        if (nCells.isEmpty()) {
                            continue;
                        }
                        for (Cell nc : nCells) { //for every such neighbor cell (with potential neighbor points)
                            if (nc.isEmpty()) {
                                continue;
                            }
                            for (Point q : nc.getList()) { //we examine all points of a neighbor
                                if (p.getDistanceFrom(q) <= eps) {
                                    if (!numPoints.contains(q)) { //found new neighbor point
                                        numPoints.add(q);
                                    }
                                    if (numPoints.size() >= minPoints) {
                                        p.setLabelCore();
                                        grid.getCell(i,j).setCore(true);
                                        break;
                                    }
                                }
                            }
                            if (numPoints.size() >= minPoints) {
                                break; //continues the break to the outer loop: next cell to examine.
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Performs the merging clusters step.
     */
    private void mergingClusters() {
        for (int i = 0; i < grid.getLength(); i++) {
            for (int j = 0; j < grid.getColLength(i); j++) {
                if (!grid.hasCell(i, j)) {
                    continue;
                }
                Cell currentCell = grid.getCell(i, j);

                if(currentCell.isCore()){
                    List<Cell> neighborCells = grid.calculateNeighboringCells(i, j);
                    for (Cell c : neighborCells) { // for every neighbor cell of the current cell we are checking
                        if(c.isCore()) {
                            findNeighborCluster(currentCell, c);
                        }
                    }
                }
                clusterCounter++;
            }
        }
    }

    /**
     * Searches for the every point of current with every point of neighbor cell and merge or separate clusters accordingly.
     * @param currentCell
     * @param neighborCell
     */
    private void findNeighborCluster(Cell currentCell, Cell neighborCell) {
        for (Point p : currentCell.getList()) { //for every point of the current cell we are checking
            for (Point pn : neighborCell.getList()) { //for every point in a neighbor cell
                if (p.isCore() && pn.isCore() && p.getDistanceFrom(pn) <= eps) {
                    int cNum = p.getCluster();
                    int cNum2 = pn.getCluster();

                    if (cNum==-1 && cNum2 ==-1) {
                        currentCell.setClusterNum(cNum);
                        neighborCell.setClusterNum(clusterCounter);
                        return;
                    } else if (cNum!=-1 && cNum2 == -1) {
                        neighborCell.setClusterNum(cNum);
                        return;
                    } else if (cNum==-1 && cNum2 !=-1){
                        currentCell.setClusterNum(cNum);
                        return;
                    } else if (cNum != cNum2){
                        neighborCell.setClusterNum(cNum);
                        for (Point p2: points){
                            if (p2.getCluster()==cNum2){
                                p2.setCluster(cNum);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Identifies the border & noise points of the dataset.
     */
    private void DetermineBorderPoint() {
        for (int i = 0; i < grid.getLength(); i++) {
            for (int j = 0; j < grid.getColLength(i); j++) {
                if (!grid.hasCell(i, j)) {
                    continue;
                }
                for (Point currentPoint : grid.getCell(i, j).getList()) { //for every point in current cell
                    if (!currentPoint.isCore()) {
                        Point q = null;
                        for (Cell neighborCell : grid.calculateNeighboringCells(i, j)) { //for every neighbor cell
                            if (!neighborCell.isEmpty()) {
                                Point nearCorePoint =null;
                                Point temp = (neighborCell.getNearestCorePoint(currentPoint));
                                if(temp!=null && temp.getDistanceFrom(currentPoint)<=eps){
                                    nearCorePoint = temp;
                                }
                                if(nearCorePoint == null){
                                    continue;
                                }
                                if (q == null) {
                                    q = nearCorePoint;
                                } else if (currentPoint.getDistanceFrom(nearCorePoint) <= currentPoint.getDistanceFrom(q)) {
                                    q = nearCorePoint;
                                }
                            }
                        }
                        if (q != null) {
                            currentPoint.setCluster(q.getCluster());
                            currentPoint.setLabelBorder();
                        } else {
                            currentPoint.setLabelNoise();
                        }
                    }
                }
            }
        }
    }

    /**
     * calculates the min and max X and Y dimensions among all data points.
     */
    private void calculateMinMaxDimensions() {
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        maxY = Double.MIN_VALUE;

        for (Point p : points) {
            if (p.getX() > maxX) {
                maxX = p.getX();
            }
            if (p.getX() < minX) {
                minX = p.getX();
            }
            if (p.getY() > maxY) {
                maxY = p.getY();
            }
            if (p.getY() < minY) {
                minY = p.getY();
            }
        }
    }

    @Override
    protected void print(){
        System.out.println(" using FADBSCAN with EPS " + eps + " and MinPoints " + minPoints);
        printHeader();

        Map<Integer,Cluster> map = new HashMap<>();
        for(Point p: points){
            if (!p.isNoise()) {
                if (map.containsKey(p.getCluster())) {
                    map.get(p.getCluster()).addPoint(p);
                } else {
                    Cluster c = new Cluster(p.getCluster());
                    c.addPoint(p);
                    map.put(p.getCluster(), c);
                }
            }
        }
        System.out.println("Total clusters: " + map.size());
        System.out.println("-----------------------------");
        int clCnt = 0;
        for(Cluster c: map.values()){
            System.out.println("Cluster_" + clCnt + ": " + c.getPoints().size() + " points");
            clCnt++;
        }
        System.out.println("-----------------------------");
    }
}
