package nets.functions;

public class logistic implements ActivationFunction{

	@Override
	public double evaluate(double wSum) {
		return 1.0 / (1.0 + Math.pow(Math.E, -wSum));
	}
	
	@Override
	public String toString(){
		return "Logistic";
	}

}
