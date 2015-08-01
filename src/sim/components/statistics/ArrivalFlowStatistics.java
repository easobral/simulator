package sim.components.statistics;

import java.util.ArrayList;

import sim.components.basic.ArrivalListener;
import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.timer.Timer;

public class ArrivalFlowStatistics implements ArrivalListener {

	ArrayList<Double> arrival_time;
	ArrayList<Double> arrival_interval;
	Double last_arrival=0D;
	Node node;

	public ArrivalFlowStatistics(Node node) {
		this.node = node;
		node.addArrivalListener(this);
		arrival_time = new ArrayList<>();
		arrival_interval = new ArrayList<>();
	}

	@Override
	public void onArrival(Job job) {
		arrival_time.add(Timer.now());
		arrival_interval.add(Timer.now()-last_arrival);
		last_arrival=Timer.now();
	}
}