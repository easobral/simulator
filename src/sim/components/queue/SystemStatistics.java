package sim.components.queue;

import java.io.OutputStream;

import sim.components.basic.ArrivalListener;
import sim.components.basic.DepartureListener;
import sim.components.basic.Job;
import sim.components.basic.Node;

/**
 * @author eduardo
 *
 */
public class SystemStatistics implements ArrivalListener, DepartureListener {
	
	Node entryPoint;
	Node exitPoint;
	OutputStream out;
	String id;
	
	Integer jobs;	
	
	public SystemStatistics(Node entryNode, Node exitNode, OutputStream out, String id) {
		entryNode.addArrivalListener(this);
		exitNode.addDepartureListener(this);
		entryPoint=entryNode;
		exitPoint=exitNode;
		this.out=out;
		this.id=id;
	}
	
	
	public Double utilization(){
		return null;
	}
	
	public Double meanJobInSystem(){
		return null;
	}
	
	public Double meanTimeInSystem(){
		return null;
		
	}


	@Override
	public void onDeparture(Job job) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onArrival(Job job) {
		// TODO Auto-generated method stub
		
	}

}
