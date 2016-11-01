package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F11_ShiftedRotatedWeierstrass extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Rotated Weierstrass Function";
	
	// Shifted global optimum
	private double[] o;
	private final double[][] matrix;
	private double[] z;
	private double[] zM;

	public F11_ShiftedRotatedWeierstrass(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);

		// Note: dimension starts from 0
		o = new double[dimensions];
		matrix = new double[dimensions][dimensions];
		z = new double[dimensions];
		zM = new double[dimensions];
		
		this.o = Benchmark.randomProblemSpaceVector(getUpperBound(), getLowerBound(), dimensions);
		// Load the matrix
//		Benchmark.loadMatrixFromFile(file_m, dimensions, dimensions, matrix);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		Benchmark.shift(z, x, o);
		Benchmark.xA(zM, z, matrix);
		result = Benchmark.weierstrass(zM);

		return result + bias;
	}

	@Override
	public double getUpperBound() {
		return 0.5;
	}

	@Override
	public double getLowerBound() {
		return -0.5;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		// TODO Auto-generated method stub
		return false;
	}

}
