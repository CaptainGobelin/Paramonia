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
		int hiddenSize = /*rand.nextInt*/(inSize*2) + inSize;
		this.inputPlage = inSize;
		this.outputPlage = inSize + hiddenSize;
		nodes = new float[inSize+outSize+hiddenSize];
		this.nodesLength = nodes.length;
		
		/*int linksSize = rand.nextInt(inSize*hiddenSize);
		for (int i=0;i<linksSize;i++) {
			int in = rand.nextInt(inSize);
			int out = rand.nextInt(hiddenSize) + inputPlage;
			if (linkIsNotTaken(in, out))
				links.add(new NeuralConnection(in, out, rand.nextFloat()*2 - 1));
		}
		linksSize = 1 + rand.nextInt(outSize*hiddenSize);
		for (int i=0;i<linksSize;i++) {
			int in = rand.nextInt(hiddenSize) + inputPlage;
			int out = rand.nextInt(outSize) + outputPlage;
			if (linkIsNotTaken(in, out))
				links.add(new NeuralConnection(in, out, rand.nextFloat()*2 - 1));
		}
		sortLinks();*/
		for (int i=0;i<inSize;i++)
			for (int j=inputPlage;j<outputPlage;j++) {
				links.add(new NeuralConnection(i, j, rand.nextFloat()*2 - 1));
			}
		for (int i=inputPlage;i<outputPlage;i++)
			for (int j=outputPlage;j<nodes.length;j++) {
				links.add(new NeuralConnection(i, j, rand.nextFloat()*2 - 1));
			}
	}
	
	public NeuralNet(NeuralNet parentA, NeuralNet parentB) {
		Random rand = new Random();
		this.inputPlage = parentA.inputPlage;
		this.outputPlage = parentA.outputPlage;
		this.nodesLength = parentA.nodesLength;
		nodes = new float[nodesLength];
		int inSize = parentA.inputPlage;
		int outSize = parentA.nodesLength - parentA.outputPlage;
		int hiddenSize = parentA.nodesLength - inSize - outSize;
		
		ArrayList<NeuralConnection> list = parentA.links;
		int i = 0;
		while (i < list.size()) {
			NeuralConnection nc = list.get(i);
			/*if (rand.nextFloat() <= MUTATION_RATE)
				continue;*/
			float w = nc.weight;
			if (rand.nextFloat() <= MUTATION_RATE)
				w = rand.nextFloat()*2 - 1;
			this.links.add(new NeuralConnection(nc.in, nc.out, w));
			if (rand.nextFloat() <= 1/(1+(parentA.links.size()/2)))
				list = parentB.links;
			i++;
		}
		/*while (rand.nextFloat() <= (MUTATION_RATE/2)) {
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
		}*/
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
