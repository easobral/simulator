package sim.components.basic;

import java.util.ArrayList;

public class ProbabilityMultiplexer extends Node {

	private ArrayList<Tuple> sinks;
	private Double sum;

	class Tuple {
		Double mass;
		Sink sink;
	}

	public ProbabilityMultiplexer() {
		sinks = new ArrayList<>();
	}

	@Override
	public void connectTo(Sink sink) {
		connectTo(sink, 1D);
	}

	public void connectTo(Sink sink, Double prob) {
		Tuple t = new Tuple();
		t.mass = prob;
		t.sink = sink;
		sum += t.mass;
		sinks.add(t);
	}

	@Override
	public void send(Job job) {
		onArrival(job);
		Double random = Math.random() * sum;
		for (Tuple t : sinks) {
			if (random < t.mass) {
				onDeparture(job);
				t.sink.send(job);
				break;
			}
			random -= t.mass;
		}
	}

}
