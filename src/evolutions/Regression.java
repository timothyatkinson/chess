package evolutions;

import nets.Network;

public class Regression {

	public static double[][] x6x4x(int values, double min_in, double max_in){
		double[][] val = new double[2][values];
		for(int i = 0; i < values; i++){
			double in = Network.randDouble(min_in, max_in);
			double out = Math.pow(in, 6) - Math.pow(in, 4) + Math.pow(in, 2);
			val[0][i] = in;
			val[1][i] = out;
		}
		return val;
	}
	
	public static double score(Network n, double[][] data, int inputs){
		double error = 0.0;
		for(int i = 0; i < data[0].length; i++){
			double[] row = new double[inputs];
			for(int j = 0; j < inputs; j++){
				row[j] = data[j][i];
			}
			double out = 0.0;
			try{
				out = n.tick(row)[0];
			}
			catch (Exception e){
			
			}
			error = error + Math.pow(out - data[inputs][i], 2);
		}
		return error;
	}
	

	
	public static double scoreDebug(Network n, double[][] data, int inputs){
		double error = 0.0;
		for(int i = 0; i < data[0].length; i++){
			System.out.print("In: ");
			double[] row = new double[inputs];
			for(int j = 0; j < inputs; j++){
				row[j] = data[j][i];
				System.out.print(row[j] + ", ");
			}
			double out = 0.0;
			try{
				out = n.tick(row)[0];
			}
			catch (Exception e){
			
			}
			System.out.println(" expects: " + data[inputs][i] + " gets: " + out + " error: " + Math.pow(out - data[inputs][i], 2) + "\n");
			error = error + Math.pow(out - data[inputs][i], 2);
		}
		return error;
	}
	
	public static void lambdaRegression(int lambda, int maxgen, double[][] values, int inputs, int nodes, int conns, double const_rate, double conn_rate){
		
		int gen = 0;
		Network parent = Network.randNetwork(inputs, 1, nodes, 5, 0, -1, 1);
		while(gen < maxgen){
			
		}
	}
}
