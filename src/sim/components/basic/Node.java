package sim.components.basic;

import java.util.Set;

public class Node implements Sink, Source {

	Set<ArrivalListener> alistener;
	Set<DepartureListener> dlistener;
	
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
