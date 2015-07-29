package sim.components.servers;

import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.components.basic.Sink;
import sim.components.basic.Source;

public class SimServer extends Node {

	public SimServer() {
	}

	@Override
	public void connectFrom(Source source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Job job) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canSend() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connectTo(Sink sink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Job get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canGet() {
		// TODO Auto-generated method stub
		return false;
	}

}