package View;

import static org.lwjgl.opengl.GL11.*;

import Utils.GraphicsConst.Color;

public abstract class Drawer {
	
	public void setColor(Color color) {
		switch (color) {
		case BLACK: glColor3f(0.0f, 0.0f, 0.0f); break;
		case WHITE: glColor3f(1.f, 1.f, 1.f); break;
		case GREY: glColor3f(0.3f, 0.3f, 0.3f); break;
		case GREEN: glColor3f(0.1f, 0.5f, 0.1f); break;
		case BROWN: glColor3f(0.7f, 0.35f, 0.1f); break;
		default: glColor3f(1.f, 1.f, 1.f);
		}
	}
	
	public void drawTriangle(float rotation, float x, float y, float sizeX, float sizeY, Color color) {
		double theta = rotation*2*Math.PI/(double)360;
		float[] p1 = {-sizeX/2, -sizeY/2};
		float[] p2 = {sizeX/2, -sizeY/2};
		float[] p3 = {0, sizeY/2};
		float[] newP1 = rotatePoint(p1, theta);
		float[] newP2 = rotatePoint(p2, theta);
		float[] newP3 = rotatePoint(p3, theta);
		glBegin(GL_TRIANGLES);
        setColor(color);
        glVertex3f(x+newP1[0], y+newP1[1], 0.f);
        glVertex3f(x+newP2[0], y+newP2[1], 0.f);
        glVertex3f(x+newP3[0], y+newP3[1], 0.f);
        glEnd();
	}
	
	public void drawRect(float x, float y, float sizeX, float sizeY, Color color) {
		glBegin(GL_POLYGON);
        setColor(color);
        glVertex3f(x, y, 0.f);
        glVertex3f(x+sizeX, y, 0.f);
        glVertex3f(x+sizeX, y+sizeY, 0.f);
        glVertex3f(x, y+sizeY, 0.f);
        glEnd();
    }
	
	public float[] rotatePoint(float[] p, double theta) {
		float newX = (float) (p[0]*Math.cos(theta) + p[1]*Math.sin(theta));
		float newY = (float) (p[1]*Math.cos(theta) - p[0]*Math.sin(theta));
		float[] newP = {newX, newY};
		return newP;
	}

}
