package nets;

import nets.functions.ActivationFunction;

public class Neuron extends Node{
	double bias;
	double initValue;
	ActivationFunction e;
	
	public Neuron(ActivationFunction e){
		super();
		bias = 0.0;
		initValue = 0.0;
		this.e = e;
	}
	
	@Override
	public void reset(){
		this.oldValue = initValue;
		this.newValue = 0.0;
		this.resetReady();
	}
	
	public void randomizeBias(double min, double max){
		this.bias = this.bias + (0.1 * Network.r.nextGaussian());
		if(this.bias < min){
			this.bias = min;
		}
		else if (this.bias > max){
			this.bias = max;
		}
	}
	
	public void randomizeInitValue(double min, double max){
		this.initValue = this.initValue + (0.1 * Network.r.nextGaussian());
		if(this.initValue < min){
			this.initValue = min;
		}
		else if (this.initValue > max){
			this.initValue = max;
		}
	}
	
	public void update(){
		double val = 0.0;
		for(Connection c : inputConnections){
			val += c.getValue();
		}
		val = val + bias;
		newValue = e.evaluate(val);
	}
	
	public Node copy(){
		Neuron n = new Neuron(this.e);
		n.bias = this.bias;
		n.initValue = this.initValue;
		return n;
	}
	
	public String toString(){
		return e.toString() + " Neuron (" + bias + ", " + initValue + ")";
	}
	
	@Override
	public void randomize(double const_rate, double min, double max){
		if(Network.r.nextDouble() <= const_rate){
			this.randomizeBias(min, max);
		}
		if(Network.r.nextDouble() <= const_rate){
			this.randomizeInitValue(min, max);
		}
	}
}
