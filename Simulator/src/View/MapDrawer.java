package View;

import Model.Inhabitants.Paramite;
import Model.Oddworld.Map;
import static Utils.CellConst.*;
import static Utils.GraphicsConst.*;

public class MapDrawer extends Drawer {
	
	@SuppressWarnings("unused")
	private MainView window;
	
	public MapDrawer(MainView window) {
		this.window = window;
	}
	
	public void draw(Map map) {
		int s = map.getCellSize();
		for (int i=0;i<map.getWidth();i++)
			for (int j=0;j<map.getHeight();j++) {
				if (map.getGrid()[i][j].getState() == BLOC_STATE)
					drawRect(i*s, j*s, s, s, Color.GREY);
				else if (map.getGrid()[i][j].getState() == SPOOCE_STATE)
					drawRect(i*s, j*s, s, s, Color.GRUE);
				else if (map.getGrid()[i][j].getState() == WATER_STATE)
					drawRect(i*s, j*s, s, s, Color.BLUE);
				else
					drawRect(i*s, j*s, s, s, Color.GREEN_2);
			}
		for (Paramite p : map.paramitePopulation) {
			drawTriangle(p.getRotation(), p.getX()*s, p.getY()*s, s+1, s+1, Color.RED);
		}
	}

}
