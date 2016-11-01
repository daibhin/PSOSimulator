package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F09_ShiftedRastrigin extends Func {

	static final public String FUNCTION_NAME = "Shifted Rastrigin's Function";
	
	private final double[] o;
	private double[] z;
	
	public F09_ShiftedRastrigin(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		o = new double[dimensions];
		z = new double[dimensions];

		// Load the shifted global optimum
		Benchmark.randomProblemSpaceVector(getUpperBound(), getLowerBound(), dimensions);
	}
	
	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		Benchmark.shift(z, x, o);

		result = Benchmark.rastrigin(z);
		
		return result + bias;
	}

	@Override
	public double getLowerBound() {
		return -5;
	}

	@Override
	public double getUpperBound() {
		return 5;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
