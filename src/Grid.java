import java.util.ArrayList;
import java.util.List;

public class Grid {

	private Cell [] [] grid;
	private double cellWidth;
	
	public Grid (int rows, int cols, double eps ) {
		 cellWidth = eps/Math.sqrt(2);
		 grid = new Cell [rows] [cols];
		 for(int i=0; i<rows; i++){
		 	for(int j=0; j<cols; j++){
		 		grid[i][j] = new Cell();
			}
		 }
	}

	public double getCellWidth() {
		return cellWidth;
	}
	
	public Cell getCell (int x, int y) {
		return grid [x] [y];
	}
	
	public void setCell (int x, int y, Cell cell) {
		grid [x] [y] = cell;
	}
	
	public void setPointInCell (int x, int y, Point newPoint) {
		grid [x] [y].addPoint(newPoint);
	}
	
	public int getLength() {
		return grid.length;
	}
	
	public int getColLength(int i) {
		return grid[i].length;
	}
	
	public List<Cell> getNeighborsOfCell (int rowPosition, int colPosition) {
		List <Cell> neighborsList = new ArrayList<Cell>();
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				if ((rowPosition + i >=0) && (rowPosition + i >= getLength()) && (colPosition + j >=0) && (colPosition + i >= getColLength(colPosition)) && !((i%4==2) && (j%4==2)) ) {
					neighborsList.add(getCell(i,j));
				}
			}
		}
    	return neighborsList;
    }

	public List<Cell> calculateNeighboringCells(int i, int j) {
		List<Cell> nCells = new ArrayList<>();

		for (int row = i - 2; row <= i + 2; row++) {
			boolean rowInBounds = (row >= 0) && (row < grid.length);
			if (rowInBounds) {
				for (int col = j - 2; col <= j + 2; col++) {
					boolean colInBounds = (col >= 0) && (col < grid[row].length);
					if (colInBounds) {
						if (!(row==i && col==j) && (row != i - 2 && col != j - 2) && (row != i + 2 && col != j + 2) && (row != i + 2 && col != j - 2) && (row != i - 2 && col != j + 2)) { //taking out corners
							nCells.add(grid[i][j]);
						}
					}
				}
			}
		}

		return nCells;
	}

	public List<Cell> calculateOnlyNeighboringCells(int i, int j) {
		List<Cell> nCells = new ArrayList<>();

		for (int row = i - 2; row < i + 2; row++) {
			boolean rowInBounds = (row >= 0) && (row < grid.length);
			if (rowInBounds) {
				for (int col = j - 2; col < j + 2; col++) {
					boolean colInBounds = (col >= 0) && (col < grid[row].length);
					if (colInBounds) {
						if ((row != i && col != j) && (row != i - 2 && col != j - 2) && (row != i + 2 && col != j + 2) && (row != i - 2 && col != j - 2) && (row != i + 2 && col != j - 2) && (row != i - 2 && col != j + 2)) { //taking out corners
							nCells.add(grid[i][j]);
						}
					}
				}
			}
		}

		return nCells;
	}
}
