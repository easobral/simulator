package sim.timer;

import java.util.PriorityQueue;

import sim.timer.exception.TaskInPastException;

public class Timer {
	
	public static Timer timer;

	public static Double now(){
		return timer.time();
	}
	
	public static void addTaskS(TimedTask task){
		timer.addTask(task);
	}
	
	private PriorityQueue<TimedTask> taskQueue;
	private boolean _should_stop;
	private double _time;
	public Double max_time =1000D;
	
	public Timer() {
		_should_stop = false;
		taskQueue = new PriorityQueue<TimedTask>();
		_time = 0;
	}

	public boolean shouldStop() {
		return _should_stop || time()> max_time;
	}

	public void stop() {
		_should_stop = true;
	}

	public double time() {
		return _time;
	}

	public void addTask(TimedTask task) {
		taskQueue.add(task);
	}

	public void start() {
		while (!shouldStop()) {
			try {
				beforeLoop();
				doLoop();
				afterLoop();
			} catch (TaskInPastException e) {
				System.out.println(e.msg);
			}
		}
	}
	
	public void doLoop () throws TaskInPastException{
		TimedTask task = taskQueue.poll();
		if (_time > task.time){
			TaskInPastException e= new TaskInPastException();
			String msg = "ctime="+_time+" : ttime="+task.time+" msg:"+task.debug_msg;
			e.msg=msg;
			throw e;
		}
		this._time = task.time;
		task.run();	
	}
	
	
	public void beforeLoop(){
		
	}
	
	public void afterLoop(){
		
	}

}
