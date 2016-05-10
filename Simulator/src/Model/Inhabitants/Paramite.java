package Model.Inhabitants;

import java.util.Random;

import Model.Oddworld.Cell;
import Model.Oddworld.Map;
import Utils.JsonConverter;

import static Utils.CellConst.*;
import static Utils.SimConst.*;
import static Utils.Geometry.*;

public class Paramite extends MovingBody implements JsonConverter {
	
	public Paramite(Map map) {
		super();
		
		//Creating a paramite at a free space
		Random rand = new Random();
		do {
			x = rand.nextInt(map.getWidth()-1);
			y = rand.nextInt(map.getHeight()-1);
		} while (map.getGrid()[(int) x][(int) y].getState() != FREE_STATE);
		//Placing it at the middle of the cell
		x += 0.5f;
		y += 0.5f;
		//Put a random rotation
		rotation = rand.nextInt(360);
		this.map = map;
	}
	
	public void step() {
		//Dumb paramite always moves and turns randomly
		Random rand = new Random();
		switch (rand.nextInt(3)) {
		case 0: turnLeft(); break;
		case 1: turnRight(); break;
		}
		//Move back if cannot move forward
		if (!move())
			rotation += 180;
		checkView(map);
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
		//try to move forward
		double theta = rotation*2*Math.PI/(double)360;
		float[] upDirection = {0.f, PARAMITE_SPEED_DEFAULT};
		float[] movment = rotatePoint(upDirection, theta);
		x += movment[0];
		y += movment[1];
		//If there's an obstacle, cancel the movement
		for (Cell c : getCollidedCells(map)) {
			if (c.getState() >= BLOC_STATE) {
				x -= movment[0];
				y -= movment[1];
				return false;
			}
		}
		//Eat the spooce if there's one
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

	@Override
	public String toJSON() {
		return "{id: " + id + ", x: " + x + ", y: " + y + "}";
	}

}
