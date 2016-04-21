package Controller;

import Model.Map;
import View.MainView;

public class MainController {
	
	public MainView window;
	
	private Map map;
	
	public MainController() {
		window = new MainView(this, "Paranomia");
		map = new Map(128, 72, 5);
		
		window.run();
	}
	
	public void step() {
		
	}
	
	public Map getMap() {
		return this.map;
	}

}
