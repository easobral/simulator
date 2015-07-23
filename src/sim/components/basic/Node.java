package sim.components.basic;

public class Node implements Sink, Source {

	Double meanNumJobs=0D;
	Double lastJobTime=0D;

	@Override
	public void connectTo(Sink sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public Job get() {
		return null;
	}

	@Override
	public boolean canGet() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connectFrom(Source source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Job job) {
	}

	@Override
	public boolean canSend() {
		// TODO Auto-generated method stub
		return false;
	}

}
