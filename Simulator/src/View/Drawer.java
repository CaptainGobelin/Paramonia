package View;

import static org.lwjgl.opengl.GL11.*;

public abstract class Drawer {
	
	public void drawRect(float x, float y, float sizeX, float sizeY) {
		glBegin(GL_POLYGON);
        glColor3f(0.2f, 0.2f, 0.2f);
        glVertex3f(x, y, 0.f);
        glVertex3f(x+sizeX, y, 0.f);
        glVertex3f(x+sizeX, y+sizeY, 0.f);
        glVertex3f(x, y+sizeY, 0.f);
        glEnd();
    }

}
