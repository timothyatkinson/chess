package nets;

public class Connection {
	Node source;
	Node target;
	double weight;
	boolean feedback;
	
	public Connection(Node s, Node t, double w, boolean fb){
		this.source = s;
		this.target = t;
		this.weight = w;
		this.feedback = fb;
	}
	
	public double getValue(){
		if(this.feedback){
			return this.weight * target.oldValue;
		}
		else{
			return this.weight * target.newValue;
		}
	}
	
	//Only safe to use outside of network runs
	public void moveSource(Node newSource){
		this.source.inputConnections.remove(this);
		this.source.readyInputs.remove(0);
		this.source = newSource;
		newSource.inputConnections.add(this);
		newSource.readyInputs.add(false);
	}

	
	public void moveTarget(Node newTarget){
		this.target.outputConnections.remove(this);
		this.target = newTarget;
		newTarget.outputConnections.add(this);
	}
	
	public void randomizeWeight(double min, double max){
		this.weight = this.weight + (0.1 * Network.r.nextGaussian());
		if(this.weight < min){
			this.weight = min;
		}
		else if (this.weight > max){
			this.weight = max;
		}
	}
	
	public boolean notifySource(){
		this.source.notifyReady(this);
		return source.allReady();
	}
}
