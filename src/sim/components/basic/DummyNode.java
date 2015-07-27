package sim.components.basic;

public class DummyNode extends Node {

	Source source;
	Sink sink;

	public DummyNode(Source source, Sink sink) {
		this.source = source;
		this.sink = sink;
	}

	@Override
	public boolean canGet() {
		return source.canGet();
	}

	@Override
	public boolean canSend() {
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

}
