package Model.Inhabitants;

import java.util.ArrayList;

import Model.Oddworld.Cell;
import Model.Oddworld.Map;

import static Utils.Geometry.*;
import static Utils.Array.*;
import static Utils.Const.CellConst.*;
import static Utils.Const.SimConst.*;
import static Utils.ID.*;

public abstract class MovingBody {
	
	protected String id;
	
	protected float x;
	protected float y;
	protected float rotation;
	
	protected Map map;
	
	protected double energy;
	protected int state;
	protected int idleTime;
	protected double fitness;
	
	public MovingBody() {
		id = createID();
	}

	public ArrayList<Cell> getCollidedCells(Map map) {
		ArrayList<Cell> result = new ArrayList<Cell>();
		int startingX = (int)(x-0.5f);
		int startingY = (int)(y-0.5f);
		for (int i=startingX-1;i<startingX+2;i++) {
			for (int j=startingY-1;j<startingY+2;j++) {
				if (outOfBounds2(i, j, map.getGrid()))
						continue;
				if (distance2(x, y, i+0.5f, j+0.5f) <= 1)
					result.add(map.getGrid()[i][j]);
			}
		}
		return result;
	}
	
	void checkView(Map map) {
		int xx = Math.round(x);
		int yy = Math.round(y);
		for (int j=Math.max(0,yy-DIST_VIEW);j<Math.min(map.getHeight(),yy+DIST_VIEW+1);j++)
			for (int i=Math.max(0,xx-DIST_VIEW);i<Math.min(map.getWidth(),x+DIST_VIEW+1);i++)
				if (distance2(x, y, i, j) <= DIST_VIEW)
					checkLine(map,i,j);
	}

	public boolean checkLine(Map map, int i, int j) {
		//A standard Bresenham algorithm
		boolean see = true;
		int xx = Math.round(x);
		int yy = Math.round(y);
		int dx = Math.abs(i - xx);
		int dy = Math.abs(j - yy);
		float e = dx-dy;
		int sx = (x < i) ? 1 : -1;
		int sy = (y < j) ? 1 : -1;
		
		boolean stop = false;
		while (!stop) {
			if (see)
				map.getGrid()[xx][yy].setViewed(true);
			if (map.getGrid()[xx][yy].getState() >= BLOC_STATE)
				return false;
			if ((xx == i) && (yy == j))
				return true;
			float e2 = 2*e;
			if (e2 > -dx) {
				e -= dy;
				xx += sx;
			}
			if (e2 < dx) {
				e += dx;
				yy += sy;
			}
		}
		return true;
	}
	
	public void die() {
		state = DEAD_STATE;
		fitness += energy;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public double getEnergy() {
		return this.energy;
	}
	
	public int getState() {
		return this.state;
	}
	
	public double getFitness() {
		return this.fitness;
	}
	
}
