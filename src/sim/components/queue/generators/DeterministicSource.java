package sim.components.queue.generators;

import sim.timer.Timer;

public class DeterministicSource extends ArrivalGenerator {
	Double time_between_arrivals;

	public DeterministicSource() {
		time_between_arrivals = 1D;
	}

	public DeterministicSource(Double time) {
		time_between_arrivals = 1 / time;
	}

	public void setRate(Double time) {
		time_between_arrivals = 1 / time;
	}

	public void programNextArrival() {
		Arrival next = new Arrival();
		next.time = Timer.now() + time_between_arrivals;
		Timer.timer.addTask(next);
	}
}
