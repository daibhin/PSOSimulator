package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class Rosenbrock extends Function {
	
	private static final String FUNCTION_NAME = "Rosenbrock Function";

	public Rosenbrock(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();		
		return Benchmarker.rosenbrock(x);
	}

	@Override
	public double getLowerBound() {
		return -2.048;
	}

	@Override
	public double getUpperBound() {
		return 2.048;
	}

}
