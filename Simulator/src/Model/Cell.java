package Model;

public class Cell {
	
	private CellState state;
	
	public Cell() {
		this.state = new CellState();
	}
	
	public void setState(int value) {
		this.state.setValue(value);
	}
	
	public int getState() {
		return state.getValue();
	}

}
