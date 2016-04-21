package Controller;

import Model.Oddworld.Map;
import View.MainView;

public class MainController {
	
	public MainView window;
	
	private Map map;
	
	public MainController() {
		window = new MainView(this, "Paranomia");
		map = new Map(128, 72, 5);
		map.generate();
		
		window.run();
	}
	
	public void step() {
		map.grow();
	}
	
	public void generateNewMap() {
		map.generate();
	}
	
	public Map getMap() {
		return this.map;
	}
}
