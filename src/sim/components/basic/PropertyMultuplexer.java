package sim.components.basic;

import java.util.HashMap;

public class PropertyMultuplexer extends Multiplexer {

	String property;
	HashMap<String, Sink> map;
	
	public PropertyMultuplexer(String property) {
		// TODO Auto-generated constructor stub
		this.property = property;
		map = new HashMap<>();
	}
	
	@Override
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
		String id = job.getString(property);
		if (null == id) return;		
		Sink sink = map.get(id);
		if (null == sink) return;
		sink.send(job);
	}
	
}
