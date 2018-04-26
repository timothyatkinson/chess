package nets;

import java.util.ArrayList;
import java.util.Random;

import nets.functions.logistic;

public class Network {

	public static Random r = new Random();
	public double min;
	public double max;
	
	public static double randDouble(double min, double max){
		double diff = max - min;
		return min + (r.nextDouble() * diff);
	}
	
	public static Node randNode(ArrayList<Node> nodes){
		int i = r.nextInt(nodes.size());
		return nodes.get(i);
	}
	
	public static Network randNetwork(int inputs, int outputs, int nodes, int ffcon, int reccon, double min, double max){
		Network n = new Network(inputs, outputs);
		ArrayList<Node> neurons = new ArrayList<Node>();
		
		for(int i = 0; i < nodes; i++){
			Neuron neuron = new Neuron(new logistic());
			for(int j = 0; j < ffcon; j++){
				neuron.addInput(randNode(n.targetNodes()), randDouble(min, max), false);
			}
			neuron.randomizeBias(min, max);
			neuron.randomizeInitValue(min, max);
			n.addNode(neuron);
			neurons.add(neuron);
		}
		
		for(int i = 0; i < nodes; i++){
			for(int j = 0; j < reccon; j++){
				neurons.get(i).addInput(randNode(n.targetNodes()), randDouble(min, max), true);
			}
		}
		
		n.min = min;
		n.max = max;
		
		for(int i = 0; i < outputs; i++){
			n.outputs.get(i).addInput(randNode(n.targetNodes()), 1.0, false);
		}
		
		neurons.clear();
		
		return n;
	}
	
	public void resetValues(){
		for(Node n : nodes){
			n.reset();
		}
	}
	

	
	public static Network mutateNetwork(Network net, double const_rate, double conn_rate){
		Network new_net = null;
		try {
			new_net = net.copy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<Connection> toConsider = new_net.getAllConnections();
		
		//Shuffle in a RANDOM order
		while(!toConsider.isEmpty()){
			Connection c = toConsider.remove(r.nextInt(toConsider.size()));
			if(r.nextDouble() <= conn_rate){
				ArrayList<Node> pot;
				if(c.feedback){
					pot = new_net.getValidInputs(c);
				}
				else{
					pot = new_net.getAcyclicInputs(c);
				}
				if(!pot.isEmpty()){
					c.moveTarget(randNode(pot));
				}
			}
			if(r.nextDouble() <= const_rate && !c.source.getClass().equals(Output.class)){
				c.randomizeWeight(new_net.min, new_net.max);
			}
		}
		
		for(Node n : new_net.nodes){
			n.randomize(const_rate, new_net.min, new_net.max);
		}
		
		return new_net;
	}
	
	
	public double[] tick(double[] in) throws Exception{
		
		for(Node node : nodes){
			node.resetReady();
			node.updateOldValue();
		}
		
		ArrayList<Node> queue = new ArrayList<Node>();
		ArrayList<Node> done = new ArrayList<Node>();
		for(int i = 0; i < inputs.size(); i++){
			inputs.get(i).setValue(in[i]);
			queue.add(inputs.get(i));
		}
		
		while(!queue.isEmpty()){
			Node nextNode = queue.remove(0);
			if(!done.contains(nextNode)){
				ArrayList<Node> newNodes = nextNode.update_node();
				for(Node n : newNodes){
					queue.add(n);
				}
				done.add(nextNode);
			}
		}
		
		double[] out = new double[outputs.size()];
		for(int i = 0; i < outputs.size(); i++){
			out[i] = outputs.get(i).newValue;
		}
		
		return out;
	}
	
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public ArrayList<Input> inputs = new ArrayList<Input>();
	public ArrayList<Output> outputs = new ArrayList<Output>();
	
	public Network(int in, int out){
		for(int i = 0; i < in; i++){
			addNewInput();
		}
		for(int i = 0; i < out; i++){
			addNewOutput();
		}
	}
	
	public void addNewInput(){
		Input n = new Input();
		nodes.add(n);
		inputs.add(n);
	}
	
	public void addNewOutput(){
		Output n = new Output();
		nodes.add(n);
		outputs.add(n);
	}
	
	public ArrayList<Connection> getAllConnections(){
		ArrayList<Connection> c = new ArrayList<Connection>();
		for(Node n : nodes){
			for(Connection con : n.inputConnections){
				c.add(con);
			}
		}
		return c;
	}
	
	//Gets the possible nodes a mutating connection can target
	public ArrayList<Node> getValidInputs(Connection c){
		ArrayList<Node> n = new ArrayList<Node>();
		for(Node node : nodes){
			if(node.getClass() != Output.class && !node.equals(c.target)){
				n.add(node);
			}
		}
		return n;
	}
	
	public ArrayList<Node> getAcyclicInputs(Connection c){
		ArrayList<Node> n = getValidInputs(c);
		
		ArrayList<Node> done = new ArrayList<Node>();
		
		ArrayList<Node> toDo = new ArrayList<Node>();
		
		done.add(c.source);
		for(Connection con : c.source.outputConnections){
			toDo.add(con.source);
		}
		n.remove(c.source);
		
		while(!toDo.isEmpty()){
			Node node = toDo.remove(0);
			if(!done.contains(node) && !node.getClass().equals(Output.class)){
				done.add(node);
				n.remove(node);
				for(Connection con : node.outputConnections){
					toDo.add(con.source);
				}
			}
		}	
		
		return n;
	}
	
	public void addNode(Node n){
		nodes.add(n);
	}
	
	public ArrayList<Node> targetNodes(){
		ArrayList<Node> n = new ArrayList<Node>();
		for(Node node : nodes){
			if(node.getClass() != Output.class){
				n.add(node);
			}
		}
		return n;
	}
	
	public int countInputs(){
		return this.inputs.size();
	}
	
	public int countOutputs(){
		return this.outputs.size();
	}
	
	public int countNodes(){
		return this.nodes.size();
	}
	
	public int countANodes(){
		return countNodes() - (countInputs() + countOutputs());
	}
	
	public int getNodeIndex(Node n){
		return nodes.indexOf(n);
	}
	
	public Network copy() throws Exception{
		Network n = new Network(this.countInputs(), this.countOutputs());
		
		Node[] map_source = new Node[this.countNodes()];
		Node[] map_target = new Node[this.countNodes()];
		
		//Map inputs and outputs
		for(int i = 0; i < this.countInputs() + this.countOutputs(); i++){
			map_target[i] = n.nodes.get(i);
			map_source[i] = this.nodes.get(i);
		}
		
		//Add all nodes
		for(int i = 0; i < this.countANodes(); i++){
			int index = this.countInputs() + this.countOutputs() + i;
			Node new_node = this.nodes.get(index).copy();
			n.addNode(new_node);
			map_target[index] = new_node;
			map_source[index] = this.nodes.get(index);
		}
		
		//Copy all connections
		ArrayList<Connection> conns = this.getAllConnections();
		for(Connection c : conns){
			int source = getNodeIndex(c.source);
			int target = getNodeIndex(c.target);
			
			//Check
			if(!map_source[source].equals(c.source) || !map_source[target].equals(c.target)){
				throw new Exception("Node mappings are wrong!");
			}
			
			map_target[source].addInput(map_target[target], c.weight, c.feedback);
		}		
		n.min = this.min;
		n.max = this.max;
		
		return n;
	}
	
	public void print(){
		System.out.println("Network with " + inputs.size() + " inputs, " + countANodes() + " nodes, " + outputs.size() + " outputs.");
		System.out.println("Inputs:");
		for(Node n : inputs){
			printNode(n);
		}
		System.out.println("Outputs:");
		for(Node n : outputs){
			printNode(n);
		}
		System.out.println("Nodes: ");
		for(Node n : nodes){
			if(n.getClass() != Output.class && n.getClass() != Input.class){
				printNode(n);
			}
		}
	}
	
	private void printNode(Node node){
		int index = getNodeIndex(node);
		System.out.println("(" + index + ") " + node.toString() + " with " + node.inputConnections.size() + " inputs, " + node.outputConnections.size() + " outputs:");
		if(node.inputConnections.size() > 0){
			System.out.println("   Input Connections:");
			for(int i = 0; i < node.inputConnections.size(); i++){
				System.out.println("      ->(" + this.getNodeIndex(node.inputConnections.get(i).target) + ") [" + node.inputConnections.get(i).weight + "] feedback? " + node.inputConnections.get(i).feedback);
			}
		}
		if(node.outputConnections.size() > 0){
			System.out.println("   Output Connections:");
			for(int i = 0; i < node.outputConnections.size(); i++){
				System.out.println("      <-(" + this.getNodeIndex(node.outputConnections.get(i).source) + ") [" + node.outputConnections.get(i).weight + "] feedback? " + node.outputConnections.get(i).feedback);
			}
		}
	}
}
