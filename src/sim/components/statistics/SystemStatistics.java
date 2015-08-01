package sim.components.statistics;

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
	static public final String ARRAIVAL_SUFIX = "_arraival";
	static public final String JOB_ID = "_job";

	Node entryPoint;
	Node exitPoint;
	String id;

	Integer jobs_in_system;
	Integer arrival_count;
	Integer departure_count;
	Integer arrival_on_empty;
	Integer departure_on_empty;

	Double job_x_time;
	Double time_in_use;
	Double last_update;
	Double total_time_on_sistem;

	public SystemStatistics(Node entryNode, Node exitNode, String id) {
		entryNode.addArrivalListener(this);
		exitNode.addDepartureListener(this);
		entryPoint = entryNode;
		exitPoint = exitNode;
		this.id = id;
		last_update = 0D;
		jobs_in_system = 0;
		job_x_time = 0D;
		time_in_use = 0D;
		departure_count = 0;
		total_time_on_sistem = 0D;
		arrival_count = 0;
		departure_on_empty = 0;
		arrival_on_empty = 0;
	}

	public Double utilization() {
		return (time_in_use + (jobs_in_system > 0 ? Timer.now() - last_update : 0D)) / Timer.now();
	}

	public Double meanJobInSystem() {
		return (job_x_time + (Timer.now() - last_update) * jobs_in_system) / Timer.now();
	}

	public Double meanTimeInSystem() {
		return departure_count > 0 ? total_time_on_sistem / departure_count : 0;
	}

	public Double arrivalOnEmptyFraction() {
		return ((double) arrival_on_empty) / arrival_count;
	}

	public Double departureOnEmptyFraction() {
		return ((double) departure_on_empty) / departure_count;
	}

	public Integer totalArrivals() {
		return arrival_count;
	}

	@Override
	public void onDeparture(Job job) {
		Double time_slice = Timer.now() - last_update;
		Double arrival_time = job.getDouble(id + ARRAIVAL_SUFIX);
		job_x_time += jobs_in_system * time_slice;
		time_in_use += time_slice;
		jobs_in_system--;
		departure_count++;
		total_time_on_sistem += Timer.now() - arrival_time;
		last_update = Timer.now();
		if (jobs_in_system == 0) {
			departure_on_empty++;
		}
	}

	@Override
	public void onArrival(Job job) {
		Double time_slice = Timer.now() - last_update;
		job.addDouble(id + ARRAIVAL_SUFIX, Timer.now());
		job_x_time += jobs_in_system * time_slice;
		time_in_use += jobs_in_system > 0 ? time_slice : 0D;
		String job_id = id + "_" + arrival_count++;
		job.addString(id + JOB_ID, job_id);
		if (jobs_in_system == 0)
			arrival_on_empty++;
		jobs_in_system++;
		last_update = Timer.now();
	}
}
