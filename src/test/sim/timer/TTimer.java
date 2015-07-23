package test.sim.timer;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import sim.timer.TimedTask;
import sim.timer.Timer;

public class TTimer {
	static class TestTimer extends Timer {
		public int runs = 0;
		public int max_runs;

		@Override
		public boolean shouldStop() {
			return runs >= max_runs;
		}

		@Override
		public void afterLoop() {
			runs++;
		}
	}

	static class TimedTestRunner implements Runnable {
		double time;
		int pos;
		static int cpos=0;

		public TimedTestRunner(double time, int pos) {
			this.time = time;
			this.pos = pos;
		}

		@Override
		public void run() {
			assertEquals(timer.time(), time,0.5);
			assertEquals(pos, cpos);
			cpos++;
		}

	}

	static TestTimer timer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		timer = new TestTimer();
		timer.max_runs=10;
	}

	@Test
	public void testStart() {
		// #1
		double time = 100;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,9)));
		// #2
		time = 20;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,4)));
		// #3
		time = 10;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,1)));
		// #4
		time = 16;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,3)));
		// #5
		time = 30;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,6)));
		// #6
		time = 15;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,2)));
		// #7
		time = 35;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,7)));
		// #8
		time = 3;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,0)));
		// #9
		time = 25;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,5)));
		// #10
		time = 50;
		timer.addTask(new TimedTask(time, new TimedTestRunner(time,8)));

		timer.start();
	}

}
