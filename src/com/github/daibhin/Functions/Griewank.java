package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class Griewank extends Func {
	
	private static final String FUNCTION_NAME = "Griewank Function";

	public Griewank(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		return Benchmarker.griewank(x);
	}

	@Override
	public double getLowerBound() {
		return -600;
	}

	@Override
	public double getUpperBound() {
		return 600;
	}

}
