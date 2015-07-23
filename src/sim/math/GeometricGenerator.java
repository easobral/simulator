package sim.math;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GeometricGenerator implements RandomNumberGenerator{
	private double constant;
	/**
	 * 
	 * @param p  probabilidade de sucesso
	 */
	public GeometricGenerator(double p) {
		constant=1/Math.log(1-p);
	}

	/**
	 * 
	 * @param m a média da distribuição
	 * @return um gerador geométrico com média m
	 */
	public static GeometricGenerator mediaConstructor(double m) {
		return new GeometricGenerator(1/m);
	}

	public int getAsInt(){
		return (int) get();
	}
	
	@Override
	public double get() {
		return (int) Math.ceil(constant*Math.log(1-Math.random()));
	}

	public static void main(String[] args) {
		FileOutputStream file;
		GeometricGenerator gen = mediaConstructor(10);

		try {
			file = new FileOutputStream(new File("geometric.dat"));

			Integer max =100000;
			Integer sum=0;
			
			for (int i =0; i< max; i++){
				Integer rand = gen.getAsInt();
				sum+=rand;
				file.write(rand.toString().getBytes());
				file.write("\n".getBytes());
			}
			Double mean = (double) (sum/max);
			
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
