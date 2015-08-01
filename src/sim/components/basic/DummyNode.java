package sim.components.basic;

public class DummyNode extends Node {

	Source source;
	Sink sink;
	
	public DummyNode(){
		source=null;
		sink=null;
	}

	public DummyNode(Source source, Sink sink) {
		this.source = source;
		this.sink = sink;
	}

	@Override
	public boolean canGet() {
		if(source==null)
			return false;
		return source.canGet();
	}

	@Override
	public boolean canSend() {
		if (sink == null){
			return false;
		}
		return sink.canSend();
	}

	@Override
	public Job get() {
		Job job = source.get();
		onArrival(job);
		onDeparture(job);
		return job;
	}
	
	@Override
	public void send(Job job){
		onArrival(job);
		onDeparture(job);
		sink.send(job);
	}
	
	@Override
	public void connectTo(Sink sink){
		this.sink = sink;
	}
	
	@Override
	public void connectFrom(Source source){
		this.source = source;
	}
}
