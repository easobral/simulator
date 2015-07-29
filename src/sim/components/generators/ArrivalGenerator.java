package sim.components.generators;

import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.components.basic.Sink;
import sim.timer.TimedTask;
import sim.timer.Timer;

public abstract class ArrivalGenerator extends Node {
	public static final String inTime = "ARRIVAL_TIME";
	Sink sink;

	class Arrival extends TimedTask implements Runnable {

		public void run() {
			Job job = new Job();
			job.addDouble(inTime, Timer.timer.time());
			onDeparture(job);
			sink.send(job);
			programNextArrival();
		}

	}

	@Override
	public void connectTo(Sink sink) {
		this.sink = sink;
	}

	public void start() {
		programNextArrival();
	}

	abstract void programNextArrival();

}
