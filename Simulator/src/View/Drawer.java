package View;

import org.lwjgl.opengl.GL11;

public abstract class Drawer {
	
	public void drawRect() {
		 
        // set the color of the quad (R,G,B,A)
        GL11.glColor4f(1.0f, 0.1f, 0.1f, 0.5f);
           
        // draw quad
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(100,100);
        GL11.glVertex2f(100+200,100);
        GL11.glVertex2f(100+200,100+200);
        GL11.glVertex2f(100,100+200);
        GL11.glEnd();
         
    }

}
