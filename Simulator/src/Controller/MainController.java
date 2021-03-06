package Controller;

import static Utils.Const.GraphicsConst.*;
import static Utils.Const.SimConst.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import Model.Inhabitants.Paramite;
import Model.Inhabitants.Scrab;
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
			map = new Map(this, GRID_WIDTH, GRID_HEIGHT, resolution);
			map.generate();
			System.out.println("done.");
			
			window.run();
		}
		else {
			console.write("Connection... ");
			try {
				ServerSocket serverSocket = new ServerSocket(PORT_NUMBER, 0, InetAddress.getByName(null));
				console.writeln("done.");
				console.writeln("Wait client... ");
                Socket socket = serverSocket.accept();
                console.writeln("Client connected");
                
                console.write("Generating map... ");
    			map = new Map(this, GRID_WIDTH, GRID_HEIGHT, 1);
    			map.generate();
    			console.writeln("done.");

				Client client = new Client (this, socket);
				
				long nStep = 0;
				long timeOffset = (long) (1000.f/FPS);
				while (true) {
					long time = System.currentTimeMillis();
					
					step();
					if ((nStep%500) == 0) {
						console.writeln("Step: " + nStep);
						client.sendCreatures(false);
					}
					else {
						client.sendCreatures(true);
					}
					
					long spentTime = System.currentTimeMillis() - time;
					if (spentTime < timeOffset)
						TimeUnit.MILLISECONDS.sleep(timeOffset - spentTime);
					nStep++;
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
		boolean apocalypse = true;
		for (Paramite p : map.paramitePopulation) {
			if (p.getState() != DEAD_STATE) {
				apocalypse = false;
				break;
			}
		}
		if (apocalypse) {
			console.writeln("Oh no ! Everybody's dead !");
			map.newGeneration();
		}
		for (int i=0;i<map.getWidth();i++)
			for (int j=0;j<map.getHeight();j++) {
				map.getGrid()[i][j].setViewed(false);
			}
		for (int i=0;i<SPEED;i++) {
			//map.grow();
			for (Paramite p : map.paramitePopulation) {
				p.step();
			}
			for (Scrab sc : map.scrabPopulation) {
				sc.step();
			}
		}
	}
	
	public void generateNewMap() {
		map.generate();
	}
	
	public Map getMap() {
		return this.map;
	}
}
