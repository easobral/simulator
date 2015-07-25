package sim.components.queue;

import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.components.basic.Sink;
import sim.timer.TimedTask;
import sim.timer.Timer;

public class UniformSource extends Node {
	private static final String inTime = "ARRIVAL_TIME";

	Sink sink;
	Double min_range;
	Double max_range;

	class Arrival extends TimedTask implements Runnable {

		public void run() {
			Job job = new Job();
			job.addDouble(inTime, Timer.timer.time());
			sink.send(job);
			programNextArrival();
		}
	}

	public UniformSource() {
		min_range = 1D;
		max_range = 10D;
	}

	public UniformSource(Double min_time, Double max_time) {
		min_range=min_time;
		max_range=max_time;
	}
	
	public void start() {
		programNextArrival();
	}

	private void programNextArrival() {
		Arrival next = new Arrival();
		next.time = Timer.now() + min_range + Math.random()*(max_range-min_range);
		Timer.timer.addTask(next);
	}

	@Override
	public void connectTo(Sink sink) {
		this.sink = sink;
	}
}
