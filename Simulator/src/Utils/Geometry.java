package Utils;

public class Geometry {
	
	public static float[] rotatePoint(float[] p, double theta) {
		float newX = (float) (p[0]*Math.cos(theta) + p[1]*Math.sin(theta));
		float newY = (float) (p[1]*Math.cos(theta) - p[0]*Math.sin(theta));
		float[] newP = {newX, newY};
		return newP;
	}

}
