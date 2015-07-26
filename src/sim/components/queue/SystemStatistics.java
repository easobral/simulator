package sim.components.queue;

import java.io.IOException;
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
	
	Integer jobs_in_system;
	Integer total_jobs;
	Double job_x_time;
	Double time_in_use;
	Double last_update;
	Double total_time_on_sistem;
	

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
		return (time_in_use + jobs_in_system>0?Timer.now()-last_update:0D)/Timer.now();
	}
	
	public Double meanJobInSystem(){
		return (job_x_time+(Timer.now()-last_update)*jobs_in_system)/Timer.now();
	}
	
	public Double meanTimeInSystem(){
		return total_time_on_sistem/total_jobs;
	}


	@Override
	public void onDeparture(Job job) {
		
	}


	@Override
	public void onArrival(Job job) {
		Double time_slice = Timer.now()-last_update;
		job.addDouble(id+ARRAIVAL_SUFIX, Timer.now());
		job_x_time += jobs_in_system * time_slice;
		time_in_use += jobs_in_system>0?time_slice:0;
		String job_id= id +"_"+total_jobs;
		jobs_in_system++;
		total_jobs++;
		String line = job_id +" "+Timer.now()+"\n";
		try {
			arrivalLog.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
