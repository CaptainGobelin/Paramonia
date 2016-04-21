package Model;

public class Map {
	
	private final int width;
	private final int height;
	private int cellX;
	private int cellY;
	private int cellSize;
	
	private Cell[][] grid;
	
	public Map(int cellX, int cellY, int cellSize) {
		this.width = cellX *  cellSize;
		this.height = cellY * cellSize;
		this.cellX = cellX;
		this.cellY = cellY;
		this.cellSize = cellSize;
		this.grid = new Cell[cellX][cellY];
	}

}
