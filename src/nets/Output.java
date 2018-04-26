package nets;

public class Output extends Node{

	@Override
	public void update() throws Exception {
		this.newValue = inputConnections.get(0).getValue();
	}

	@Override
	public Node copy() {
		return new Output();
	}

	@Override
	public String toString() {
		return "Output";
	}

	
}
