package View;

import static org.lwjgl.opengl.GL11.*;
import static Utils.Const.GraphicsConst.Color;
import static Utils.Geometry.*;

public abstract class Drawer {
	
	public void setColor(Color color) {
		switch (color) {
		case BLACK: glColor3f(0.0f, 0.0f, 0.0f); break;
		case WHITE: glColor4f(1.f, 1.f, 1.f, 0.1f); break;
		case GREY: glColor3f(0.34f, 0.34f, 0.34f); break;
		case GREEN: glColor3f(0.38f, 0.4f, 0.184f); break;
		case GREEN_2: glColor3f(0.43f, 0.475f, 0.235f); break;
		case GREEN_3: glColor3f(0.518f, 0.565f, 0.35f); break;
		case BROWN: glColor3f(0.52f, 0.443f, 0.27f); break;
		case RED: glColor3f(0.63f, 0.16f, 0.18f); break;
		case GRUE: glColor3f(0.133f, 0.447f, 0.38f); break;
		case BLUE: glColor3f(0.33f, 0.53f, 0.42f); break;
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

}
