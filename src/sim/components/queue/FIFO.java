package sim.components.queue;

import java.util.LinkedList;

import sim.components.basic.Job;
import sim.components.basic.Sink;
import sim.components.basic.Source;

public class FIFO extends SimQueue {
	private LinkedList<Job> queue;

	public FIFO() {
		queue = new LinkedList<Job>();
	}

	public FIFO(Sink sink) {
		queue = new LinkedList<Job>();
		this.sink = sink;
	}

	public FIFO(String file) {
		queue = new LinkedList<Job>();
	}

	@Override
	public void connectTo(Sink sink) {
		this.sink = sink;
	}

	@Override
	public Job get() {
		Job j = queue.poll();
		onDeparture(j);
		return j;
	}

	@Override
	public boolean canGet() {
		return !queue.isEmpty();
	}

	@Override
	public void connectFrom(Source source) {
	}

	@Override
	public void send(Job job) {
		queue.add(job);
		onArrival(job);
		if (sink != null && sink.canSend()) {
			Job j = queue.poll();
			sink.send(j);
		}
	}

	@Override
	public boolean canSend() {
		return true;
	}

}
