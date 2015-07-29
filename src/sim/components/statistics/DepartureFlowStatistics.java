package sim.components.statistics;

import java.io.IOException;
import java.io.OutputStream;
import sim.components.basic.DepartureListener;
import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.timer.Timer;

public class DepartureFlowStatistics implements DepartureListener {
	OutputStream out;
	Double last_arrival = 0D;
	Node node;

	public DepartureFlowStatistics(Node node, OutputStream out) {
		this.node = node;
		node.addDepartureListener(this);
		this.out = out;
	}

	@Override
	public void onDeparture(Job job) {
		Double time = Timer.now();
		Double interval = time - last_arrival;
		String line = "" + time + " " + interval+"\n";
		last_arrival = time;
		try {
			out.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
