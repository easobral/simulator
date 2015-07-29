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

	public void close(){
		queue.closeFile();
	}

	
	public void start(){
		Timer.timer = new Timer();
		source.start();
		Timer.timer.max_time=10000D;
		Timer.timer.start();
	}
	
	public static void main (String[] argc){
		Double lambda = 0.5D;
		Double mi = 1D;
		
		MM1 n = new MM1(lambda,mi);
		
		n.start();

		System.out.println("Tempo na fila: " + n.queue.meanQueueTime());
		System.out.println("Clientes na fila: " + n.queue.meanJob());
		System.out.println("Tempo no servidor: " + n.server.meanServerTime());
		System.out.println("Clientes no servidor: " + n.server.meanJob());

		System.out.println("Little: " + lambda*n.queue.meanQueueTime() + " = " + n.queue.meanJob());
		System.out.println("Little: " + lambda/mi + " = " + n.server.meanJob());
		System.out.println("Período Ocupado:" + lambda/mi + " = " + n.server.meanJob());

		n.close();
	}
	

}
