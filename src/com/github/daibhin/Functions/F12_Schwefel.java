package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F12_Schwefel extends Function {
	
	static final public String FUNCTION_NAME = "Schwefel's Problem 2.13";
	static final public String FILE_DATA = applicationDirectory + "/Java-ypchen-050309/supportData/schwefel_213_data.txt";
	
	// Shifted global optimum
	private final double[] alpha;
	private final double[][] a;
	private final double[][] b;

	private double[] A;
	private double[] B;

	public F12_Schwefel(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		alpha = new double[dimensions];
		a = new double[dimensions][dimensions];
		b = new double[dimensions][dimensions];

		A = new double[dimensions];
		B = new double[dimensions];
		
		// Data:
		//	1. a 		100x100
		//	2. b 		100x100
		//	3. alpha	1x100
		double[][] data = new double[100+100+1][dimensions];

		// Load the matrix
		Benchmarker.loadMatrixFromFile(FILE_DATA, data.length, dimensions, data);
		for (int i=0; i < dimensions; i ++) {
			for (int j = 0 ; j < dimensions; j ++) {
				a[i][j] = data[i][j];
				b[i][j] = data[100+i][j];
			}
			alpha[i] = data[100+100][i];
		}
		for (int i =0; i < dimensions; i ++) {
			A[i] = 0.0;
			for (int j=0; j < dimensions; j ++) {
				A[i] += a[i][j] * Math.sin(alpha[j]) + b[i][j] * Math.cos(alpha[j]);
			}
		}
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double sum = 0.0;

		for (int i=0; i < dimensions; i++) {
			B[i] = 0.0;
			for (int j=0; j < dimensions; j ++) {
				B[i] += a[i][j] * Math.sin(x[j]) + b[i][j] * Math.cos(x[j]);
			}
			double subtraction = A[i] - B[i];
			sum += subtraction * subtraction;
		}

		return sum + bias;
	}

	@Override
	public double getUpperBound() {
		return Math.PI;
	}

	@Override
	public double getLowerBound() {
		return - Math.PI;
	}

}
