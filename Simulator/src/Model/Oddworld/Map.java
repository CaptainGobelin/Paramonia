package Model.Oddworld;

import static Utils.Const.CellConst.*;
import static Utils.Const.GraphicsConst.*;
import static Utils.Const.SimConst.*;

import java.util.ArrayList;
import java.util.Random;

import Controller.MainController;
import Model.Inhabitants.*;
import Utils.JsonConverter;

public class Map implements JsonConverter {
	
	private MainController controller;

	private int width;
	private int height;
	private int cellSize;
	private int gen;
	
	private Cell[][] grid;
	
	private float vegetationRate = 0;
	public ArrayList<Spooce> spoocePopulation = new ArrayList<Spooce>();
	public ArrayList<Paramite> paramitePopulation = new ArrayList<Paramite>();
	public ArrayList<Scrab> scrabPopulation = new ArrayList<Scrab>();
	
	public Map(MainController controller, int cellX, int cellY, int cellSize) {
		this.width = cellX;
		this.height = cellY;
		this.cellSize = cellSize;
		this.gen = 1;
		this.grid = new Cell[cellX][cellY];
		for (int i=0;i<cellX;i++)
			for (int j=0;j<cellY;j++)
				grid[i][j] = new Cell(i, j);
		this.controller = controller;
	}
	
	public void generate() {
		int toWall = BLOC_PERCENT;
		int limit = CELL_SWITCH_LIMIT;
		int loop = LOOP_GEN;
		Random rand = new Random();
		//It's a cellula automata algorithm
		//At te begining each cell is randomly convert
		for (int i=0;i<width;i++)
			for (int j=0;j<height;j++)
				if ((i==0)||(i==(width-1))||(j==0)||(j==(height-1)))
					grid[i][j].setState(BLOC_STATE);
				else
					if ((rand.nextInt())<toWall)
						grid[i][j].setState(BLOC_STATE);
					else
						grid[i][j].setState(FREE_STATE);
		//Then we apply four times a simple algorithm
		//For each cell, if the number of neighboors (include the cell itself)
		//Is in [1..4] we convert it to a wall
		int[][] copy = new int[width][height];
		for (int turn=0;turn<(loop+1);turn++) {
			for (int i=1;i<width-1;i++)
				for (int j=1;j<height-1;j++) {
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
			for (int i=1;i<width-1;i++)
				for (int j=1;j<height-1;j++)
					grid[i][j].setState(copy[i][j]);
		}
		//Finally we convert isolated walls to floor (3 times to make it clean)
		for (int turn=0;turn<loop;turn++) {
			for (int i=1;i<width-1;i++)
				for (int j=1;j<height-1;j++) {
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
			for (int i=1;i<width-1;i++)
				for (int j=1;j<height-1;j++)
					grid[i][j].setState(copy[i][j]);
		}
		
		convertGrass();
		flood();
		
		//Free vegetaion and creature's lists
		clear();
		
		//Generating new populations
		for (int i=0;i<width*height*SPOOCE_GROWING_RATE;i++)
			grow();
		generateParamitePopulation();
		generateScrabPopulation();
	}
	
	public void clear() {
		for (Spooce s : spoocePopulation)
			removeSpooce(s);
		vegetationRate = 0.f;
		spoocePopulation.clear();
		paramitePopulation.clear();
		scrabPopulation.clear();
	}
	
	public void newGeneration() {
		Random rand = new Random();
		ArrayList<Paramite> newParamitePop = new ArrayList<Paramite>();
		Paramite[] parentA = new Paramite[STARTING_PARAMITE_NB];
		Paramite[] parentB = new Paramite[STARTING_PARAMITE_NB];
		double totalFitnessParamite = 0;
		for (Paramite p : paramitePopulation)
			totalFitnessParamite += p.getFitness() - PARAMITE_STARTING_ENERGY;
		controller.console.writeln(" Paramite mean fitness: " + totalFitnessParamite/paramitePopulation.size());
		for (int i=0;i<STARTING_PARAMITE_NB;i++) {
			float r = rand.nextFloat();
			double objective = 0;
			parentA[i] = paramitePopulation.get(0);
			for (Paramite p : paramitePopulation) {
				objective += p.getFitness()/totalFitnessParamite;
				if (r <= objective) {
					parentA[i] = p;
					break;
				}
			}
		}
		for (int i=0;i<STARTING_PARAMITE_NB;i++) {
			float r = rand.nextFloat();
			double objective = 0;
			parentB[i] = paramitePopulation.get(0);
			for (Paramite p : paramitePopulation) {
				objective += p.getFitness()/totalFitnessParamite;
				if (r <= objective) {
					parentB[i] = p;
					break;
				}
			}
		}
		for (int i=0;i<STARTING_PARAMITE_NB;i++)
			newParamitePop.add(new Paramite(this, parentA[i].getBrain(), parentB[i].getBrain()));
		
		ArrayList<Scrab> newScrabPop = new ArrayList<Scrab>();
		Scrab[] parentAb = new Scrab[STARTING_SCRAB_NB];
		Scrab[] parentBb = new Scrab[STARTING_SCRAB_NB];
		double totalFitnessScrab = 0;
		for (Scrab s : scrabPopulation)
			totalFitnessScrab += s.getFitness() - SCRAB_STARTING_ENERGY;
		controller.console.writeln(" Scrab mean fitness: " + totalFitnessScrab/scrabPopulation.size());
		for (int i=0;i<STARTING_SCRAB_NB;i++) {
			float r = rand.nextFloat();
			double objective = 0;
			parentAb[i] = scrabPopulation.get(0);
			for (Scrab s : scrabPopulation) {
				objective += s.getFitness()/totalFitnessScrab;
				if (r <= objective) {
					parentAb[i] = s;
					break;
				}
			}
		}
		for (int i=0;i<STARTING_SCRAB_NB;i++) {
			float r = rand.nextFloat();
			double objective = 0;
			parentBb[i] = scrabPopulation.get(0);
			for (Scrab s : scrabPopulation) {
				objective += s.getFitness()/totalFitnessScrab;
				if (r <= objective) {
					parentBb[i] = s;
					break;
				}
			}
		}
		for (int i=0;i<STARTING_SCRAB_NB;i++)
			newScrabPop.add(new Scrab(this, parentAb[i].getBrain(), parentBb[i].getBrain()));
		clear();
		for (int i=0;i<width*height*SPOOCE_GROWING_RATE;i++)
			grow();
		paramitePopulation = newParamitePop;
		scrabPopulation = newScrabPop;
		
		gen++;
	}
	
	public void flood() {
		Random rand = new Random();
		int count = 0;
		do {
			count++;
			int fX,fY;
			int tries = 0;
			//We take a random floor
			do {
				tries++;
				fX = rand.nextInt(width-2)+1;
				fY = rand.nextInt(height-2)+1;
			} while (grid[fX][fY].getState() != BLOC_STATE && tries < MAX_TRIES_FLOOD);
			//And we start flooding from this cell
			flood_rec(fX, fY);
		} while (count < LAKES_PERCENT*width*height);
		//Then we replace flooded cells by normal cells
		//And non flooded cells by walls
		for (int i=1;i<width-1;i++) {
			for (int j=1;j<height-1;j++) {
				if (grid[i][j].getState() == TEMP_STATE)
					grid[i][j].setState(WATER_STATE);
			}
		}
	}

	public void flood_rec(int fX, int fY) {
		//We mark the cell flooded
		if (grid[fX][fY].getState() == BLOC_STATE)
			grid[fX][fY].setState(TEMP_STATE);
		//We call the flood method on the adjacent cells
		if (fX > 1)
			if (grid[fX-1][fY].getState() == BLOC_STATE)
				flood_rec(fX-1,fY);
		if (fX < width-2)
			if (grid[fX+1][fY].getState() == BLOC_STATE)
				flood_rec(fX+1,fY);
		if (fY > 1)
			if (grid[fX][fY-1].getState() == BLOC_STATE)
				flood_rec(fX,fY-1);
		if (fY < height-2)
			if (grid[fX][fY+1].getState() == BLOC_STATE)
				flood_rec(fX,fY+1);
	}
	
	public void grow() {
		//At each turn we only have a chance to grow a spooce
		/*int toGrow = (int)(SPOOCE_GROWING_RATE*width*height);
		if (new Random().nextInt(100) < toGrow)
			addSpooce();*/
		int count = 0;
		while (!addSpooce()) {
			count++;
			if (count >= 1000)
				break;
		}
	}
	
	public boolean addSpooce() {
		//Grow new spooce only if there not too much of them
		if (vegetationRate >= MAX_VEGETABLE_RATE)
			return false;
		//Growing a spooce at a free space
		Random rand = new Random();
		int x = rand.nextInt(width);
		int y = rand.nextInt(height);
		if (grid[x][y].getState() < BLOC_STATE && !grid[x][y].isSpooced()) {
			spoocePopulation.add(new Spooce(x, y));
			vegetationRate += (float)1/(width*height);
			grid[x][y].setSpooced(true);
			return true;
		}
		return false;
	}
	
	public void removeSpooce(Spooce spooce) {
		grid[spooce.getX()][spooce.getY()].setSpooced(false);
		vegetationRate -= (float)1/(width*height);
		//spoocePopulation.remove(spooce);
	}
	
	public void generateParamitePopulation() {
		for (int i=0;i<STARTING_PARAMITE_NB;i++)
			paramitePopulation.add(new Paramite(this, null, null));
	}
	
	public void generateScrabPopulation() {
		for (int i=0;i<STARTING_SCRAB_NB;i++)
			scrabPopulation.add(new Scrab(this, null, null));
	}
	
	public void convertGrass() {
		Random rand = new Random();
		for (int state=DIRT_STATE;state<=GRASS_STATE;state++) {
			for (int i=0;i<rand.nextInt((int)(width*height*TO_CONVERT));i++) {
				int x;
				int y;
				do {
					x = rand.nextInt(width-1);
					y = rand.nextInt(width-1);
				} while (grid[(int) x][(int) y].getState() >= BLOC_STATE);
				propagateGrass(state, CONVERT_GRASS, x, y);
			}
		}
	}
	
	public void propagateGrass(int state, float rate, int x, int y) {
		Random rand = new Random();
		grid[x][y].setState(state);
		if ((x > 1) && (rand.nextFloat() < rate))
			if (grid[x-1][y].getState() < BLOC_STATE)
				propagateGrass(state, rate*0.75f, x-1, y);
		if ((y > 1) && (rand.nextFloat() < rate))
			if (grid[x][y-1].getState() < BLOC_STATE)
				propagateGrass(state, rate*0.75f, x, y-1);
		if ((x < width-2) && (rand.nextFloat() < rate))
			if (grid[x+1][y].getState() < BLOC_STATE)
				propagateGrass(state, rate*0.75f, x+1, y);
		if ((y < height-2) && (rand.nextFloat() < rate))
			if (grid[x][y+1].getState() < BLOC_STATE)
				propagateGrass(state, rate*0.75f, x, y+1);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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
	
	public float getVegetationRate() {
		return vegetationRate;
	}

	@Override
	public String toJSON() {
		String result = "{\n map: {\n";
		result += "w: " + width + ", h: " + height + ",\n";
		result += "cont: [\n";
		for (int i=0;i<width;i++) {
			for (int j=0;j<height;j++) {
				switch (grid[i][j].getState()) {
					case (WATER_STATE): result += "{val:" + SPRITE_WATER + "},";break;
					case (BLOC_STATE): result += "{val:" + SPRITE_BLOC + "},";break;
					case (DIRT_STATE): result += "{val:" + SPRITE_DIRT + "},";break;
					case (GRASS_STATE): result += "{val:" + SPRITE_GRASS_2 + "},";break;
					case (SHORT_GRASS_STATE): result += "{val:" + SPRITE_GRASS_1 + "},";break;
					case (TALL_GRASS_STATE): result += "{val:" + SPRITE_GRASS_3 + "},";break;
					default:result += "{val:" + SPRITE_GRASS_2 + "},";break;
				}
			}
			result += '\n';
		}
		result = result.substring(0, result.length()-1);
		return result + "]\n}\n}";
	}
	
	public String creaturesToJSON() {
		String result = "{gen: " + gen + ", creatures: {\n";
		result += "pLen: " + paramitePopulation.size() + ", \n";
		result += "scLen: " + scrabPopulation.size() + ", \n";
		result += "spLen: " + spoocePopulation.size() + ", \n";
		
		result += "paramites: [\n";
		for (Paramite p : paramitePopulation) {
			result += p.toJSON() + ",";
		}
		result = result.substring(0, result.length()-1);
		result += "],\n";
		
		result += "scrabs: [\n";
		for (Scrab s : scrabPopulation) {
			result += s.toJSON() + ",";
		}
		result = result.substring(0, result.length()-1);
		result += "],\n";
		
		result += "spooces: [\n";
		for (Spooce s : spoocePopulation) {
			result += s.toJSON() + ",";
		}
		result = result.substring(0, result.length()-1);
		result += "]\n";
		return result + "\n}\n}";
	}

}
