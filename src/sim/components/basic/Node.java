package sim.components.basic;

import java.util.HashSet;
import java.util.Set;

public class Node implements Sink, Source {

	Set<ArrivalListener> alistener;
	Set<DepartureListener> dlistener;

	public Node() {
		alistener = new HashSet<>(4);
		dlistener = new HashSet<>(4);
	}

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

	public void addArrivalListener(ArrivalListener al) {
		alistener.add(al);
	}

	public void addDepartureListener(DepartureListener dl) {
		dlistener.add(dl);
	}

	public void onDeparture(Job job) {
		for (DepartureListener listener : dlistener) {
			listener.onDeparture(job);
		}
	}

	public void onArrival(Job job) {
		for (ArrivalListener listener : alistener) {
			listener.onArrival(job);
		}
	}

}
