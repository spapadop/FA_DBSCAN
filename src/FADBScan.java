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
        readData("input-5000.txt");
        scan();
    }

    public FADBScan(double eps, int minPoints) {
        this.eps = eps;
        this.minPoints = minPoints;
        clusterCounter = 0;
        points = new ArrayList<>();
        clusters = new ArrayList<>();
        readData("input-5000.txt");
        scan();
    }

//    70478153127 dbscan with 5000

    public void scan() {


        constructGrid(); //Step 1:  partition the data using a grid -hashmap
        //constructBoxes(); //Step 1:  partition the data using boxes -sorting

        determineCorePoints(); //Step 2

        mergingClusters(); //Step 3

        DetermineBorderPoint(); //Step 4

        printing();

    }

    private void printing() {
        clusters = new ArrayList<>();
        Map<Integer,Cluster> clustersMap = new HashMap<>();
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (!grid.hasCell(i, j)) {
                    continue;
                }
                if(grid.getCell(i, j).getClusterNum() !=-1){
                    if(clustersMap.containsKey(grid.getCell(i, j).getClusterNum())) {
                        for (Point p : grid.getCell(i, j).getList()) {
                            clustersMap.get(grid.getCell(i, j).getClusterNum()).addPoint(p);
                        }
                    } else {
                        Cluster cluster = new Cluster(grid.getCell(i, j).getClusterNum());
                        for (Point p : grid.getCell(i, j).getList()) {
                            cluster.addPoint(p);
                        }
                        clustersMap.put(cluster.getId(),cluster);
                    }
                }
            }
        }
        System.out.println("Running FADBSCAN...");
        System.out.println("Total Clusters: " + clustersMap.size());
        for(Cluster c: clustersMap.values()){
            System.out.println("Cluster_" + c.getId() + ": " + c.getPoints().size() + " points");
        }

        for(Point p: points){
            if(p.isUndefined())
                System.out.println(p.getId() + " " + p.getLabel());
        }

    }

    private void DetermineBorderPoint() {
        for (int i = 0; i < grid.getLength(); i++) {
            for (int j = 0; j < grid.getColLength(i); j++) {
                if (!grid.hasCell(i, j)) {
                    continue;
                }
                Cell currentCell = grid.getCell(i, j);
                int numberOfPoint = currentCell.getNumberOfPoints();
                if (numberOfPoint > 0) {
                    for (int k = 0; k < numberOfPoint; k++) {
                        Point currentPoint = currentCell.getPoint(k);

                        if (!currentPoint.isCore()) {
                            Point q = null;
                            List<Cell> neighborCellsList = grid.calculateNeighboringCells(i, j);
                            for (Cell neighborCell : neighborCellsList) {
                                if (!neighborCell.isEmpty()) {
                                    Point tempPoint = neighborCell.getNearestCorePoint(currentPoint);
                                    if (q == null ) {
                                        q = tempPoint;
                                    } else if (currentPoint.getDistanceFrom(tempPoint) <= currentPoint.getDistanceFrom(q)) {
                                        q = tempPoint;
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
    }


    /**
     * Merging clusters
     */
    private void mergingClusters() {
        for (int i = 0; i < grid.getLength(); i++) {
            for (int j = 0; j < grid.getColLength(i); j++) {
                if (!grid.hasCell(i, j)) {
                    continue;
                }
                Cell currentCell = grid.getCell(i, j);

                if (currentCell.getClusterNum() != -1) {
                    List<Cell> neighborCells = grid.calculateNeighboringCells(i, j);
                    for (Cell c : neighborCells) { // for every neighbor cell of the current cell we are checking
                            findNeighborCluster(currentCell, c);
                    }
                }

            }
        }
    }

    private void findNeighborCluster(Cell currentCell, Cell neighborCell) {
        for (Point p : currentCell.getList()) { //for every point of the current cell we are checking...
            for (Point pn : neighborCell.getList()) { //for every point in a neighbor cell
                if (p.getDistanceFrom(pn) <= eps) {
                    neighborCell.setClusterNum(currentCell.getClusterNum());
                    return;
                }
            }
        }
    }

    /**
     * Determines the core points.
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
                    grid.getCell(i, j).setClusterNum(clusterCounter);
                } else if (cellPoints != 0) {
                    for (Point p : grid.getCell(i, j).getList()) { //of every point of the current cell
                        Set<Point> numPoints = new HashSet<>(); //calculates number of neighbours (points with distance less than eps) TODO: Maybe simple int counter.
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
                                        grid.getCell(i, j).setClusterNum(clusterCounter);
                                        break;
                                    }
                                }
                            }
                            if (numPoints.size() >= minPoints) {
                                break;
                            }
                        }
                    }
                }
                clusterCounter++;
            }
        }
    }

    private void constructBoxes() {

        cellWidth = eps / Math.sqrt(2);

        /* Algorithm 4
        3: cellWidth "=p2
        4: Initialize G as an empty set of boxes
        5: Sort P in x-coordinate
        6: Initialize empty set of points strip
        7: Add the first point P1 to strip
        8: for i 2 to jPj do
        9: q first point of strip
        10: if Pi:x > q:x + cellWidth then
        11: AddStripT oGrid(G; strip; cellWidth)
        12: Remove all points from strip
        13: Add point Pi to strip
        14: AddStripT oGrid(G; strip; cellWidth)
        15: return G
         */
    }

    private void addStripToGrid() {

        /* Algorithm 5
            4: Sort strip in y-coordinate
            5: Initialize empty set of points Box
            6: Add the first point of strip to Box
            7: for i 2 to jPj do
            8: q j-th point of strip
            9: if q:y > Box1:y + cellWidth then
            10: Add Box to G
            11: Remove all points from Box
            12: Add q to Box
            13: Add Box to G
         */
    }

    /**
     * Implements the first step of FA-DBScan.
     * It initializes appropriate thresholds (cellWidth, nRows,nCols) and calling calculateMinMaxDimensions()
     * and classifies all data points into the proper cell in the grid
     */
    private void constructGrid() {
        cellWidth = eps / Math.sqrt(2);
        calculateMinMaxDimensions();
        nRows = (int) ((maxX - minX) / cellWidth + 1);
        nCols = (int) ((maxY - minY) / cellWidth + 1);

        /*
            In the case of a point on the border of a cell,
            we assign it in the top-right cell as shown in Figure 3.1, where x will be assigned to cell A
            and y will be assigned to cell B. If at least one point is exactly on the top-most border or
            right-most border of the grid, we will add an extra row / column to prevent it to be assigned
            to the outside of the grid. For example, in Figure 3.1, an extra row is added because of point
            p and an extra column is added because of point q. An algorithm to construct this is shown
            in Algorithm 3.
        */

        grid = new Grid(nRows, nCols, eps);
        for (Point p : points) {
            int tempx;
            int tempy;
            tempx = (int) ((p.getX() - minX) / cellWidth); //+1
            tempy = (int) ((p.getY() - minY) / cellWidth); //+1
            //TODO: Need to cater for exceptions (e.g. points in the borders of 2 cells)
            grid.setPointInCell(tempx, tempy, p);
        }

        /* Algorithm 3
        3: cellWidth "=p2
        4: Initialize minX, maxX, minY , and maxY from P
        5: nRows bmaxX − minX
        cellWidth + 1c
        6: nCols bmaxY − minY
        cellWidth + 1c
        7: Initialize G as an empty grid with size nCells = nRows × nCols
        8: for each point p 2 P do
        9: Add point p to G bp:x cellWidth − minX + 1c bp:y cellWidth − minY + 1c
        10: return G
         */

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

}
