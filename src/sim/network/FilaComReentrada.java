package sim.network;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import sim.components.basic.DummyNode;
import sim.components.basic.NullSink;
import sim.components.basic.ProbabilityMultiplexer;
import sim.components.basic.Sink;
import sim.components.generators.ArrivalGenerator;
import sim.components.generators.DeterministicSource;
import sim.components.generators.ExponentialSource;
import sim.components.generators.UniformSource;
import sim.components.queue.FIFO;
import sim.components.servers.ExponentialTimeServer;
import sim.components.statistics.ArrivalFlowStatistics;
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
	public ProbabilityMultiplexer pm;
	public ArrivalFlowStatistics as;

	FileOutputStream chegada;
	FileOutputStream saida;
	FileOutputStream fluxo_saida;
	FileOutputStream fluxo_saida_servidor;
	FileOutputStream fluxo_entrada_fila;

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

	public FilaComReentrada(String dir, ArrivalGenerator source, Double serverLambda) {
		try {
			this.source = source;
			queue = new FIFO();
			server = new ExponentialTimeServer(serverLambda);
			nSink = new NullSink();

			chegada = new FileOutputStream(dir + "chegada.data");
			saida = new FileOutputStream(dir + "saida.data");
			fluxo_saida = new FileOutputStream(dir + "fluxo_saida.data");

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

	public FilaComReentrada(String dir, ArrivalGenerator source, Double serverLambda, Double p) {
		try {
			DummyNode dummy_arrival = new DummyNode(null, null);
			DummyNode dummy_departure = new DummyNode(null, null);

			this.source = source;
			queue = new FIFO();
			server = new ExponentialTimeServer(serverLambda);
			nSink = new NullSink();

			chegada = new FileOutputStream(dir + "chegada.data");
			saida = new FileOutputStream(dir + "saida.data");
			fluxo_saida = new FileOutputStream(dir + "fluxo_saida.data");
			fluxo_saida_servidor = new FileOutputStream(dir + "fluxo_saida_servidor.data");
			fluxo_entrada_fila = new FileOutputStream(dir + "fluxo_chegada_fila.data");

			pm = new ProbabilityMultiplexer();

			ss = new SystemStatistics(dummy_arrival, dummy_departure, chegada, saida, "cliente");
			ds = new DepartureFlowStatistics(dummy_departure, fluxo_saida);

			source.connectTo(queue);
			source.connectTo(dummy_arrival);
			dummy_arrival.connectTo(queue);
			queue.connectTo(server);
			server.connectFrom(queue);
			server.connectTo(pm);
			pm.connectTo(queue, p);
			pm.connectTo(dummy_departure, 1 - p);
			dummy_departure.connectTo(nSink);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		Timer.timer = new Timer();
		source.start();
		Timer.timer.max_time = 10000D;
		Timer.timer.start();
	}

	public void close() {
		try {
			chegada.close();
			saida.close();
			fluxo_saida.close();
			if (fluxo_entrada_fila != null) {
				fluxo_entrada_fila.close();
			}
			if (fluxo_saida_servidor != null) {
				fluxo_saida_servidor.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] argc) {

		scenario01(100);
		scenario02(100);
		scenario03(100);

	}

	public static void scenario01(Integer runs) {
		String dir = "Data/cenario_1/";
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
		FilaComReentrada fila;
		ArrivalGenerator source;

		ArrayList<Double> samples_time = new ArrayList<>();
		ArrayList<Double> samples_job = new ArrayList<>();

		FileWriter file;
		try {
			FileWriter sumary = new FileWriter(dir + "summary.data");
			sumary.write("# taxa media_tempo erro_padrao_tempo media_trabalho erro_padrao_trabalho");
			System.out.println("Scenario01");
			for (Double rate : lambda) {
				file = new FileWriter(dir + "lambda_" + rate + ".data");
				for (int i = 0; i < runs; i++) {
					source = new ExponentialSource(rate);
					fila = new FilaComReentrada(dir, source, mi);
					fila.start();
					String line = "" + fila.ss.meanTimeInSystem() + " " + fila.ss.meanJobInSystem() + "\n";
					file.write(line);
					samples_job.add(fila.ss.meanJobInSystem());
					samples_time.add(fila.ss.meanTimeInSystem());
					fila.close();
				}
				file.close();
				NumberFormat f = new DecimalFormat("0.0000000000");
				Double mean_time = calculate_mean(samples_time);
				Double sd_time = 1.96 * calculate_sd(samples_time, mean_time);
				Double mean_job = calculate_mean(samples_job);
				Double sd_job = 1.96 * calculate_sd(samples_job, mean_job);
				sumary.write("" + rate + " " + mean_time + " " + sd_time);
				sumary.write(" " + mean_job + " " + sd_job + "\n");
				System.out.println("Finished: " + rate);
				System.out.println("time_mean:" + f.format(mean_time) + "\n" + "time_sd: " + f.format(sd_time));
				System.out.println("job_mean:" + f.format(mean_job) + "\n" + "job_sd: " + f.format(sd_job));
				System.out.println("---------------------------------");
			}
			System.out.println("\n\n\n\n\n\n\n\n\n");
			sumary.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void scenario02(Integer runs) {
		String dir = "Data/cenario_2/";
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
		FilaComReentrada fila;
		ArrivalGenerator source;

		ArrayList<Double> samples_time = new ArrayList<>();
		ArrayList<Double> samples_job = new ArrayList<>();

		FileWriter file;
		try {
			FileWriter sumary = new FileWriter(dir + "summary.data");
			sumary.write("# taxa media_tempo erro_padrao_tempo media_trabalho erro_padrao_trabalho");

			for (Double rate : lambda) {
				file = new FileWriter(dir + "lambda_" + rate + ".data");
				for (int i = 0; i < runs; i++) {
					source = new DeterministicSource(rate);
					fila = new FilaComReentrada(dir, source, mi);
					fila.start();
					String line = "" + fila.ss.meanTimeInSystem() + " " + fila.ss.meanJobInSystem() + "\n";
					file.write(line);
					fila.close();
					samples_job.add(fila.ss.meanJobInSystem());
					samples_time.add(fila.ss.meanTimeInSystem());
				}
				file.close();
				NumberFormat f = new DecimalFormat("0.0000000000");
				Double mean_time = calculate_mean(samples_time);
				Double sd_time = 1.96 * calculate_sd(samples_time, mean_time);
				Double mean_job = calculate_mean(samples_job);
				Double sd_job = 1.96 * calculate_sd(samples_job, mean_job);
				sumary.write("" + rate + " " + mean_time + " " + sd_time);
				sumary.write(" " + mean_job + " " + sd_job + "\n");
				System.out.println("Finished: " + rate);
				System.out.println("time_mean:" + f.format(mean_time) + "\n" + "time_sd: " + f.format(sd_time));
				System.out.println("job_mean:" + f.format(mean_job) + "\n" + "job_sd: " + f.format(sd_job));
				System.out.println("---------------------------------");
			}
			sumary.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void scenario03(Integer runs) {
		String dir = "Data/cenario_3/";
		ArrayList<Double> mi = new ArrayList<>();
		mi.add(1D);
		mi.add(1.5D);
		mi.add(2D);
		mi.add(2.5D);
		mi.add(3D);
		mi.add(3.5D);
		mi.add(4D);
		mi.add(4.5D);
		mi.add(5D);
		mi.add(5.5D);
		mi.add(6D);
		mi.add(6.5D);
		mi.add(7D);
		mi.add(7.5D);
		mi.add(8D);
		mi.add(8.5D);
		mi.add(9D);
		mi.add(9.5D);
		mi.add(10D);

		FilaComReentrada fila;
		ArrivalGenerator source;

		ArrayList<Double> samples_time = new ArrayList<>();
		ArrayList<Double> samples_job = new ArrayList<>();

		FileWriter file;
		try {
			FileWriter sumary = new FileWriter(dir + "summary.data");
			sumary.write("# taxa media_tempo erro_padrao_tempo media_trabalho erro_padrao_trabalho");

			for (Double rate : mi) {
				file = new FileWriter(dir + "mi_" + rate + ".data");
				for (int i = 0; i < runs; i++) {
					source = new UniformSource(5D, 15D);
					fila = new FilaComReentrada(dir, source, rate);
					fila.start();
					String line = "" + fila.ss.meanTimeInSystem() + " " + fila.ss.meanJobInSystem() + "\n";
					fila.close();
					file.write(line);
					samples_job.add(fila.ss.meanJobInSystem());
					samples_time.add(fila.ss.meanTimeInSystem());
				}
				file.close();
				NumberFormat f = new DecimalFormat("0.0000000000");
				Double mean_time = calculate_mean(samples_time);
				Double sd_time = 1.96 * calculate_sd(samples_time, mean_time);
				Double mean_job = calculate_mean(samples_job);
				Double sd_job = 1.96 * calculate_sd(samples_job, mean_job);
				sumary.write("" + rate + " " + mean_time + " " + sd_time);
				sumary.write(" " + mean_job + " " + sd_job + "\n");
				System.out.println("Finished: " + rate);
				System.out.println("time_mean:" + f.format(mean_time) + "\n" + "time_sd: " + f.format(sd_time));
				System.out.println("job_mean:" + f.format(mean_job) + "\n" + "job_sd: " + f.format(sd_job));
				System.out.println("---------------------------------");
			}
			sumary.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Double calculate_mean(ArrayList<Double> samples) {
		Double sum = 0D;
		for (Double sample : samples) {
			sum += sample;
		}
		return sum / samples.size();
	}

	private static Double calculate_sd(ArrayList<Double> samples, Double mean) {
		Double sum = 0D;
		for (Double sample : samples) {
			sum += (sample - mean) * (sample - mean);
		}
		Double var = sum / (samples.size() - 1);
		return Math.sqrt(var / samples.size());
	}
}