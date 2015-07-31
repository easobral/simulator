package sim.components.generators;

import sim.timer.Timer;

public class UniformSource extends ArrivalGenerator {

	Double min_range;
	Double max_range;

	public UniformSource() {
		min_range = 1D;
		max_range = 10D;
	}

	public UniformSource(Double min_time, Double max_time) {
		min_range = min_time;
		max_range = max_time;
	}

	@Override
	public void programNextArrival() {
		Arrival next = new Arrival();
		next.time = Timer.now() + min_range + Math.random() * (max_range - min_range);
		Timer.timer.addTask(next);
	}
}