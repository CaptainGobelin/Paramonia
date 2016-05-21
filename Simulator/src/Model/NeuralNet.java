package Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static Utils.Const.SimConst.*;

public class NeuralNet {
	
	public float[] nodes;
	public ArrayList<NeuralConnection> links = new ArrayList<NeuralConnection>();
	private int inputPlage;
	private int outputPlage;
	private int nodesLength;
	
	public NeuralNet(int inSize, int outSize) {
		Random rand = new Random();
		int hiddenSize = rand.nextInt(inSize*2) + inSize;
		this.inputPlage = inSize;
		this.outputPlage = inSize + hiddenSize;
		nodes = new float[inSize+outSize+hiddenSize];
		this.nodesLength = nodes.length;
		
		int linksSize = rand.nextInt(inSize*hiddenSize);
		for (int i=0;i<linksSize;i++) {
			int in = rand.nextInt(inSize);
			int out = rand.nextInt(hiddenSize) + inputPlage;
			if (linkIsNotTaken(in, out))
				links.add(new NeuralConnection(in, out, rand.nextFloat()*2 - 1));
		}
		linksSize = rand.nextInt(outSize*hiddenSize);
		for (int i=0;i<linksSize;i++) {
			int in = rand.nextInt(hiddenSize) + inputPlage;
			int out = rand.nextInt(outSize) + outputPlage;
			if (linkIsNotTaken(in, out))
				links.add(new NeuralConnection(in, out, rand.nextFloat()*2 - 1));
		}
		sortLinks();
	}
	
	public NeuralNet(NeuralNet parent) {
		Random rand = new Random();
		this.inputPlage = parent.inputPlage;
		this.outputPlage = parent.outputPlage;
		this.nodesLength = parent.nodesLength;
		nodes = new float[nodesLength];
		int inSize = parent.inputPlage;
		int outSize = parent.nodesLength - parent.outputPlage;
		int hiddenSize = parent.nodesLength - inSize - outSize;
		
		for (NeuralConnection nc : parent.links) {
			if (rand.nextFloat() <= MUTATION_RATE)
				continue;
			float w = nc.weight;
			if (rand.nextFloat() <= MUTATION_RATE)
				w = rand.nextFloat()*2 - 1;
			this.links.add(new NeuralConnection(nc.in, nc.out, w));
		}
		while (rand.nextFloat() <= (MUTATION_RATE/2)) {
			int in = rand.nextInt(inSize);
			int out = rand.nextInt(hiddenSize) + inputPlage;
			if (linkIsNotTaken(in, out))
				links.add(new NeuralConnection(in, out, rand.nextFloat()*2 - 1));
		}
		while (rand.nextFloat() <= (MUTATION_RATE/2)) {
			int in = rand.nextInt(hiddenSize) + inputPlage;
			int out = rand.nextInt(outSize) + outputPlage;
			if (linkIsNotTaken(in, out))
				links.add(new NeuralConnection(in, out, rand.nextFloat()*2 - 1));
		}
	}
	
	public void compute(float[] inputs) {
		nodes = new float[nodesLength];
		for (int i=0;i<inputPlage;i++)
			nodes[i] = inputs[i];
		for (NeuralConnection nc : links) {
			nodes[nc.out] += nc.weight*nodes[nc.in];
		}
	}
	
	private boolean linkIsNotTaken(int in, int out) {
		for (NeuralConnection nc : links)
			if (nc.in == in && nc.out == out)
				return false;
		return true;
	}
	
	private void sortLinks() {
		links.sort(new Comparator<NeuralConnection>() {
			@Override
			public int compare(NeuralConnection o1, NeuralConnection o2) {
				return Integer.compare(o1.out, o2.out);
			}
		});
	}
	
	public String print() {
		String result = "";
		/*for (NeuralConnection nc : links) {
			result += ("In: " + nc.in + ", Out: " + nc.out + ", weight: " + nc.weight + '\n');
		}*/
		for (int i=0;i<nodes.length;i++)
			result += i + ": " + nodes[i] + "; ";
		return result;
	}

}
