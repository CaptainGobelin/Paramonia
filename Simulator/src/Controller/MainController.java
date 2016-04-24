package Controller;

import static Utils.SimConst.*;

import Model.Inhabitants.Paramite;
import Model.Oddworld.Map;
import View.MainView;

public class MainController {
	
	public MainView window;
	
	private Map map;
	
	public MainController() {
		System.out.print("Opening window... ");
		window = new MainView(this, "Paranomia");
		System.out.println("done.");
		
		//Resolution compute the size of each cell to have a specific number
		//of cell in a specific sized window
		int resolution = window.getWidth()/GRID_WIDTH;
		if (window.getWidth()%GRID_WIDTH != 0)
			System.out.println("Warning: resolution incorrect !");
		if (resolution != window.getHeight()/GRID_HEIGHT)
			System.out.println("Warning: width resolution != height resolution !");
		
		System.out.print("Generating map... ");
		map = new Map(GRID_WIDTH, GRID_HEIGHT, resolution);
		map.generate();
		System.out.println("done.");
		
		window.run();
	}
	
	public void step() {
		map.grow();
		for (Paramite p : map.paramitePopulation) {
			p.step();
		}
	}
	
	public void generateNewMap() {
		map.generate();
	}
	
	public Map getMap() {
		return this.map;
	}
}
