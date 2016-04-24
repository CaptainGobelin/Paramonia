package Utils;

public class Array {
	
	public static boolean outOfBounds2(int x, int y, Object[][] array) {
		if ((x < 0) || (y < 0))
			return true;
		if ((x >= array.length) || (y >= array[0].length))
			return true;
		return false;
	}

}
