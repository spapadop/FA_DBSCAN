import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class FADBScan extends Scan{

    private double cellWidth;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private int nRows;
    private int nCols;
    private Grid grid;


    public FADBScan(){
        eps = 10;
        minPoints = 15;
        clusterCounter = 0;
        points = new ArrayList<>();
        clusters = new ArrayList<>();
        readData("input.txt");
        
        scan ();
    }
    
    public void scan () {
        constructGrid(); //Step 1:  partition the data using a grid -hashmap
        constructBoxes(); //Step 1:  partition the data using boxes -sorting

        determineCorePoints(); //Step 2

        mergingClusters(); //Step 3

        DetermineBorderPoint(); //Step 4
    }

    private void DetermineBorderPoint() {
    	grid = new Grid (grid.getLength(),grid.getColLength(0),eps);
    	
    	for (int i = 0; i < grid.getLength(); i++) {
    		for (int j = 0; j < grid.getColLength(i); j++) {
    			Cell currentCell = grid.getCell(i, j);    			
    			int numberOfPoint = currentCell.getNumberOfPoints();
    			if (numberOfPoint > 0) {
    				for (int k = 0; k < numberOfPoint;k++){
    					Point currentPoint = currentCell.getPoint(k);
    					if (!currentPoint.isCore()) {
    						Point q  = null;
    						List<Cell> neighborCellsList = grid.getNeighborsOfCell(i,j);
    						for (Cell neighborCell : neighborCellsList) {
    							Point tempPoint = neighborCell.getNearestCorePoint(currentPoint);
    							if (q == null || currentPoint.getDistanceFrom(tempPoint) <= currentPoint.getDistanceFrom(q)) {
    								q = tempPoint;
    							}
    						}
    						if (q != null) {
    							currentPoint.setCluster(q.getCluster());
    						}else {
    							currentPoint.setLabelNoise();
    						}
    					}
    				}
    			}
    		}
    	}
    }


    private void mergingClusters() {

        /*
        This step is done based on the fact that if the distance between two core points in two
        different cells is at most ", these two points belong to the same cluster. A simple example
        is shown in Figure 3.4. This figure shows two neighboring cells, each contains four points.
        Since minP ts = 3, we know that all of these points are core points. If the distance between a
        and b is at most ", we can conclude that all of these eight points belong to the same cluster.
        Otherwise, we conclude that there are two clusters with four points each
         */
    }

    private void determineCorePoints() {

        /*
        Note that if we use Algorithm
        3 when partitioning, it will gives us some empty cells inside the grid. To get the list of all
        non-empty cells, we can simply create a new list, scan all the cells and add only the non-empty
        cells to the list.
         */

        /* Algorithm 6
        4: for each non-empty cell c 2 G do
        5: if jcj > minPts then
        6: Mark all points p 2 c as core points.
        7: else
        8: for each point p 2 c do
        9: nPoints 0
        10: for each cell nc 2 N"(c) do
        11: for each point q 2 nc do
        12: if dist(p; q) ≤ " then
        13: nP oints nP oints + 1
        14: if nP oint ≥ minP ts then
        15: Mark p as a core point
        16: break
        17: if nP oint ≥ minP ts then
        18: break
         */
    }

    private void constructBoxes() {

        cellWidth = eps/Math.sqrt(2);

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

    private void addStripToGrid(){

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
    private void constructGrid(){
        cellWidth = eps/Math.sqrt(2);
        calculateMinMaxDimensions();
        nRows = (int) ((maxX-minX)/cellWidth + 1);
        nCols = (int) ((maxY-minY)/cellWidth + 1);

        /*
            In the case of a point on the border of a cell,
            we assign it in the top-right cell as shown in Figure 3.1, where x will be assigned to cell A
            and y will be assigned to cell B. If at least one point is exactly on the top-most border or
            right-most border of the grid, we will add an extra row / column to prevent it to be assigned
            to the outside of the grid. For example, in Figure 3.1, an extra row is added because of point
            p and an extra column is added because of point q. An algorithm to construct this is shown
            in Algorithm 3.
        */

        grid = new Grid (nRows,nCols,eps);
        for(Point p: points){
            int tempx; int tempy;
            tempx = (int) ((p.getX()-minX)/cellWidth + 1);
            tempy = (int) ((p.getY()-minY)/cellWidth + 1);
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

        for(Point p: points){
            if(p.getX() > maxX){ maxX = p.getX(); }
            if(p.getX() < minX){ minX = p.getX(); }
            if(p.getY() > maxY){ maxY = p.getY(); }
            if(p.getY() < minY){ minY = p.getY(); }
        }
    }

}
