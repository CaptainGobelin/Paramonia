package View;

import Model.Map;

public class MapDrawer extends Drawer {
	
	private MainView window;
	
	public MapDrawer(MainView window) {
		this.window = window;
	}
	
	public void draw(Map map) {
		drawRect();
	}

}
