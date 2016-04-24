package Model.Inhabitants;

import java.util.Random;

import Model.Oddworld.Cell;
import Model.Oddworld.Map;

import static Utils.CellConst.*;
import static Utils.SimConst.*;
import static Utils.Geometry.*;

public class Paramite extends MovingBody {
	
	public Paramite(Map map) {
		Random rand = new Random();
		do {
			x = rand.nextInt(map.getCellX()-1);
			y = rand.nextInt(map.getCellY()-1);
		} while (map.getGrid()[(int) x][(int) y].getState() != FREE_STATE);
		x += 0.5f;
		y += 0.5f;
		rotation = rand.nextInt(360);
		this.map = map;
	}
	
	public void step() {
		Random rand = new Random();
		switch (rand.nextInt(3)) {
		case 0: turnLeft(); break;
		case 1: turnRight(); break;
		}
		if (!move())
			rotation += 180;
	}
	
	public boolean turnLeft() {
		rotation -= PARAMITE_ROATION_SPEED_DEFAULT;
		return true;
	}
	
	public boolean turnRight() {
		rotation += PARAMITE_ROATION_SPEED_DEFAULT;
		return true;
	}
	
	public boolean move() {
		double theta = rotation*2*Math.PI/(double)360;
		float[] upDirection = {0.f, PARAMITE_SPEED_DEFAULT};
		float[] movment = rotatePoint(upDirection, theta);
		x += movment[0];
		y += movment[1];
		for (Cell c : getCollidedCells(map)) {
			if (c.getState() == BLOC_STATE) {
				x -= movment[0];
				y -= movment[1];
				return false;
			}
		}
		for (Cell c : getCollidedCells(map)) {
			if (c.getState() == SPOOCE_STATE) {
				c.setState(FREE_STATE);
			}
		}
		return true;
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

}
