package Model;

import static Utils.CellConst.*;

import java.util.Random;

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
