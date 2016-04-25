package Model.Oddworld;

public class Cell {
	
	private CellState state;
	private boolean viewed;
	
	public Cell() {
		this.state = new CellState();
		this.viewed = false;
	}
	
	public void setState(int value) {
		this.state.setValue(value);
	}
	
	public int getState() {
		return state.getValue();
	}
	
	public boolean isViewed() {
		return this.viewed;
	}
	
	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

}
