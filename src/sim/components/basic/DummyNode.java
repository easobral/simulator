package sim.components.basic;

public class DummyNode extends Node {
	
	Source source;
	Sink sink;

	public DummyNode(Source source, Sink sink) {
		this.source=source;
		this.sink=sink;
	}

}
