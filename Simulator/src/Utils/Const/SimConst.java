package Utils.Const;

public class SimConst {
	
	//General informations
	public static final String APP_NAME = "Paramonia";
	public static final boolean SERVER_MODE = true;
	public static final int PORT_NUMBER = 12345;
	public static final int RAS_SIGNAL = 1;
	public static final int STOP_SIGNAL = 2;
	public static final int NORMAL_SPEED_SIGNAL = 3;
	public static final int FASTER_SIGNAL = 4;
	public static final int BREAK_SIGNAL = 5;
	
	//Map generation pattern
	public static final int GRID_WIDTH = 100;
	public static final int GRID_HEIGHT = 100;
	
	public static final int BLOC_PERCENT = 15;
	public static final int CELL_SWITCH_LIMIT = 5;
	public static final int LOOP_GEN = 3;
	
	public static final float MAX_VEGETABLE_RATE = 0.05f;
	public static final int STARTING_PARAMITE_NB = 30;
	public static final int STARTING_SCRAB_NB = 6;
	
	public static final int MAX_TRIES_FLOOD = 100;
	public static final double LAKES_PERCENT = 6e-4;
	
	public static final float CONVERT_GRASS = 0.95f;
	public static final double TO_CONVERT = 2;
	
	//Creatures
	public static final int DIST_VIEW = 10;
	public static final float NEURAL_THRESHOLD = 0.1f;
	public static final float MUTATION_RATE = 0.1f;
	
	public static final double SPOOCE_GROWING_RATE = 2.2e-3;
	
	public static final float PARAMITE_SPEED_DEFAULT = 0.05f;
	public static final float PARAMITE_ROATION_SPEED_DEFAULT = 10.0f;
	public static final int PARAMITE_EYES_NUMBER = 3;
	public static final double PARAMITE_STARTING_ENERGY = 10000;
	public static final double PARAMITE_EATING_ENERGY = 500;
	public static final int PARAMITE_EATING_COOLDOWN = 75;
	
	public static final float SCRAB_SPEED_DEFAULT = 0.065f;
	public static final float SCRAB_ROATION_SPEED_DEFAULT = 10.0f;
	public static final int SCRAB_EYES_NUMBER = 3;
	public static final double SCRAB_STARTING_ENERGY = 10000;
	public static final double SCRAB_EATING_ENERGY = 5000;
	public static final int SCRAB_EATING_COOLDOWN = 75;
	
	//Creatures states
	public static final int IDLE_STATE = 0;
	public static final int WALKING_STATE = 1;
	public static final int DONT_DISTURB_STATE = 100;
	public static final int DEAD_STATE = 101;
	public static final int EATING_STATE = 102;
	public static final int EATED_STATE = 103;

}
