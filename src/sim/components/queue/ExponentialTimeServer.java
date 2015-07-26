package sim.components.queue;

import sim.components.basic.Job;
import sim.components.basic.Sink;
import sim.components.basic.Source;
import sim.math.ExponentialGenerator;
import sim.timer.TimedTask;
import sim.timer.Timer;

public class ExponentialTimeServer extends SimServer {

	private Job servingJob;
	private Sink sink;
	private Source source;
	ExponentialGenerator gen;

	Double lastChange=0D;
	Double totalTimeinQueue=0D;
	Long totalJobs=0L;

	
	public Double meanServerTime(){
		return lastChange>0 ?totalTimeinQueue/lastChange:0D;
	}

	public Double meanJob(){
		return lastChange>0 ?totalTimeinQueue/lastChange:0D;
	}


	class JobCompleted extends TimedTask {

		public JobCompleted(Double time) {
			super.time=time;
		}
		
		@Override
		public void run() {
			onDeparture(servingJob);
			sink.send(servingJob);
			servingJob = null;
			lastChange=Timer.now();
			if (source.canGet()) {
				servingJob = source.get();
				serveJob();
			}
		}
	}

	public ExponentialTimeServer(Double lambda) {
		sink = null;
		servingJob = null;
		gen = new ExponentialGenerator(lambda);
	}

	@Override
	public void connectTo(Sink sink) {
		this.sink = sink;
	}

	@Override
	public Job get() {
		return null;
	}

	@Override
	public boolean canGet() {
		return false;
	}

	@Override
	public void connectFrom(Source source) {
		this.source = source;
	}

	@Override
	public void send(Job job) {
		onArrival(job);
		if (servingJob == null) {
			servingJob = job;
			serveJob();
		}
	}

	private void serveJob() {
		Double servingTime = gen.get();
		totalJobs++;
		totalTimeinQueue+=servingTime;
		lastChange=Timer.now();
		Timer.timer.addTask(new JobCompleted(Timer.now()+servingTime));
	}

	@Override
	public boolean canSend() {
		return servingJob == null;
	}

}
