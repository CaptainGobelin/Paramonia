package Controller;

import static Utils.SimConst.*;
import static Utils.GraphicsConst.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import Model.Inhabitants.Paramite;
import Model.Oddworld.Map;
import Network.Client;
import View.ConsoleView;
import View.MainView;

public class MainController {
	
	public MainView window;
	public ConsoleView console;
	
	private Map map;
	
	public MainController() {
		System.out.print("Opening terminal... ");
		console = new ConsoleView(APP_NAME + " - Results");
		System.out.println("done.");
		if (!SERVER_MODE) {
			System.out.print("Opening window... ");
			window = new MainView(this, APP_NAME);
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
			console.writeln(map.toJSON());
			System.out.println("done.");
			
			window.run();
		}
		else {
			console.write("Connection... ");
			try {
				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
				console.writeln("done.");
				console.writeln("Wait client... ");
                Socket socket = serverSocket.accept();
                console.writeln("Client connected");
                
                console.write("Generating map... ");
    			map = new Map(GRID_WIDTH, GRID_HEIGHT, 1);
    			map.generate();
    			console.writeln("done.");

				Client client = new Client (this, socket);
				
				long timeOffset = (long) (1000.f/FPS);
				while (true) {
					long time = System.currentTimeMillis();
					
					step();
					client.sendCreatures();
					
					long spentTime = System.currentTimeMillis() - time;
					if (spentTime < timeOffset)
						TimeUnit.MILLISECONDS.sleep(timeOffset - spentTime);
				}
			} catch (IOException e) {
				console.writeln("FAILED.");
				e.printStackTrace();
			} catch (InterruptedException e) {
				console.write("Step interrupted.");
				e.printStackTrace();
			}
		}
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
