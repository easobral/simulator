package sim.main;

import sim.network.MM1;

public class Main {

	/**
	 * @param args
	 */

	public static Long Cg = 0L;
	public static Long Cs = 0L;
	public static Double ackTime1 = 0D;
	public static Double ackTime2 = 0D;
	public static Double bGTime = 0D;
	public static Long bGSize = 0L;
	public static Long buffer = 0L;
	public static Integer politic = 0;

	public static final Integer FIFO = 0;
	public static final Integer RED = 0;

	public static Double lambda=1D;
	public static Double mi=1D;
	public static Integer runs=1;
	public static String file="MM1.dat";
	
	public static Double tempo_na_fila=0D;
	public static Double clientes_na_fila=0D;
	public static Double tempo_no_servidor=0D;
	public static Double clientes_no_servidor=0D;	
	
	
	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-l"))
				lambda = Double.parseDouble(args[++i<args.length?i:i-1]);
			if (args[i].equalsIgnoreCase("-m"))
				mi = Double.parseDouble(args[++i<args.length?i:i-1]);
			if (args[i].equalsIgnoreCase("-r"))
				runs = Integer.parseInt(args[++i<args.length?i:i-1]);
			if (args[i].equalsIgnoreCase("-f"))
				file =args[++i<args.length?i:i-1];
		}
		Long time = System.currentTimeMillis();
		
		for(int i = 0; i<runs; i++){
			run();
		}

		time = System.currentTimeMillis() - time;
		Double rho = lambda/mi;
		
		System.out.println("Valor Obtido Tempo na fila: " + tempo_na_fila/runs);
		System.out.println("Valor esperado Tempo na fila: " + rho/(mi -lambda));
		System.out.println("Valor Obtido Clientes na fila: " + clientes_na_fila/runs);
		System.out.println("Valor Esperado Clientes na fila: " + (lambda/(mi-lambda) - rho));
		System.out.println("Valor Obtido Período Ocupado: " + tempo_no_servidor/runs);
		System.out.println("Valor Esperado Período Ocupado: "+ rho );
		System.out.println("Tempo de processamento(médio/total):" +time/runs +"/"+time);
		
	}
	
	static void run (){
		MM1 n = new MM1(lambda,mi,file);
		
		n.start();

		System.out.println("Tempo na fila: " + n.queue.meanQueueTime());
		tempo_na_fila+= n.queue.meanQueueTime();
		System.out.println("Clientes na fila: " + n.queue.meanJob());
		clientes_na_fila+= n.queue.meanJob();
		System.out.println("Tempo no servidor: " + n.server.meanServerTime());
		tempo_no_servidor+=n.server.meanServerTime();
		System.out.println("Clientes no servidor: " + n.server.meanJob());
		clientes_no_servidor+=n.server.meanJob();
		System.out.println("Little: " + lambda*n.queue.meanQueueTime() + " = " + n.queue.meanJob());
		System.out.println("Little: " + lambda/mi + " = " + n.server.meanJob());
		System.out.println("Período Ocupado:" + lambda/mi + " = " + n.server.meanJob());
		System.out.println("\n\n----------------------------------------------------------\n\n");

		n.close();
		
		
	}

}
