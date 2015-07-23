package sim.components.queue;

import sim.components.basic.Node;
import sim.components.basic.Sink;

public class SimQueue extends Node {

	Sink sink;
		
	@Override
	public void connectTo(Sink sink) {
		this.sink = sink;
	}

	
	
}
