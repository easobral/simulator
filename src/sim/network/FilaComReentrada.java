package sim.network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

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

	static class Nuple {
		public ArrayList<Double> mean_job = new ArrayList<>();
		public ArrayList<Double> empty_time = new ArrayList<>();
		public ArrayList<Double> empty_arrival = new ArrayList<>();
		public ArrayList<Double> empty_departure = new ArrayList<>();
	}

	static class Summary {
		public Double rate;
		public Double mean_job;
		public Double empty_time;
		public Double empty_arrival;
		public Double empty_departure;
		public Double mean_job_error;
		public Double empty_time_error;
		public Double empty_arrival_error;
		public Double empty_departure_error;
	}

	public ArrivalGenerator source;
	public DummyNode exogenous_entry;
	public FIFO queue;
	public ExponentialTimeServer server;
	public ProbabilityMultiplexer reEnterPoint;
	public DummyNode exogenous_exit;
	public Sink nSink;

	public SystemStatistics estistica_externa;
	public SystemStatistics estatistica_interna;

	public ArrivalFlowStatistics fluxo_chegadas_compartilhada;
	public DepartureFlowStatistics fluxo_saida_servidor;
	public DepartureFlowStatistics fluxo_saida_sistema;

	public FilaComReentrada(ArrivalGenerator source, Double serverLambda, Double p) {

		this.source = source;
		queue = new FIFO();
		exogenous_entry = new DummyNode();
		server = new ExponentialTimeServer(serverLambda);
		reEnterPoint = new ProbabilityMultiplexer();
		exogenous_exit = new DummyNode();
		nSink = new NullSink();

		source.connectTo(exogenous_entry);
		exogenous_entry.connectFrom(source);
		exogenous_entry.connectTo(queue);
		queue.connectTo(server);
		server.connectFrom(queue);
		server.connectTo(reEnterPoint);
		reEnterPoint.connectTo(queue, p);
		reEnterPoint.connectTo(exogenous_exit, 1 - p);
		exogenous_exit.connectTo(nSink);

		estistica_externa = new SystemStatistics(exogenous_entry, exogenous_exit, "sistema_externo");
		estatistica_interna = new SystemStatistics(queue, server, "sistema_interno");
		fluxo_chegadas_compartilhada = new ArrivalFlowStatistics(queue);
		fluxo_saida_servidor = new DepartureFlowStatistics(server);
		fluxo_saida_sistema = new DepartureFlowStatistics(exogenous_exit);

	}

	public void start(Double time) {
		Timer.timer = new Timer();
		source.start();
		Timer.timer.max_time = time;
		Timer.timer.start();
	}

	public static void main(String[] argc) {

		scenario01(100, 10000D);
		scenario02(100, 10000D);
		scenario03(100, 10000D);
		scenario04(100, 100000D);
		scenario05(100, 100000D);
		scenario06(100, 100000D);

	}

	public static void scenario01(Integer runs, Double time) {
		String dir = "data/cenario01/";
		create_dir(dir);
		ArrayList<Double> lambda = new ArrayList<>();
		for (int i = 1; 0.05 * i < 0.91; i++) {
			lambda.add(i * 0.05);
		}
		Double mi = 1D;

		FilaComReentrada fila = null;
		ArrivalGenerator source;
		ArrayList<Summary> summary = new ArrayList<>();

		for (Double rate : lambda) {
			Nuple samples = new Nuple();

			for (int i = 0; i < runs; i++) {
				source = new ExponentialSource(rate);
				fila = new FilaComReentrada(source, mi, 0D);
				fila.start(time);
				addSample(samples, fila);
			}
			write_run_data(rate, samples, dir);
			calculate_statistics(summary, rate, samples);
			print_data(summary.get(summary.size() - 1));
			write_arrivals(fila, dir, rate);
		}
		write_summary(summary, dir);
	}

	public static void scenario02(Integer runs, Double time) {
		String dir = "data/cenario02/";
		create_dir(dir);
		ArrayList<Double> lambda = new ArrayList<>();
		for (int i = 1; 0.05 * i < 0.91; i++) {
			lambda.add(i * 0.05);
		}
		Double mi = 1D;

		FilaComReentrada fila = null;
		ArrivalGenerator source;
		ArrayList<Summary> summary = new ArrayList<>();

		for (Double rate : lambda) {
			Nuple samples = new Nuple();

			for (int i = 0; i < runs; i++) {
				source = new DeterministicSource(rate);
				fila = new FilaComReentrada(source, mi, 0D);
				fila.start(time);
				addSample(samples, fila);
			}
			write_run_data(rate, samples, dir);
			calculate_statistics(summary, rate, samples);
			print_data(summary.get(summary.size() - 1));
			write_arrivals(fila, dir, rate);
		}
		write_summary(summary, dir);

	}

	public static void scenario03(Integer runs, Double time) {
		String dir = "data/cenario03/";
		create_dir(dir);
		ArrayList<Double> mi = new ArrayList<>();

		for (int i = 0; 0.5 * i < 9.1; i++) {
			mi.add(1 + i * 0.5);
		}

		FilaComReentrada fila = null;
		ArrivalGenerator source;
		ArrayList<Summary> summary = new ArrayList<>();

		for (Double rate : mi) {
			Nuple samples = new Nuple();

			for (int i = 0; i < runs; i++) {
				source = new UniformSource(5D, 15D);
				fila = new FilaComReentrada(source, rate, 0D);
				fila.start(time);
				addSample(samples, fila);
			}
			write_run_data(rate, samples, dir);
			calculate_statistics(summary, rate, samples);
			print_data(summary.get(summary.size() - 1));
			write_arrivals(fila, dir, rate);
		}
		write_summary(summary, dir);

	}

	public static void scenario04(Integer runs, Double time) {
		String dir = "data/cenario04/";
		create_dir(dir);
		ArrayList<Double> mi = new ArrayList<>();

		for (int i = 0; 0.5 * i < 9.1; i++) {
			mi.add(1 + i * 0.5);
		}

		FilaComReentrada fila = null;
		ArrivalGenerator source;
		ArrayList<Summary> summary = new ArrayList<>();

		for (Double rate : mi) {
			Nuple samples = new Nuple();

			for (int i = 0; i < runs; i++) {
				source = new ExponentialSource(0.01D);
				fila = new FilaComReentrada(source, rate, 0.9D);
				fila.start(time);
				addSample(samples, fila);
			}
			write_run_data(rate, samples, dir);
			calculate_statistics(summary, rate, samples);
			print_data(summary.get(summary.size() - 1));
			write_arrivals(fila, dir, rate);
		}
		write_summary(summary, dir);

	}

	public static void scenario05(Integer runs, Double time) {
		String dir = "data/cenario05/";
		create_dir(dir);
		ArrayList<Double> mi = new ArrayList<>();

		for (int i = 0; 0.5 * i < 9.1; i++) {
			mi.add(1 + i * 0.5);
		}

		FilaComReentrada fila = null;
		ArrivalGenerator source;
		ArrayList<Summary> summary = new ArrayList<>();

		for (Double rate : mi) {
			Nuple samples = new Nuple();

			for (int i = 0; i < runs; i++) {
				source = new DeterministicSource(0.01D);
				fila = new FilaComReentrada(source, rate, 0.9D);
				fila.start(time);
				addSample(samples, fila);
			}
			write_run_data(rate, samples, dir);
			calculate_statistics(summary, rate, samples);
			print_data(summary.get(summary.size() - 1));
			write_arrivals(fila, dir, rate);
		}
		write_summary(summary, dir);

	}

	public static void scenario06(Integer runs, Double time) {
		String dir = "data/cenario06/";
		create_dir(dir);
		ArrayList<Double> mi = new ArrayList<>();

		for (int i = 0; 0.5 * i < 9.1; i++) {
			mi.add(1 + i * 0.5);
		}

		FilaComReentrada fila = null;
		ArrivalGenerator source;
		ArrayList<Summary> summary = new ArrayList<>();

		for (Double rate : mi) {
			Nuple samples = new Nuple();

			for (int i = 0; i < runs; i++) {
				source = new UniformSource(50D, 150D);
				fila = new FilaComReentrada(source, rate, 0.9D);
				fila.start(time);
				addSample(samples, fila);
			}
			write_run_data(rate, samples, dir);
			calculate_statistics(summary, rate, samples);
			print_data(summary.get(summary.size() - 1));
			write_arrivals(fila, dir, rate);
		}
		write_summary(summary, dir);

	}

	private static void write_arrivals(FilaComReentrada fila, String dir, Double rate) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat f = new DecimalFormat("0.00", otherSymbols);
		try {
			FileWriter file = new FileWriter(dir + "fluxo_saida_exogeno_" + f.format(rate) + ".data");
			ArrayList<Double> time = fila.fluxo_saida_sistema.departure_time;
			ArrayList<Double> interval = fila.fluxo_saida_sistema.departure_interval;

			for (int i = 0; i < time.size(); i++) {
				String line = "" + time.get(i) + " " + interval.get(i) + "\n";
				file.write(line);
			}
			file.close();

			file = new FileWriter(dir + "fluxo_saida_exogeno_cdf_" + f.format(rate) + ".data");
			Collections.sort(interval);

			for (int i = 0; i < interval.size(); i++) {
				String line = "" + interval.get(i) + " " + ((double) (i + 1)) / interval.size() + "\n";
				file.write(line);
			}
			file.close();

			file = new FileWriter(dir + "fluxo_saida_servidor_" + f.format(rate) + ".data");
			time = fila.fluxo_saida_servidor.departure_time;
			interval = fila.fluxo_saida_servidor.departure_interval;
			for (int i = 0; i < time.size(); i++) {
				String line = "" + time.get(i) + " " + interval.get(i) + "\n";
				file.write(line);
			}
			file.close();

			file = new FileWriter(dir + "fluxo_saida_servidor_cdf_" + f.format(rate) + ".data");
			Collections.sort(interval);

			for (int i = 0; i < interval.size(); i++) {
				String line = "" + interval.get(i) + " " + ((double) (i + 1)) / interval.size() + "\n";
				file.write(line);
			}
			file.close();

			file = new FileWriter(dir + "fluxo_entrada_compartilhada_" + f.format(rate) + ".data");
			time = fila.fluxo_chegadas_compartilhada.arrival_time;
			interval = fila.fluxo_chegadas_compartilhada.arrival_interval;
			for (int i = 0; i < time.size(); i++) {
				String line = "" + time.get(i) + " " + interval.get(i) + "\n";
				file.write(line);
			}
			file.close();

			file = new FileWriter(dir + "fluxo_entrada_compartilhada_cdf_" + f.format(rate) + ".data");
			Collections.sort(interval);

			for (int i = 0; i < interval.size(); i++) {
				String line = "" + interval.get(i) + " " + ((double) (i + 1)) / interval.size() + "\n";
				file.write(line);
			}
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void write_summary(ArrayList<Summary> summary, String dir) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat f = new DecimalFormat("0.00", otherSymbols);
		try {
			FileWriter file = new FileWriter(dir + "summary" + ".data");
			for (Summary s : summary) {
				file.write("" + f.format(s.rate) + " ");
				file.write("" + s.mean_job + " ");
				file.write("" + s.mean_job_error + " ");
				file.write("" + s.empty_time + " ");
				file.write("" + s.empty_time_error + " ");
				file.write("" + s.empty_arrival + " ");
				file.write("" + s.empty_arrival_error + " ");
				file.write("" + s.empty_departure + " ");
				file.write("" + s.empty_departure_error + "\n");
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void print_data(Summary summary) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat f = new DecimalFormat("0.00", otherSymbols);
		System.out.println("Finished: " + f.format(summary.rate));
	}

	private static void calculate_statistics(ArrayList<Summary> summary, Double rate, Nuple samples) {
		Summary s = new Summary();
		s.rate = rate;

		Double mean = calculate_mean(samples.mean_job);
		Double error = calculate_standard_error(samples.mean_job, mean);
		s.mean_job = mean;
		s.mean_job_error = error;

		mean = calculate_mean(samples.empty_arrival);
		error = calculate_standard_error(samples.empty_arrival, mean);
		s.empty_arrival = mean;
		s.empty_arrival_error = error;

		mean = calculate_mean(samples.empty_time);
		error = calculate_standard_error(samples.empty_time, mean);
		s.empty_time = mean;
		s.empty_time_error = error;

		mean = calculate_mean(samples.empty_departure);
		error = calculate_standard_error(samples.empty_departure, mean);
		s.empty_departure = mean;
		s.empty_departure_error = error;

		summary.add(s);

	}

	private static void write_run_data(Double rate, Nuple samples, String dir) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat f = new DecimalFormat("0.00", otherSymbols);
		try {
			FileWriter file = new FileWriter(dir + "rate_" + f.format(rate) + ".data");
			for (int i = 0; i < samples.empty_arrival.size(); i++) {
				String line = "" + samples.mean_job.get(i) + " " + samples.empty_time.get(i) + " "
						+ samples.empty_arrival.get(i) + " " + samples.empty_departure.get(i) + "\n";
				file.write(line);
			}
			file.close();
		} catch (IOException e) {
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

	private static Double calculate_standard_error(ArrayList<Double> samples, Double mean) {
		return 1.96 * calculate_sd(samples, mean);
	}

	private static Double calculate_sd(ArrayList<Double> samples, Double mean) {
		Double sum = 0D;
		for (Double sample : samples) {
			Double d = sample - mean;
			sum += d * d;
		}
		Double var = sum / (samples.size() - 1);
		return Math.sqrt(var / samples.size());
	}

	private static void addSample(Nuple samples, FilaComReentrada fila) {
		samples.mean_job.add(fila.estistica_externa.meanJobInSystem());
		samples.empty_arrival.add(fila.estatistica_interna.arrivalOnEmptyFraction());
		samples.empty_departure.add(fila.estistica_externa.departureOnEmptyFraction());
		samples.empty_time.add(1 - fila.estatistica_interna.utilization());
	}

	private static void create_dir(String dir) {
		File f = new File(dir);
		f.mkdirs();
	}

}