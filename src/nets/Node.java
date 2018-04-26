package nets;

import java.util.ArrayList;

public abstract class Node {
	double oldValue;
	double newValue;
	ArrayList<Connection> inputConnections;
	ArrayList<Boolean> readyInputs;
	ArrayList<Connection> outputConnections;
	
	public Node(){
		oldValue = 0.0;
		newValue = 0.0;
		inputConnections = new ArrayList<Connection>();
		outputConnections = new ArrayList<Connection>();
		readyInputs = new ArrayList<Boolean>();
	}

	public void resetReady(){
		for(int i = 0; i < readyInputs.size(); i++){
			readyInputs.set(i, false);
		}
	}
	
	public boolean allReady(){
		for(int i = 0; i < readyInputs.size(); i++){
			if(readyInputs.get(i) == false && !inputConnections.get(i).feedback){
				return false;
			}
		}
		return true;
	}
	
	public void addInput(Node n, double w, boolean feedback){
		Connection c = new Connection(this, n, w, feedback);
		inputConnections.add(c);
		n.outputConnections.add(c);
		readyInputs.add(false);
	}
	
	public void addOutput(Node n, double w, boolean feedback){
		n.addInput(this, w, feedback);
	}
	
	public void setValue(double value){
		this.newValue = value;
	}
	
	public void setOldValue(double value){
		this.oldValue = value;
	}
	
	public boolean notifyReady(Connection c){
		for(int i = 0; i < inputConnections.size(); i++){
			if(inputConnections.get(i).equals(c)){
				readyInputs.set(i, true);
				return true;
			}
		}
		return false;
	}
	
	public void updateOldValue(){
		oldValue = newValue;
	}
	
	public ArrayList<Node> update_node() throws Exception{
		if(allReady()){
			this.update();
			ArrayList<Node> newReady = new ArrayList<Node>();
			for(int i = 0; i < outputConnections.size(); i++){
				boolean r = outputConnections.get(i).notifySource();
				if(r && !outputConnections.get(i).feedback){
					newReady.add(outputConnections.get(i).source);
				}
			}
			return newReady;
		}
		else{
			throw new Exception("Not all inputs are ready!");
		}
	}
	
	public abstract void update() throws Exception;
	
	public abstract Node copy();
	
	public abstract String toString();
	
	public void reset(){
		
	}
	
	public void randomize(double const_rate, double min, double max){
		
	}
}
