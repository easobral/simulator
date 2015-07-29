package sim.network;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sim.components.basic.NullSink;
import sim.components.basic.Sink;
import sim.components.queue.ExponentialTimeServer;
import sim.components.queue.FIFO;
import sim.components.queue.generators.ArrivalGenerator;
import sim.components.queue.generators.DeterministicSource;
import sim.components.statistics.DepartureFlowStatistics;
import sim.components.statistics.SystemStatistics;
import sim.timer.Timer;

public class FilaComReentrada {
	public ArrivalGenerator source;
	public FIFO queue;
	public ExponentialTimeServer server;
	public Sink nSink;
	public SystemStatistics ss;
	public DepartureFlowStatistics ds;

	public FilaComReentrada(ArrivalGenerator source, Double serverLambda) {
		this.source = source;
		queue = new FIFO();
		server = new ExponentialTimeServer(serverLambda);
		nSink = new NullSink();

		FileOutputStream chegada;
		FileOutputStream saida;
		FileOutputStream fluxo_saida;
		try {
			chegada = new FileOutputStream("chegada.data");
			saida = new FileOutputStream("saida.data");
			fluxo_saida = new FileOutputStream("fluxo_saida.data");
			ss = new SystemStatistics(queue, server, chegada, saida, "cliente");
			ds = new DepartureFlowStatistics(server, fluxo_saida);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		source.connectTo(queue);
		queue.connectTo(server);
		server.connectTo(nSink);
		server.connectFrom(queue);

	}

	public void close() {
		queue.closeFile();
	}

	public void start() {
		Timer.timer = new Timer();
		source.start();
		Timer.timer.max_time = 10000D;
		Timer.timer.start();
	}

	public static void main(String[] argc) {
		Double lambda = 0.9D;
		Double mi = 1D;
		ArrivalGenerator source = new DeterministicSource(lambda);
		FilaComReentrada n = new FilaComReentrada(source, mi);

		n.start();
		// System.out.println("Tempo na fila: " + n.queue.meanQueueTime());
		// System.out.println("Clientes na fila: " + n.queue.meanJob());
		// System.out.println("Tempo no servidor: " +
		// n.server.meanServerTime());
		// System.out.println("Clientes no servidor: " + n.server.meanJob());

		// System.out.println("Little: " + lambda*n.queue.meanQueueTime() + " =
		// " + n.queue.meanJob());
		// System.out.println("Little: " + lambda/mi + " = " +
		// n.server.meanJob());
		// System.out.println("Período Ocupado:" + lambda/mi + " = " +
		// n.server.meanJob());

		// System.out.println("Numero medio de clientes no sistema:
		// "+n.ss.meanJobInSystem());
		// System.out.println("Tempo medio no sistema:
		// "+n.ss.meanTimeInSystem());
		FileOutputStream file;
		try {
			file = new FileOutputStream("lambda_tempo_medio.data");

			for (lambda = 0.05; lambda <= 0.91; lambda += 0.05) {
				System.out.println("Para lambda = " + lambda + ":");
				source = new DeterministicSource(lambda);
				n = new FilaComReentrada(source, mi);
				n.start();
				System.out.println("Numero medio de clientes no sistema: " + n.ss.meanJobInSystem());
				System.out.println("Tempo medio no sistema: " + n.ss.meanTimeInSystem());
				System.out.println("---------------------------------");
				String line = "" + lambda + " " + n.ss.meanJobInSystem() + "\n";
				file.write(line.getBytes());
			}

			n.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
