import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grid {

    private HashMap<Integer, Cell> grid;
    private double cellWidth;
    public int nrows;
    public int ncols;


    public Grid(double eps) {
        cellWidth = eps / Math.sqrt(2);
        grid = new HashMap<>();
    }

    public Grid(int rows, int cols, double eps) {
        cellWidth = eps / Math.sqrt(2);
        nrows = rows;
        ncols = cols;
        grid = new HashMap<>();
    }

    /**
     *Calculates the list of neighbor Cells of a given Cell.
     * @param i
     * @param j
     * @return a list of neighbor Cells of current cell
     */
    public List<Cell> calculateNeighboringCells(int i, int j) {
        List<Cell> nCells = new ArrayList<>();

        for (int row = i - 2; row <= i + 2; row++) {
            boolean rowInBounds = (row >= 0) && (row < nrows);
            if (rowInBounds) {
                for (int col = j - 2; col <= j + 2; col++) {
                    boolean colInBounds = (col >= 0) && (col < ncols);
                    if (colInBounds) {
                        boolean isCorner, isCenter;
                        //isCenter = row == i && col == j;
                        isCorner = (row == i - 2 && col == j - 2) || (row == i + 2 && col == j + 2) || (row == i + 2 && col == j - 2) || (row == i - 2 && col == j + 2);
                        if (!isCorner) {
                            if (this.hasCell(row, col)) {
                                nCells.add(this.getCell(row, col));
                            }
                        }
                    }
                }
            }
        }
        return nCells;
    }

    public boolean hasCell(int i, int j) {
        int key = i * (ncols + 1) + j;
        return grid.containsKey(key);
    }

    public void setPointInCell(int x, int y, Point newPoint) {
        int key = x * (ncols + 1) + y;
        if (!grid.containsKey(key)) {
            grid.put(key, new Cell());
            grid.get(key).addPoint(newPoint);
        } else {
            grid.get(key).addPoint(newPoint);
        }
    }

    public Cell getCell(int x, int y) {
        return grid.get(x * (ncols + 1) + y);
    }

    public void setCell(int x, int y, Cell cell) {
        grid.put(x * (ncols + 1) + y, cell);
    }

    public int getLength() {
        return grid.size();
    }

    public int getColLength(int i) {
        return ncols;
    }

    public Cell getCellbyKey(int key) {
        return grid.get(key);
    }

    public double getCellWidth() {
        return cellWidth;
    }

}
