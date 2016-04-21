package Model.Inhabitants;

import java.util.Random;

import Model.Oddworld.Map;

import static Utils.CellConst.*;

public class Paramite {
	
	private float x;
	private float y;
	private float rotation;
	
	public Paramite(Map map) {
		Random rand = new Random();
		do {
			x = rand.nextInt(map.getCellX()-1);
			y = rand.nextInt(map.getCellY()-1);
		} while (map.getGrid()[(int) x][(int) y].getState() != FREE_STATE);
		rotation = rand.nextInt(360);
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
