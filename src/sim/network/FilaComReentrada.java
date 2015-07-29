package sim.network;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import sim.components.basic.NullSink;
import sim.components.basic.Sink;
import sim.components.generators.ArrivalGenerator;
import sim.components.generators.DeterministicSource;
import sim.components.queue.FIFO;
import sim.components.servers.ExponentialTimeServer;
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
		try {
			this.source = source;
			queue = new FIFO();
			server = new ExponentialTimeServer(serverLambda);
			nSink = new NullSink();

			FileOutputStream chegada = new FileOutputStream("chegada.data");
			FileOutputStream saida = new FileOutputStream("saida.data");
			FileOutputStream fluxo_saida = new FileOutputStream("fluxo_saida.data");

			ss = new SystemStatistics(queue, server, chegada, saida, "cliente");
			ds = new DepartureFlowStatistics(server, fluxo_saida);

			source.connectTo(queue);
			queue.connectTo(server);
			server.connectTo(nSink);
			server.connectFrom(queue);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		ArrayList<Double> lambda = new ArrayList<>();
		lambda.add(0.05D);
		lambda.add(0.1D);
		lambda.add(0.15D);
		lambda.add(0.2D);
		lambda.add(0.25D);
		lambda.add(0.3D);
		lambda.add(0.35D);
		lambda.add(0.4D);
		lambda.add(0.45D);
		lambda.add(0.5D);
		lambda.add(0.55D);
		lambda.add(0.6D);
		lambda.add(0.65D);
		lambda.add(0.7D);
		lambda.add(0.75);
		lambda.add(0.8D);
		lambda.add(0.85D);
		lambda.add(0.9D);

		Double mi = 1D;
		
		
		ArrivalGenerator source = new DeterministicSource(lambda.get(1));
		FilaComReentrada n = new FilaComReentrada(source, mi);

		n.start();
		FileOutputStream file;
		try {
			file = new FileOutputStream("lambda_tempo_medio.data");

			for ( Double rate : lambda) {
				System.out.println("Para lambda = " + rate + ":");
				source = new DeterministicSource(rate);
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
