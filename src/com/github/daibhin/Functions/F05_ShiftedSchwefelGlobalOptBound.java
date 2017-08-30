package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F05_ShiftedSchwefelGlobalOptBound extends Function {
	
	static final public String FUNCTION_NAME = "Schwefel's Problem 2.6 with Global Optimum on Bounds";
	static final public String SHIFTED_OPTIMUM_POINTS = applicationDirectory + "/Java-ypchen-050309/supportData/schwefel_206_data.txt";

	
	// Shifted global optimum
	private double[] o;
	private double[][] A;
	private double[] B;
	private double[] z;
	
	public F05_ShiftedSchwefelGlobalOptBound (int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		this.o = new double[dimensions];
		this.A = new double[dimensions][dimensions];
		this.B = new double[dimensions];
		this.z = new double[dimensions];
		
		double[][] data = new double[dimensions+1][dimensions];
		
		// Load the shifted global optimum
//		this.o = Benchmarker.randomProblemSpaceVector(getUpperBound(), getLowerBound(), dimensions);
		Benchmarker.loadMatrixFromFile(SHIFTED_OPTIMUM_POINTS, dimensions+1, dimensions, data);
		
		for (int i = 0; i < dimensions; i ++) {
			if ((i+1) <= Math.ceil(dimensions / 4.0)) {
				this.o[i] = -100.0;
			}
			else if ((i+1) >= Math.floor((3.0 * dimensions) / 4.0)) {
				this.o[i] = 100.0;
			}
			else {
				this.o[i] = data[0][i];
			}
		}
		for (int i=0; i < dimensions; i ++) {
			for (int j=0; j < dimensions; j ++) {
				A[i][j] = data[i+1][j];
			}
		}
		Benchmarker.Ax(B, A, o);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double max = Double.NEGATIVE_INFINITY;

		Benchmarker.Ax(z, A, x);

		for (int i = 0 ; i < dimensions ; i ++) {
			double temp = Math.abs(z[i] - B[i]);
			if (max < temp)
				max = temp;
		}

		return max + bias;
	}

	@Override
	public double getLowerBound() {
		return -100;
	}

	@Override
	public double getUpperBound() {
		return 100;
	}

}
