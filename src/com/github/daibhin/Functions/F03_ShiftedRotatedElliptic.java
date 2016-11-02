package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F03_ShiftedRotatedElliptic extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Rotated High Conditioned Elliptic Function";
	
	// Shifted global optimum
	private double[] o;
	private double[][] matrixM;
	private double[] z;
	private double[] zM;
	private	double constant;
	
	private static double bias = -450;
	
	public F03_ShiftedRotatedElliptic (int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		o = new double[dimensions];
		matrixM = new double[dimensions][dimensions];
		z = new double[dimensions];
		zM = new double[dimensions];
		
		this.o = Benchmark.randomProblemSpaceVector(this.getUpperBound(), this.getLowerBound(), dimensions);
		
		constant = Math.pow(1.0e6, (1.0/(dimensions - 1.0)) );
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0;
		
		Benchmark.shift(z, x, o);
		Benchmark.rotate(zM, z, matrixM);
		
		for (int i = 0 ; i < dimensions ; i ++) {
			result += Math.pow(constant, i) * (zM[i] * zM[i]);
		}
		
//		Benchmark.elliptic(x);

		return result + bias;
	}

	@Override
	public double getLowerBound() {
		return -100;
	}

	@Override
	public double getUpperBound() {
		return 100;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
