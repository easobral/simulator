package sim.components.servers;

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

	class JobCompleted extends TimedTask {

		public JobCompleted(Double time) {
			super.time = time;
		}

		@Override
		public void run() {
			onDeparture(servingJob);
			sink.send(servingJob);
			servingJob = null;
			if (source.canGet()) {
				serveJob(source.get());
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
		if (servingJob == null) {
			serveJob(job);
		}
	}

	private void serveJob(Job job) {
		onArrival(job);
		servingJob = job;
		Double servingTime = gen.get();
		Timer.timer.addTask(new JobCompleted(Timer.now() + servingTime));
	}

	@Override
	public boolean canSend() {
		return servingJob == null;
	}

}
