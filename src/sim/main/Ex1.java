package sim.main;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Ex1 {

	public Ex1() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		x1();
		x2();
	}

	public static void x1() {
		ArrayList<Double> lambda = new ArrayList<>();
		for (int i = 1; 0.05 * i < 0.91; i++) {
			lambda.add(i * 0.05);
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat f = new DecimalFormat("0.00", otherSymbols);

		try {
			FileWriter file = new FileWriter("resultado_analitico_1.data");

			for (Double l : lambda) {
				Double val = (l / (1 - l));
				file.write(f.format(l) + " " + val + "\n");
			}

			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void x2() {
		ArrayList<Double> mi = new ArrayList<>();
		for (int i = 0; 1 + 0.5 * i < 10.1; i++) {
			mi.add(1 + i * 0.5);
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat f = new DecimalFormat("0.00", otherSymbols);

		try {
			FileWriter file = new FileWriter("resultado_analitico_4.data");

			for (Double m : mi) {
				Double l = 0.01;
				Double p = 0.9;
				Double beta = l / (1 - p);
				Double ro = beta / m;
				Double val = (ro / (1 - ro));
				file.write(f.format(m) + " " + val + "\n");
			}

			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
