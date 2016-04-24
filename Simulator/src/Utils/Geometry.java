package Utils;

public class Geometry {
	
	public static float[] rotatePoint(float[] p, double theta) {
		float newX = (float) (p[0]*Math.cos(theta) + p[1]*Math.sin(theta));
		float newY = (float) (p[1]*Math.cos(theta) - p[0]*Math.sin(theta));
		float[] newP = {newX, newY};
		return newP;
	}
	
	public static double distance2(float x1, float y1, float x2, float y2) {
		double xd = x2 - x1;
		double yd = y2 - y1;
		return Math.sqrt(xd*xd + yd*yd);
	}

}
