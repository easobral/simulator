package sim.components.queue;

import java.io.OutputStream;

import sim.components.basic.ArrivalListener;
import sim.components.basic.DepartureListener;
import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.timer.Timer;

/**
 * @author eduardo
 *
 */
public class SystemStatistics implements ArrivalListener, DepartureListener {
	static public String ARRAIVAL_SUFIX="_arraival";
	static public String DEPARTURE_SUFIX="_departure";
	
	Node entryPoint;
	Node exitPoint;
	OutputStream arrivalLog;
	OutputStream departureLog;
	String id;
	
	Integer jobs;
	Double job_x_time;
	Double time_in_use;
	Double last_update;
	Double total_time;
	

	public SystemStatistics(Node entryNode, Node exitNode,
			OutputStream arrivalLog, OutputStream departureLog,
			String id) {
		entryNode.addArrivalListener(this);
		exitNode.addDepartureListener(this);
		entryPoint=entryNode;
		exitPoint=exitNode;
		this.arrivalLog=arrivalLog;
		this.departureLog=departureLog;
		this.id=id;
	}
	
	
	public Double utilization(){
		return time_in_use/Timer.now();
	}
	
	public Double meanJobInSystem(){
		return job_x_time/Timer.now();
	}
	
	public Double meanTimeInSystem(){
		return null;		
	}


	@Override
	public void onDeparture(Job job) {
		
	}


	@Override
	public void onArrival(Job job) {
		Double time_slice = Timer.now()-last_update;
		job.addDouble(id+ARRAIVAL_SUFIX, Timer.now());
		job_x_time += jobs * time_slice;
		time_in_use += time_slice;
	}

}
