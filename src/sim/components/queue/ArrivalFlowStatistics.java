package sim.components.queue;

import java.io.IOException;
import java.io.OutputStream;

import sim.components.basic.ArrivalListener;
import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.timer.Timer;

public class ArrivalFlowStatistics implements ArrivalListener {

	OutputStream out;
	Double last_arrival=0D;
	Node node;

	public ArrivalFlowStatistics(Node node, OutputStream out) {
		this.node = node;
		node.addArrivalListener(this);
		this.out=out;
	}

	@Override
	public void onArrival(Job job) {
		Double time = Timer.now();
		Double interval = time - last_arrival;
		String line = "" + time + " " + interval;
		last_arrival=time;
		try {
			out.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}