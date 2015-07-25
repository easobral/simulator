package sim.components.basic;

public class Node implements Sink, Source {

	@Override
	public void connectTo(Sink sink) {
	}

	@Override
	public Job get() {
		return null;
	}

	@Override
	public boolean canGet() {
		return false;
	}

	@Override
	public void connectFrom(Source source) {
	}

	@Override
	public void send(Job job) {
	}

	@Override
	public boolean canSend() {
		return false;
	}

}
