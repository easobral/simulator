package sim.components.queue;

import sim.components.basic.Job;
import sim.components.basic.Node;
import sim.components.basic.Sink;
import sim.math.ExponentialGenerator;
import sim.timer.TimedTask;
import sim.timer.Timer;

public class ExponentialSource extends Node{
	private static final String inTime = "EXP_SOURCE_IN_TIME";
	
	Sink sink;
	ExponentialGenerator exp;
	
	class Arrival extends TimedTask implements Runnable{

		public void run(){
			Job job = new Job();
			job.addDouble(inTime, Timer.timer.time());
			sink.send(job);
			programNextArrival();
		}
	}
	
	public ExponentialSource() {
		exp = new ExponentialGenerator(1);
	}

	public ExponentialSource(Double lambda){
		exp = new ExponentialGenerator(lambda);
	}	

		
	public ExponentialSource setTax(Double exponentialMedia){
		exp = new ExponentialGenerator(exponentialMedia);
		return this;
	}
		
	public void start(){
		programNextArrival();
	}
	
	
	private void programNextArrival() {
		Arrival next = new Arrival();
		next.time=Timer.now() + exp.get();
		Timer.timer.addTask(next);
	}
	
	@Override
	public void connectTo(Sink sink){
		this.sink=sink;
	}

}
