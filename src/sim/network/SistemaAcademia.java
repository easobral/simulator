package sim.network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import sim.components.basic.DummyNode;
import sim.components.basic.NullSink;
import sim.components.basic.ProbabilityMultiplexer;
import sim.components.generators.ArrivalGenerator;
import sim.components.generators.ExponentialSource;
import sim.components.queue.FIFO;
import sim.components.servers.ExponentialTimeServer;
import sim.components.statistics.ArrivalFlowStatistics;
import sim.components.statistics.DepartureFlowStatistics;
import sim.components.statistics.SystemStatistics;
import sim.timer.Timer;

public class SistemaAcademia {

	static class Nuple {
		public ArrayList<Double> utilizacao_esteira = new ArrayList<>();
		public ArrayList<Double> utilizacao_bike = new ArrayList<>();
		public ArrayList<Double> tempo_medio_sistema = new ArrayList<>();
	}

	static class Summary {
		public Double media_tempo = 0D;
		public Double error_tempo = 0D;
		public Double media_utilizacao_esteira = 0D;
		public Double error_utilizacao_esteira = 0D;
		public Double media_utilizacao_bike = 0D;
		public Double error_utilizacao_bike = 0D;
	}

	ArrivalGenerator gerador_entrada;
	DummyNode entrada_sistema;
	FIFO fila_esteira;
	ExponentialTimeServer servidor_esteira;
	ProbabilityMultiplexer esteira_multiplexer;
	DummyNode esteira_para_fora;
	FIFO fila_bike;
	ExponentialTimeServer servidor_bike;
	ProbabilityMultiplexer bike_multiplexer;
	DummyNode bike_saida_sistema;
	DummyNode saida_sistema;
	DummyNode bike_retorno_esteira;
	NullSink sink;

	SystemStatistics sistema;
	SystemStatistics esteira;
	SystemStatistics bike;

	ArrivalFlowStatistics entrada_exogena;
	ArrivalFlowStatistics entrada_agregada;
	ArrivalFlowStatistics entrada_bike;
	DepartureFlowStatistics saida_bike;
	DepartureFlowStatistics retorno_esteira;
	DepartureFlowStatistics saida_esteira;
	DepartureFlowStatistics fluxo_saida_sistema;

	public SistemaAcademia(Double p, Double q, Double miE, Double miB, Double lambda) {

		gerador_entrada = new ExponentialSource(lambda);
		entrada_sistema = new DummyNode();
		fila_esteira = new FIFO();
		servidor_esteira = new ExponentialTimeServer(miE);
		esteira_multiplexer = new ProbabilityMultiplexer();
		esteira_para_fora = new DummyNode();
		fila_bike = new FIFO();
		servidor_bike = new ExponentialTimeServer(miB);
		bike_multiplexer = new ProbabilityMultiplexer();
		bike_saida_sistema = new DummyNode();
		saida_sistema = new DummyNode();
		bike_retorno_esteira = new DummyNode();
		sink = new NullSink();

		sistema = new SystemStatistics(entrada_sistema, saida_sistema, "sistema");
		esteira = new SystemStatistics(fila_esteira, servidor_esteira, "esteira");
		bike = new SystemStatistics(fila_bike, servidor_bike, "bike");

		entrada_exogena = new ArrivalFlowStatistics(entrada_sistema);
		entrada_agregada = new ArrivalFlowStatistics(fila_esteira);
		entrada_bike = new ArrivalFlowStatistics(fila_bike);
		saida_bike = new DepartureFlowStatistics(bike_saida_sistema);
		retorno_esteira = new DepartureFlowStatistics(bike_retorno_esteira);
		saida_esteira = new DepartureFlowStatistics(esteira_para_fora);
		fluxo_saida_sistema = new DepartureFlowStatistics(saida_sistema);

		gerador_entrada.connectTo(entrada_sistema);
		entrada_sistema.connectTo(fila_esteira);
		fila_esteira.connectTo(servidor_esteira);
		servidor_esteira.connectFrom(fila_esteira);
		servidor_esteira.connectTo(esteira_multiplexer);

		esteira_multiplexer.connectTo(fila_bike, p);
		esteira_multiplexer.connectTo(esteira_para_fora, 1 - p);
		esteira_para_fora.connectTo(saida_sistema);

		fila_bike.connectTo(servidor_bike);
		servidor_bike.connectFrom(fila_bike);
		servidor_bike.connectTo(bike_multiplexer);
		bike_multiplexer.connectTo(bike_saida_sistema, q);
		bike_multiplexer.connectTo(bike_retorno_esteira, 1 - q);
		bike_retorno_esteira.connectTo(fila_esteira);
		bike_saida_sistema.connectTo(saida_sistema);

		saida_sistema.connectTo(sink);

	}

	public void start(Double time) {
		Timer.timer = new Timer();
		gerador_entrada.start();
		Timer.timer.max_time = time;
		Timer.timer.start();
	}

	public static void main(String[] argc) {
		cenario01(100, 10000D);
		cenario02(100, 10000D);
		cenario03(100, 10000D);
	}

	private static void cenario01(Integer runs, Double time) {
		String dir = "data/academia/01/";
		create_dir(dir);
		SistemaAcademia sistema = null;
		Nuple samples = new Nuple();

		Summary summary = null;
		for (int i = 0; i < runs; i++) {
			sistema = new SistemaAcademia(1D, 0.9D, 5D, 5D, 0.1D);
			sistema.start(time);
			addSample(samples, sistema);
		}
		write_run_data(samples, dir);
		summary = calculate_statistics(samples);
		print_data(summary);
		write_arrivals(sistema, dir);
		write_summary(summary, dir);
		write_jobs_vs_time(sistema, dir);

	}

	private static void cenario02(Integer runs, Double time) {
		String dir = "data/academia/02/";
		create_dir(dir);
		SistemaAcademia sistema = null;
		Nuple samples = new Nuple();

		Summary summary = null;
		for (int i = 0; i < runs; i++) {
			sistema = new SistemaAcademia(0.1D, 0.9D, 1D, 1D, 0.1D);
			sistema.start(time);
			addSample(samples, sistema);
		}
		write_run_data(samples, dir);
		summary = calculate_statistics(samples);
		print_data(summary);
		write_arrivals(sistema, dir);
		write_summary(summary, dir);
		write_jobs_vs_time(sistema, dir);

	}

	private static void cenario03(Integer runs, Double time) {
		String dir = "data/academia/03/";
		create_dir(dir);
		SistemaAcademia sistema = null;
		Nuple samples = new Nuple();

		Summary summary = null;
		for (int i = 0; i < runs; i++) {
			sistema = new SistemaAcademia(0.9D, 0.9D, 1D, 1D, 10D);
			sistema.start(time);
			addSample(samples, sistema);
		}
		write_run_data(samples, dir);
		summary = calculate_statistics(samples);
		print_data(summary);
		write_arrivals(sistema, dir);
		write_summary(summary, dir);
		write_jobs_vs_time(sistema, dir);
	}

	private static void write_jobs_vs_time(SistemaAcademia sis, String dir) {
		try {
			SystemStatistics s = sis.sistema;

			FileWriter file;
			file = new FileWriter(dir + "time_job_sis.data");

			for (int i = 0; i < s.time.size(); i++) {
				file.write(s.time.get(i) + " " + s.jobs.get(i) + "\n");
			}

			file.close();

			s = sis.esteira;
			file = new FileWriter(dir + "time_job_esteira.data");
			for (int i = 0; i < s.time.size(); i++) {
				file.write(s.time.get(i) + " " + s.jobs.get(i) + "\n");
			}

			file.close();

			s = sis.bike;
			file = new FileWriter(dir + "time_job_bike.data");
			for (int i = 0; i < s.time.size(); i++) {
				file.write(s.time.get(i) + " " + s.jobs.get(i) + "\n");
			}

			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void write_summary(Summary summary, String dir) {
		try {
			FileWriter file = new FileWriter(dir + "summary" + ".data");
			file.write(summary.media_tempo + " ");
			file.write(summary.error_tempo + " ");
			file.write(summary.media_utilizacao_esteira + " ");
			file.write(summary.error_utilizacao_esteira + " ");
			file.write(summary.media_utilizacao_bike + " ");
			file.write("" + summary.error_utilizacao_bike);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void write_arrivals(SistemaAcademia sis, String dir) {
		try {
			ArrayList<Double> time = sis.entrada_exogena.arrival_time;
			ArrayList<Double> interval = sis.entrada_exogena.arrival_interval;
			write_flux(dir + "entrada_exogena", time, interval);

			time = sis.entrada_agregada.arrival_time;
			interval = sis.entrada_agregada.arrival_interval;
			write_flux(dir + "entrada_agregada", time, interval);

			time = sis.entrada_bike.arrival_time;
			interval = sis.entrada_bike.arrival_interval;
			write_flux(dir + "entrada_bike", time, interval);

			time = sis.saida_esteira.departure_time;
			interval = sis.saida_esteira.departure_interval;
			write_flux(dir + "saida_esteira", time, interval);

			time = sis.saida_bike.departure_time;
			interval = sis.saida_bike.departure_interval;
			write_flux(dir + "saida_bike", time, interval);

			time = sis.retorno_esteira.departure_time;
			interval = sis.retorno_esteira.departure_interval;
			write_flux(dir + "retorno_esteira", time, interval);

			time = sis.fluxo_saida_sistema.departure_time;
			interval = sis.fluxo_saida_sistema.departure_interval;
			write_flux(dir + "saida_sistema", time, interval);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void write_flux(String fname, ArrayList<Double> time, ArrayList<Double> interval)
			throws IOException {
		FileWriter file = new FileWriter(fname + ".data");

		for (int i = 0; i < time.size(); i++) {
			String line = "" + time.get(i) + " " + interval.get(i) + "\n";
			file.write(line);
		}
		file.close();

		file = new FileWriter(fname + "_cdf.data");
		Collections.sort(interval);

		for (int i = 0; i < interval.size(); i++) {
			String line = "" + interval.get(i) + " " + ((double) (i + 1)) / interval.size() + "\n";
			file.write(line);
		}
		file.close();

	}

	private static void write_run_data(Nuple samples, String dir) {
		try {
			FileWriter file = new FileWriter(dir + "run" + ".data");
			for (int i = 0; i < samples.tempo_medio_sistema.size(); i++) {
				String line = "" + samples.tempo_medio_sistema.get(i) + " " + samples.utilizacao_esteira.get(i) + " "
						+ samples.utilizacao_bike.get(i) + "\n";
				file.write(line);
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void print_data(Summary summary) {
		System.out.println("Media no tempo: " + summary.media_tempo);
		System.out.println("Erro da media: " + summary.error_tempo);
		System.out.println("Media da utilização da esteira: " + summary.media_utilizacao_esteira);
		System.out.println("Erro utilizacao da esteira: " + summary.error_utilizacao_esteira);
		System.out.println("Media utilização bike: " + summary.media_utilizacao_bike);
		System.out.println("Erro utilização bike: " + summary.error_utilizacao_bike);
		System.out.println("----------------------------------------------------------------");
	}

	private static Summary calculate_statistics(Nuple samples) {
		Summary s = new Summary();

		Double mean = calculate_mean(samples.tempo_medio_sistema);
		Double error = calculate_standard_error(samples.tempo_medio_sistema, mean);
		s.media_tempo = mean;
		s.error_tempo = error;

		mean = calculate_mean(samples.utilizacao_esteira);
		error = calculate_standard_error(samples.utilizacao_esteira, mean);
		s.media_utilizacao_esteira = mean;
		s.error_utilizacao_esteira = error;

		mean = calculate_mean(samples.utilizacao_bike);
		error = calculate_standard_error(samples.utilizacao_bike, mean);
		s.media_utilizacao_bike = mean;
		s.error_utilizacao_bike = error;

		return s;
	}

	public static Double calculate_mean(ArrayList<Double> samples) {
		Double sum = 0D;
		for (Double sample : samples) {
			sum += sample;
		}
		return sum / samples.size();
	}

	public static Double calculate_standard_error(ArrayList<Double> samples, Double mean) {
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

	private static void addSample(Nuple samples, SistemaAcademia sis) {
		samples.utilizacao_bike.add(sis.bike.utilization());
		samples.utilizacao_esteira.add(sis.esteira.utilization());
		samples.tempo_medio_sistema.add(sis.sistema.meanTimeInSystem());
	}

	public static void create_dir(String dir) {
		File f = new File(dir);
		f.mkdirs();
	}
}
