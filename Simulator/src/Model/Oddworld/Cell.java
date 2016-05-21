package Model.Oddworld;

import static Utils.Const.CellConst.*;

public class Cell {
	
	private int x;
	private int y;
	private int state;
	private boolean viewed;
	private boolean spooced;
	
	public Cell(int x, int y) {
		this.state = FREE_STATE;
		this.viewed = false;
		this.spooced = false;
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setState(int value) {
		this.state = value;
	}
	
	public int getState() {
		return state;
	}
	
	public boolean isViewed() {
		return this.viewed;
	}
	
	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}
	
	public boolean isSpooced() {
		return this.spooced;
	}
	
	public void setSpooced(boolean spooced) {
		this.spooced = spooced;
	}

}
