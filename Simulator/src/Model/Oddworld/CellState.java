package Model.Oddworld;

import static Utils.CellConst.*;

public class CellState {
	
	private int value;
	
	public CellState() {
		this.value = FREE_STATE;
	}
	
	public CellState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
