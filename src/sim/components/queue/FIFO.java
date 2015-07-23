package sim.components.queue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import sim.components.basic.Job;
import sim.components.basic.Sink;
import sim.components.basic.Source;
import sim.timer.Timer;


public class FIFO extends SimQueue {
	private LinkedList<Job> queue;
	private Sink sink;
	OutputStream out;
	
	Double jobsSum=0D;
	Double lastChange=0D;
	Double totalTimeinQueue=0D;
	Long totalJobs=0L;

	
	private final String arrival_time = "SIMQUEUE_ARRIVAL";

	public void closeFile(){
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Double meanQueueTime(){
		return totalJobs >0 ?totalTimeinQueue/totalJobs:0D;
	}

	public Double meanJob(){
		return jobsSum/lastChange;
	}

	public FIFO(){
		queue = new LinkedList<Job>();		
		try {
			out=new FileOutputStream("MM1.dat");
			out.write(("0 0\n".getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	public FIFO(Sink sink){
		queue = new LinkedList<Job>();
		this.sink=sink;
		try {
			out=new FileOutputStream("MM1.dat");
			out.write(("0 0\n".getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FIFO(String file) {
		queue = new LinkedList<Job>();
		try {
			out=new FileOutputStream(file);
			out.write(("0 0\n".getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void connectTo(Sink sink) {
		this.sink = sink;
	}

	public void registerChange() {
		lastChange = Timer.now();
		try {
			out.write(("" + lastChange + " " + queue.size()+"\n").getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Job get() {
		jobsSum+=queue.size()*(Timer.now()-lastChange);
		Job j = queue.poll();
		registerChange();
		Double arrival = j.getDouble(arrival_time);
		totalTimeinQueue+=(Timer.now()-arrival);
		return j;
	}
	
	@Override
	public boolean canGet() {
		return !queue.isEmpty();
	}

	@Override
	public void connectFrom(Source source) {
	}

	@Override
	public void send(Job job) {
		job.addDouble(arrival_time, Timer.now());
		totalJobs++;
		jobsSum+=queue.size()*(Timer.now()-lastChange);
		queue.add(job);
		if(sink!=null && sink.canSend()){
			Job j = queue.poll();
			Double arrival = j.getDouble(arrival_time);
			totalTimeinQueue+=(Timer.now()-arrival);
			sink.send(j);
		}
		registerChange();
	}
	
	@Override
	public boolean canSend() {
		return true;
	}

}
