package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F08_ShiftedRotatedAckleyGlobalOptBound extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Rotated Ackley's Function with Global Optimum on Bounds";

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
		this.o = Benchmark.randomProblemSpaceVector(this.getLowerBound(), this.getUpperBound(), dimensions);
		// Load the matrix
		// NOT COMPLETE
//		benchmark.loadMatrixFromFile(file_m, m_dimension, m_dimension, m_matrix);

		for (int i = 0 ; i < dimensions ; i += 2) {
			this.o[i] = -32.0;
		}
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;
		
		Benchmark.shift(z, x, o);
		Benchmark.rotate(zM, z, matrix);

		result = Benchmark.ackley(zM);

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

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}
}
