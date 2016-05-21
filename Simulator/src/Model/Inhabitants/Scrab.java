package Model.Inhabitants;

import static Utils.Const.CellConst.*;
import static Utils.Const.SimConst.*;
import static Utils.Geometry.rotatePoint;

import java.util.Random;

import Model.Oddworld.Cell;
import Model.Oddworld.Map;
import Utils.JsonConverter;

	public class Scrab extends MovingBody implements JsonConverter {
		
		public Scrab(Map map) {
			super();
			
			//Creating a scrab at a free space
			Random rand = new Random();
			do {
				x = rand.nextInt(map.getWidth()-1);
				y = rand.nextInt(map.getHeight()-1);
			} while (map.getGrid()[(int) x][(int) y].getState() >= BLOC_STATE);
			//Placing it at the middle of the cell
			x += 0.5f;
			y += 0.5f;
			//Put a random rotation
			rotation = rand.nextInt(360);
			this.map = map;
		}
		
		public void step() {
			//Dumb scrab always moves and turns randomly
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
			rotation -= SCRAB_ROATION_SPEED_DEFAULT;
			return true;
		}
		
		public boolean turnRight() {
			rotation += SCRAB_ROATION_SPEED_DEFAULT;
			return true;
		}
		
		public boolean move() {
			//try to move forward
			double theta = rotation*2*Math.PI/(double)360;
			float[] upDirection = {0.f, SCRAB_SPEED_DEFAULT};
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
			return true;
		}

		@Override
		public String toJSON() {
			return "{id: " + id + ", x: " + x + ", y: " + y + "}";
		}

}
