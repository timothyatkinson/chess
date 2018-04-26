package nets;

public class Input extends Node{

	@Override
	public void update() throws Exception {
		//Does nothing, inputs are controlled externally!
	}

	@Override
	public Node copy() {
		return new Input();
	}

	@Override
	public String toString() {
		return "Input";
	}
	
	@Override
	public void reset(){
		this.oldValue = 0.0;
	}

	
}
