package sim.components.statistics;

import java.util.ArrayList;

import sim.components.basic.DepartureListener;
import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.timer.Timer;

public class DepartureFlowStatistics implements DepartureListener {

	ArrayList<Double> departure_time;
	ArrayList<Double> departure_interval;
	Double last_departure = 0D;
	Node node;

	public DepartureFlowStatistics(Node node) {
		this.node = node;
		node.addDepartureListener(this);
		departure_interval = new ArrayList<>();
		departure_time = new ArrayList<>();
	}

	@Override
	public void onDeparture(Job job) {
		departure_time.add(Timer.now());
		departure_interval.add(Timer.now() - last_departure);
		last_departure = Timer.now();
	}
}
