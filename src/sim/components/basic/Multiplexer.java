package sim.components.basic;

public abstract class Multiplexer extends Node {

	public Multiplexer() {
		super();
	}
	public abstract void connectTo(Sink sink,String id);
	
}