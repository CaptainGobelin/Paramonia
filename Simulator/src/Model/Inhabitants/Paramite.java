package Model.Inhabitants;

import java.util.Comparator;
import java.util.Random;

import Model.NeuralNet;
import Model.Oddworld.Cell;
import Model.Oddworld.Map;
import Utils.JsonConverter;

import static Utils.Const.CellConst.*;
import static Utils.Const.SimConst.*;
import static Utils.Geometry.*;

public class Paramite extends MovingBody implements JsonConverter {
	
	private NeuralNet brain;
	private double fitness;
	
	private Spooce food;
	
	public Paramite(Map map, NeuralNet parentABrain, NeuralNet parentBBrain) {
		super();
		
		//Creating a paramite at a free space
		Random rand = new Random();
		do {
			x = rand.nextInt(map.getWidth()-1);
			y = rand.nextInt(map.getHeight()-1);
		} while (map.getGrid()[(int) x][(int) y].getState() >= BLOC_STATE);
		//Placing it at the middle of the cell
		x += 0.5f;
		y += 0.5f;
		//Put a random rotation
		rotation = rand.nextInt(360);
		this.map = map;
		this.energy = PARAMITE_STARTING_ENERGY;
		this.state = IDLE_STATE;
		this.idleTime = 0;
		
		if (parentABrain == null || parentBBrain == null)
			brain = new NeuralNet(2, 2);
		else
			brain = new NeuralNet(parentABrain, parentBBrain);
		this.fitness = 0;
		this.food = null;
	}
	
	public void step() {
		if (state == EATED_STATE)
			return;
		if (energy <= 0)
			die();
		else {
			energy--;
			fitness++;
		}
		if (idleTime > 0) {
			idleTime--;
		}
		else if (state == EATING_STATE) {
			food = null;
			state = IDLE_STATE;
		}
		if (state >= DONT_DISTURB_STATE)
			return;
		state = IDLE_STATE;
		//checkView(map);
		checkView();
		if (brain.nodes[brain.nodes.length-2] >= NEURAL_THRESHOLD) {
			turnLeft();
		}
		if (brain.nodes[brain.nodes.length-1] >= NEURAL_THRESHOLD) {
			turnRight();
		}
		//if (brain.nodes[brain.nodes.length-3] >= NEURAL_THRESHOLD) {
			//Move back if cannot move forward
			if (!move())
				rotation += 180;
			state = WALKING_STATE;
		//}
		
		for (Spooce s : map.spoocePopulation) {
			double dist = distance2(s.getX(), s.getY(), x, y);
			if (dist <= 1) {
				food = s;
				break;
			}
		}
		if (food != null) {
			eat(food);
		}
	}
	
	public void die() {
		state = DEAD_STATE;
	}
	
	public void eat(Spooce s) {
		map.removeSpooce(s);
		map.spoocePopulation.remove(s);
		map.grow();
		energy += PARAMITE_EATING_ENERGY + PARAMITE_EATING_COOLDOWN;
		idleTime = PARAMITE_EATING_COOLDOWN;
		state = EATING_STATE;
	}
	
	public boolean turnLeft() {
		rotation -= PARAMITE_ROATION_SPEED_DEFAULT;
		if (rotation < 0)
			rotation = 360.f - rotation;
		return true;
	}
	
	public boolean turnRight() {
		rotation += PARAMITE_ROATION_SPEED_DEFAULT;
		if (rotation > 360)
			rotation -= 360.f;
		return true;
	}
	
	public boolean move() {
		//try to move forward
		double theta = rotation*2*Math.PI/(double)360;
		float[] upDirection = {0.f, PARAMITE_SPEED_DEFAULT};
		float[] movment = rotatePoint(upDirection, theta);
		x += movment[0];
		y += movment[1];
		//If there's an obstacle, cancel the movement
		for (Cell c : getCollidedCells(map)) {
			if (c.getState() >= BLOC_STATE) {
				x -= movment[0];
				y -= movment[1];
				return false;
			}
		}
		return true;
	}
	
	private void checkView() {
		map.spoocePopulation.sort(new Comparator<Spooce>() {

			@Override
			public int compare(Spooce o1, Spooce o2) {
				double dist1 = distance2(o1.getX(), o1.getY(), x, y);
				double dist2 = distance2(o2.getX(), o2.getY(), x, y);
				return Double.compare(dist1, dist2);
			}
		});
		float[] brainInputs = new float[2];
		for (Spooce s : map.spoocePopulation) {
			if (!checkLine(map, s.getX(), s.getY()))
				continue;
			float angle = (float) upAngle(s.getX()-x, s.getY()-y);
			float localRot = (float) (rotation*2*Math.PI/360.f);
			angle = angle- localRot;
			if (angle < -Math.PI)
				angle += 2*Math.PI;
			if (angle > Math.PI)
				angle -= 2*Math.PI;
			angle /= (2*Math.PI);
			brainInputs[0] = angle;
			break;
		}
		for (Scrab s : map.scrabPopulation) {
			if (!checkLine(map, (int)s.getX(), (int)s.getY()))
				continue;
			float angle = (float) upAngle(s.getX()-x, s.getY()-y);
			float localRot = (float) (rotation*2*Math.PI/360.f);
			angle = angle- localRot;
			if (angle < -Math.PI)
				angle += 2*Math.PI;
			if (angle > Math.PI)
				angle -= 2*Math.PI;
			angle /= (2*Math.PI);
			brainInputs[1] = angle;
			break;
		}
		brain.compute(brainInputs);
		/*double[] objectsInSight = new double[PARAMITE_EYES_NUMBER + 2];
		for (int i=0;i<objectsInSight.length;i++)
			objectsInSight[i] = DIST_VIEW;
		for (Spooce s : map.spoocePopulation) {
			if (!map.getGrid()[s.getX()][s.getY()].isViewed())
				continue;
			double dist = distance2(s.getX(), s.getY(), x, y);
			if (dist > DIST_VIEW)
				continue;
			int zone = 0;
			double angle = upAngle(s.getX()-x, s.getY()-y);
			double localRot = rotation*2*Math.PI/360.f;
			angle = angle- localRot;
			if (angle < -Math.PI)
				angle += 2*Math.PI;
			if (angle > Math.PI)
				angle -= 2*Math.PI;
			if (angle < Math.PI/2) {
				zone = PARAMITE_EYES_NUMBER+1;
			}
			else if (angle < Math.PI) {
				zone = (int) (angle/(Math.PI/PARAMITE_EYES_NUMBER))-1;
			}
			else
				zone = PARAMITE_EYES_NUMBER;
				
			if (dist > objectsInSight[zone])
				continue;
			objectsInSight[zone] = dist;
		}
		float[] brainInputs = new float[(PARAMITE_EYES_NUMBER+2)*3];
		for (int i=0;i<objectsInSight.length;i++) {
			if (objectsInSight[i] == DIST_VIEW)
				continue;
			brainInputs[i*3 + 0] = .4f;
			brainInputs[i*3 + 1] = .5f;
			brainInputs[i*3 + 2] = .2f;
		}
		brain.compute(brainInputs);*/
	}
	
	public double getFitness() {
		return this.fitness;
	}
	
	public NeuralNet getBrain() {
		return this.brain;
	}

	@Override
	public String toJSON() {
		return "{id: " + id + ", x: " + x + ", y: " + y + ", st: " + state + "}";
	}

}
