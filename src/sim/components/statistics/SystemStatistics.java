package sim.components.statistics;

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
	static public final String ARRAIVAL_SUFIX = "_arraival";
	static public final String JOB_ID = "_job";

	Node entryPoint;
	Node exitPoint;
	OutputStream arrivalLog;
	OutputStream departureLog;
	String id;

	Integer jobs_in_system;
	Integer arrival_count;
	Integer total_jobs;
	Integer arrival_on_empty;
	Integer departure_on_empty;
	Double job_x_time;
	Double time_in_use;
	Double last_update;
	Double total_time_on_sistem;

	public SystemStatistics(Node entryNode, Node exitNode, OutputStream arrivalLog, OutputStream departureLog,
			String id) {
		entryNode.addArrivalListener(this);
		exitNode.addDepartureListener(this);
		entryPoint = entryNode;
		exitPoint = exitNode;
		this.arrivalLog = arrivalLog;
		this.departureLog = departureLog;
		this.id = id;
		last_update = 0D;
		jobs_in_system = 0;
		job_x_time = 0D;
		time_in_use = 0D;
		total_jobs = 0;
		total_time_on_sistem = 0D;
		arrival_count = 0;
		departure_on_empty = 0;
	}

	public Double utilization() {
		return (time_in_use + (jobs_in_system > 0 ? Timer.now() - last_update : 0D)) / Timer.now();
	}

	public Double meanJobInSystem() {
		return (job_x_time + (Timer.now() - last_update) * jobs_in_system) / Timer.now();
	}

	public Double meanTimeInSystem() {
		return total_jobs > 0 ? total_time_on_sistem / total_jobs : 0;
	}

	public Double arrivalOnEmpty() {
		return ((double) arrival_on_empty) / arrival_count;
	}

	@Override
	public void onDeparture(Job job) {
		Double time_slice = Timer.now() - last_update;
		Double arrival_time = job.getDouble(id + ARRAIVAL_SUFIX);
		job_x_time += jobs_in_system * time_slice;
		time_in_use += time_slice;
		String job_id = job.getString(id + JOB_ID);
		jobs_in_system--;
		total_jobs++;
		total_time_on_sistem += Timer.now() - arrival_time;
		last_update = Timer.now();
		if (total_time_on_sistem == 0) {
			departure_on_empty++;
		}
		String line = job_id + " " + Timer.now() + "\n";
		try {
			departureLog.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onArrival(Job job) {
		Double time_slice = Timer.now() - last_update;
		job.addDouble(id + ARRAIVAL_SUFIX, Timer.now());
		job_x_time += jobs_in_system * time_slice;
		time_in_use += jobs_in_system > 0 ? time_slice : 0;
		String job_id = id + "_" + arrival_count++;
		job.addString(id + JOB_ID, job_id);
		if (jobs_in_system == 0)
			arrival_on_empty++;
		jobs_in_system++;
		String line = job_id + " " + Timer.now() + "\n";
		last_update = Timer.now();
		try {
			arrivalLog.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
