package Controller;

import static Utils.Const.SimConst.APP_NAME;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Launch {
	 
	    public static void main(String[] args) {
	    	String[] version = System.getProperty("java.version").split("[.]+");
	    	if (Integer.parseInt(version[1]) < 8) {
		    	JFrame frame = new JFrame();
		    	JOptionPane.showMessageDialog(frame, "Sorry, "+APP_NAME+" only works with Java 8 or newer");
		    	return;
	    	}
	        @SuppressWarnings("unused")
			MainController controller = new MainController();
	    }
	
}
