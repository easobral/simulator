package sim.components.queue;

import java.util.LinkedList;

import sim.components.basic.Job;
import sim.components.basic.Sink;
import sim.components.basic.Source;

public class SizeLimitedFIFO extends SimQueue implements Sink, Source {
	
	private LinkedList<Job> queue;
	private Sink sink;
	private int maxSize;

	public SizeLimitedFIFO(int max, Sink sink){
		queue = new LinkedList<Job>();
		this.sink=sink;
		maxSize = max;
	}
	
	@Override
	public void connectTo(Sink sink) {
		this.sink = sink;
	}

	@Override
	public Job get() {
		return queue.poll();
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
		if(queue.size()==maxSize) return;
		queue.add(job);
		if(sink!=null && sink.canSend()) sink.send(queue.poll());
	}

	@Override
	public boolean canSend() {
		if(queue.size()>=maxSize) return false;
		return true;
	}
}
