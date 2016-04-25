package Model.Inhabitants;

import java.util.ArrayList;

import Model.Oddworld.Cell;
import Model.Oddworld.Map;

import static Utils.Geometry.*;
import static Utils.Array.*;

public abstract class MovingBody {
	
	protected float x;
	protected float y;
	protected float rotation;
	
	protected Map map;

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
	
}
