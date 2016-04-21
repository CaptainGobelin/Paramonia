package View;

import Model.Map;
import static Utils.CellConst.*;

public class MapDrawer extends Drawer {
	
	private MainView window;
	
	public MapDrawer(MainView window) {
		this.window = window;
	}
	
	public void draw(Map map) {
		int s = map.getCellSize();
		for (int i=0;i<map.getCellX();i++)
			for (int j=0;j<map.getCellY();j++) {
				if (map.getGrid()[i][j].getState() == BLOC_STATE)
					drawRect(i*s, j*s, s, s);
			}
	}

}
