package sim.components.queue;

import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.components.basic.Sink;
import sim.timer.TimedTask;
import sim.timer.Timer;

public class DeterministicSource extends Node {
	private static final String inTime = "ARRIVAL_TIME";

	Sink sink;
	Double time_between_arrivals;

	class Arrival extends TimedTask implements Runnable {

		public void run() {
			Job job = new Job();
			job.addDouble(inTime, Timer.timer.time());
			sink.send(job);
			programNextArrival();
		}
	}

	public DeterministicSource() {
		time_between_arrivals = 1D;
	}

	public DeterministicSource(Double time) {
		time_between_arrivals = time;
	}
	
	public void setRate(Double time){
		time_between_arrivals = 1/time;
	}

	public void start() {
		programNextArrival();
	}

	private void programNextArrival() {
		Arrival next = new Arrival();
		next.time = Timer.now() + time_between_arrivals;
		Timer.timer.addTask(next);
	}

	@Override
	public void connectTo(Sink sink) {
		this.sink = sink;
	}

}
