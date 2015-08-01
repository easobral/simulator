package sim.main;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		a();
	}
	
	static class X{
		public Double a;
	}
	
	public static void b (){
		X x =  new X();
		x.a=1D;
		C(x);
		System.out.println(x.a);
	}
	
	public static void C(X c){
		c.a=2D;
	}
	
	public static void a(){
		ArrayList<Double> lambda = new ArrayList<>();
		for (int i = 1; 0.05 * i < 0.91; i++) {
			lambda.add(i * 0.05);
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat lambda_format = new DecimalFormat("0.00",otherSymbols);

		for (int i = 1; i< lambda.size(); i++) {
			System.out.println(lambda_format.format(lambda.get(i)));
			System.out.println(lambda.get(i));
		}
		
		System.out.println(0.05D*6);
		
	}

}
