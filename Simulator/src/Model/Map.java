package Model;

import static Utils.CellConst.*;

import java.util.Random;

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
		for (int i=0;i<cellX;i++)
			for (int j=0;j<cellY;j++)
				grid[i][j] = new Cell();
	}
	
	public void generate() {
		int toWall = 45;
		int limit = 4;
		int loop = 3;
		Random rand = new Random();
		//It's a cellula automata algorithm
		//At te begining each cell is randomly convert
		for (int i=0;i<cellX;i++)
			for (int j=0;j<cellY;j++)
				if ((i==0)||(i==(cellX-1))||(j==0)||(j==(cellY-1)))
					grid[i][j].setState(BLOC_STATE);
				else
					if ((rand.nextInt())<toWall)
						grid[i][j].setState(BLOC_STATE);
					else
						grid[i][j].setState(FREE_STATE);
		//Then we apply four times a simple algorithm
		//For each cell, if the number of neighboors (include the cell itself)
		//Is in [1..4] we convert it to a wall
		int[][] copy = new int[cellX][cellY];
		for (int turn=0;turn<(loop+1);turn++) {
			for (int i=1;i<cellX-1;i++)
				for (int j=1;j<cellY-1;j++) {
					int count1=0;
					for (int ii=i-1;ii<i+2;ii++)
						for (int jj=j-1;jj<j+2;jj++)
							if (grid[ii][jj].getState() == BLOC_STATE)
								count1++;
					if ((count1 > limit) || (count1 < 1))
						copy[i][j] = BLOC_STATE;
					else
						copy[i][j] = FREE_STATE;
				}
			//Here we copy the map, to prepare the new step of the loop
			for (int i=1;i<cellX-1;i++)
				for (int j=1;j<cellY-1;j++)
					grid[i][j].setState(copy[i][j]);
		}
		//Finally we convert isolated walls to floor (3 times to make it clean)
		for (int turn=0;turn<loop;turn++) {
			for (int i=1;i<cellX-1;i++)
				for (int j=1;j<cellY-1;j++) {
					int count1=0;
					for (int ii=i-1;ii<i+2;ii++)
						for (int jj=j-1;jj<j+2;jj++)
							if (grid[ii][jj].getState() == BLOC_STATE)
								count1++;
					if (count1 > limit)
						copy[i][j] = BLOC_STATE;
					else
						copy[i][j] = FREE_STATE;
				}
			for (int i=1;i<cellX-1;i++)
				for (int j=1;j<cellY-1;j++)
					grid[i][j].setState(copy[i][j]);
		}
	}

	public int getCellX() {
		return cellX;
	}

	public void setCellX(int cellX) {
		this.cellX = cellX;
	}

	public int getCellY() {
		return cellY;
	}

	public void setCellY(int cellY) {
		this.cellY = cellY;
	}

	public int getCellSize() {
		return cellSize;
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}

	public Cell[][] getGrid() {
		return grid;
	}

	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
