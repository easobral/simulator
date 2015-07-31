package sim.components.basic;

import java.util.HashMap;

public class PropertyMultiplexer extends Node {

	String property;
	HashMap<String, Sink> map;
	
	public PropertyMultiplexer(String property) {
		// TODO Auto-generated constructor stub
		this.property = property;
		map = new HashMap<>();
	}
	
	public void connectTo(Sink sink,String id) {
		map.put(id, sink);
	}
	
	@Override
	public boolean canSend() {
		return true;
	}
	
	@Override
	public void send(Job job) {
		super.send(job);
		onArrival(job);
		String id = job.getString(property);
		if (null == id) return;		
		Sink sink = map.get(id);
		if (null == sink) return;
		onDeparture(job);
		sink.send(job);
	}
	
}
