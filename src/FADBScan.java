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
        calculateMaxDimensions();
        nRows = (maxX-minX)/cellWidth + 1;

    }

    private void calculateMaxDimensions() {
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
