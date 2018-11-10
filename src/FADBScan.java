import java.util.HashSet;

public class FADBScan extends DBScan{

    private double cellWidth;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double nRows;
    private double nCols;


    public FADBScan(){
        eps = 10;
        minPoints = 15;
        clusterCounter = 0;
        points = new HashSet<>();
        clusters = new HashSet<>();
        readData("input.txt");

        cellWidth = eps/Math.sqrt(2);
        calculateMinMaxDimensions();
        nRows = (maxX-minX)/cellWidth + 1;
        nCols = (maxY-minY)/cellWidth + 1;

//          7: Initialize G as an empty grid with size nCells = nRows × nCols
//          8: for each point p 2 P do
//          9: Add point p to G bp:x cellWidth − minX + 1c bp:y cellWidth − minY + 1c
//          10: return G


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
