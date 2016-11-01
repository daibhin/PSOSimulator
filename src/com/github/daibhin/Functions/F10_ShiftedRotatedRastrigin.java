package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F10_ShiftedRotatedRastrigin extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Rotated Rastrigin's Function";
	
	// Shifted global optimum
	private double[] o;
	private double[][] matrix;
	private double[] z;
	private double[] zM;

	public F10_ShiftedRotatedRastrigin(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);

		o = new double[dimensions];
		matrix = new double[dimensions][dimensions];
		z = new double[dimensions];
		zM = new double[dimensions];
		
		// Load the shifted global optimum
		this.o = Benchmark.randomProblemSpaceVector(getUpperBound(), getLowerBound(), dimensions);
		
		// Load the matrix
//		benchmark.loadMatrixFromFile(file_m, m_dimension, m_dimension, m_matrix);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		Benchmark.shift(z, x, o);
		Benchmark.rotate(zM, z, matrix);
		result = Benchmark.rastrigin(zM);
		
		return result + bias;
	}

	@Override
	public double getUpperBound() {
		return 5;
	}

	@Override
	public double getLowerBound() {
		return -5;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
