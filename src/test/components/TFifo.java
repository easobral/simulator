package test.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sim.components.basic.Job;
import sim.components.queue.SizeLimitedFIFO;

public class TFifo {

	ArrayList<Job> job = new ArrayList<>();
	SizeLimitedFIFO fifo = new SizeLimitedFIFO(5,null);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		job = new ArrayList<>();
		fifo = new SizeLimitedFIFO(5,null);

		for(int i = 0; i<10; i++){
			job.add(new Job());
			job.get(i).addDouble("id", i);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		for(int i = 0; i< job.size(); i++){
			if(fifo.canSend()) fifo.send(job.get(i));
		}
		Job j = fifo.get();
		assertEquals(0, j.getDouble("id"),0.1);
		
		j = fifo.get();
		assertEquals(1, j.getDouble("id"),0.1);

		j = fifo.get();
		assertEquals(2, j.getDouble("id"),0.1);

		j = fifo.get();
		assertEquals(3, j.getDouble("id"),0.1);

		j = fifo.get();
		assertEquals(4, j.getDouble("id"),0.1);

		j = fifo.get();
		assertNull(j);

		for(int i = job.size()-1; i>=0; i--){
			fifo.send(job.get(i));
		}
		
		j = fifo.get();
		assertEquals(9, j.getDouble("id"),0.1);
		
		j = fifo.get();
		assertEquals(8, j.getDouble("id"),0.1);

		j = fifo.get();
		assertEquals(7, j.getDouble("id"),0.1);

		j = fifo.get();
		assertEquals(6, j.getDouble("id"),0.1);

		j = fifo.get();
		assertEquals(5, j.getDouble("id"),0.1);

		j = fifo.get();
		assertNull(j);

	}

}
