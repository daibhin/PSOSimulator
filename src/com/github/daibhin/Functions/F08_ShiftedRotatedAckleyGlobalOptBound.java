package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F08_ShiftedRotatedAckleyGlobalOptBound extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Rotated Ackley's Function with Global Optimum on Bounds";
	static final public String OPTIMUM_VALUES_FILE = applicationDirectory + "/Java-ypchen-050309/supportData/ackley_func_data.txt";
	static final public String MATRIX_VALUES_FILE_PREFIX = applicationDirectory + "/Java-ypchen-050309/supportData/ackley_M_D";
	
	// Shifted global optimum
	private double[] o;
	private double[][] matrix;
	private double[] z;
	private double[] zM;
	
	public F08_ShiftedRotatedAckleyGlobalOptBound(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		o = new double[dimensions];
		matrix = new double[dimensions][dimensions];

		z = new double[dimensions];
		zM = new double[dimensions];
		
		// Load the shifted global optimum
//		this.o = Benchmarker.randomProblemSpaceVector(this.getLowerBound(), this.getUpperBound(), dimensions);
		// Load the shifted global optimum
		Benchmarker.loadRowVectorFromFile(OPTIMUM_VALUES_FILE, dimensions, o);
		// Load the matrix
		String matrixFile = MATRIX_VALUES_FILE_PREFIX + dimensions + DEFAULT_FILE_SUFFIX;
		Benchmarker.loadMatrixFromFile(matrixFile, dimensions, dimensions, matrix);

		for (int i = 0 ; i < dimensions ; i += 2) {
			this.o[i] = -32.0;
		}
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;
		
		Benchmarker.shift(z, x, o);
		Benchmarker.rotate(zM, z, matrix);

		result = Benchmarker.ackley(zM);

		return result + bias;
	}

	@Override
	public double getUpperBound() {
		return 32;
	}

	@Override
	public double getLowerBound() {
		return -32;
	}

}
