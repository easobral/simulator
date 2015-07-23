package sim.components.queue;

import sim.components.basic.Job;
import sim.components.basic.Sink;
import sim.components.basic.Source;
import sim.timer.TimedTask;
import sim.timer.Timer;

public class QueueTransmisionServer extends SimServer implements Sink, Source {

	private Job servingJob;
	private Double transmisionTime;
	private Sink sink;
	private Source source;

	public QueueTransmisionServer(Integer MSS, Double txEnlace) {
		transmisionTime = MSS * 8 / txEnlace;
		sink = null;
		servingJob = null;
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
			servingJob = job;
			serveJob();
		}
	}

	private void serveJob() {
		Timer t = Timer.timer;
		double endTransmition = t.time() + transmisionTime;
		Runnable run = new Runnable() {
			@Override
			public void run() {
				sink.send(servingJob);
				servingJob = null;
				if (source.canGet()) {
					servingJob = source.get();
					serveJob();
				}
			}
		};
		t.addTask(new TimedTask(endTransmition, run));
	}

	@Override
	public boolean canSend() {
		return servingJob == null;
	}

}
