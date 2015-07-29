package sim.components.queue.generators;

import sim.math.ExponentialGenerator;
import sim.timer.Timer;

public class ExponentialSource extends ArrivalGenerator {
	ExponentialGenerator exp;

	public ExponentialSource() {
		exp = new ExponentialGenerator(1);
	}

	public ExponentialSource(Double rate) {
		exp = new ExponentialGenerator(rate);
	}

	public ExponentialSource setRate(Double rate) {
		exp = new ExponentialGenerator(rate);
		return this;
	}

	@Override
	public void programNextArrival() {
		Arrival next = new Arrival();
		next.time = Timer.now() + exp.get();
		Timer.timer.addTask(next);
	}

}
