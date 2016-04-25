package Model.Oddworld;

import static Utils.CellConst.*;

public class Cell {
	
	private int state;
	private boolean viewed;
	
	public Cell() {
		this.state = FREE_STATE;
		this.viewed = false;
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

}
