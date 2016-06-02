package Model.Inhabitants;

import static Utils.ID.createID;

import Utils.JsonConverter;

public class Spooce implements JsonConverter {
	
	private String id;

	private int x;
	private int y;
	
	public Spooce(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.id = createID();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public String toJSON() {
		return "{id: " + id + ", x: " + x + ", y: " + y + "}";
	}
	
}
