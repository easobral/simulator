package sim.network;

import sim.components.basic.NullSink;
import sim.components.basic.Sink;
import sim.components.generators.ExponentialSource;
import sim.components.queue.FIFO;
import sim.components.servers.ExponentialTimeServer;
import sim.timer.Timer;

public class MM1 {
	public ExponentialSource source;
	public FIFO queue;
	public ExponentialTimeServer server;
	public Sink nSink;

	public MM1(Double sourceLambda, Double serverLambda) {
		source = new ExponentialSource(sourceLambda);
		queue = new FIFO();
		server = new ExponentialTimeServer(serverLambda);
		nSink = new NullSink();
		
		source.connectTo(queue);
		queue.connectTo(server);
		server.connectTo(nSink);
		server.connectFrom(queue);
		
	}

	public MM1(Double sourceLambda, Double serverLambda,String file) {
		source = new ExponentialSource(sourceLambda);
		queue = new FIFO(file);
		server = new ExponentialTimeServer(serverLambda);
		nSink = new NullSink();
		
		source.connectTo(queue);
		queue.connectTo(server);
		server.connectTo(nSink);
		server.connectFrom(queue);
		Timer.timer=new Timer();
	}
	
	public void start(){
		Timer.timer = new Timer();
		source.start();
		Timer.timer.max_time=10000D;
		Timer.timer.start();
	}
	
	

}
