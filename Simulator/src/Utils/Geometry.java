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
	
	public static double norm(float x, float y) {
		return distance2(0, 0, x, y);
	}
	
	public static float[] normalize(float x, float y) {
		float[] result = new float[2];
		double norm = norm(x, y);
		result[0] = (float) (x/norm);
		result[1] = (float) (y/norm);
		return result;
	}
	
	public static double dot(float[] a, float[] b) {
		return a[0]*b[0] + a[1]*b[1];
	}
	
	public static double angle(float x1, float y1, float x2, float y2) {
		return Math.acos(dot(normalize(x1, y1), normalize(x2, y2)));
	}
	
	public static double upAngle(float x, float y) {
		return Math.signum(x)*angle(x, y, 0.f, 1.f);
	}

}
