package Model.Inhabitants;

import static Utils.Const.CellConst.*;
import static Utils.Const.SimConst.*;
import static Utils.Geometry.distance2;
import static Utils.Geometry.rotatePoint;
import static Utils.Geometry.upAngle;

import java.util.Comparator;
import java.util.Random;

import Model.NeuralNet;
import Model.Oddworld.Cell;
import Model.Oddworld.Map;
import Utils.JsonConverter;

public class Scrab extends MovingBody implements JsonConverter {
	
	private NeuralNet brain;
	private double fitness;
	
	private Paramite food;
	
	public Scrab(Map map, NeuralNet parentABrain, NeuralNet parentBBrain) {
		super();
		
		//Creating a scrab at a free space
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
		this.energy = SCRAB_STARTING_ENERGY;
		this.state = IDLE_STATE;
		this.idleTime = 0;
		
		if (parentABrain == null || parentBBrain == null)
			brain = new NeuralNet(1, 2);
		else
			brain = new NeuralNet(parentABrain, parentBBrain);
		this.fitness = 0;
		this.food = null;
	}
	
	public void step() {
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
			food.state = DEAD_STATE;
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
		
		for (Paramite p : map.paramitePopulation) {
			double dist = distance2(p.getX(), p.getY(), x, y);
			if (dist <= 1 && p.state != DEAD_STATE && p.state != EATED_STATE) {
				food = p;
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
	
	public void eat(Paramite p) {
		p.state = EATED_STATE;
		energy += SCRAB_EATING_ENERGY + SCRAB_EATING_COOLDOWN;
		idleTime = SCRAB_EATING_COOLDOWN;
		state = EATING_STATE;
	}
	
	public boolean turnLeft() {
		rotation -= SCRAB_ROATION_SPEED_DEFAULT;
		if (rotation < 0)
			rotation = 360.f - rotation;
		return true;
	}
	
	public boolean turnRight() {
		rotation += SCRAB_ROATION_SPEED_DEFAULT;
		if (rotation > 360)
			rotation -= 360.f;
		return true;
	}
	
	public boolean move() {
		//try to move forward
		double theta = rotation*2*Math.PI/(double)360;
		float[] upDirection = {0.f, SCRAB_SPEED_DEFAULT};
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
		for (Paramite p : map.paramitePopulation) {
			if (p.state == EATED_STATE || p.state == DEAD_STATE)
				continue;
			if (!checkLine(map, (int)p.getX(), (int)p.getY()))
				continue;
			float angle = (float) upAngle(p.getX()-x, p.getY()-y);
			float localRot = (float) (rotation*2*Math.PI/360.f);
			angle = angle- localRot;
			if (angle < -Math.PI)
				angle += 2*Math.PI;
			if (angle > Math.PI)
				angle -= 2*Math.PI;
			angle /= (2*Math.PI);
			float[] brainInputs = new float[1];
			brainInputs[0] = angle;
			brain.compute(brainInputs);
			break;
		}
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
