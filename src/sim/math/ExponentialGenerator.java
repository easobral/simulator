package sim.math;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExponentialGenerator implements RandomNumberGenerator {

	private double lambda;

	public ExponentialGenerator(double lambda) {
		this.lambda=lambda;
	}

	@Override
	public double get() {
		return -Math.log(Math.random())/lambda;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileOutputStream file;
		ExponentialGenerator gen = new ExponentialGenerator(5);

		try {
			file = new FileOutputStream(new File("exponential.dat"));

			int max =100000;
			Double sum=0.0;
			
			for (int i =0; i< max; i++){
				Double rand = gen.get();
				sum+=rand;
				file.write(rand.toString().getBytes());
				file.write("\n".getBytes());
			}
			Double mean = sum/max;
			
			System.out.println("Mean= "+mean);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i=0; i<9; i++){
			int max =100000;
			Double sum=0.0;
			
			for (int j =0; j< max; j++){
				Double rand = gen.get();
				sum+=rand;
			}
			Double mean = sum/max;
			
			System.out.println("Mean= "+mean);

		}
		
	}
	
}
