package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class Rastrigin extends Function {
	
	private static final String FUNCTION_NAME = "Rastrigin Function";

	public Rastrigin(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		return Benchmarker.rastrigin(x);
	}

	@Override
	public double getLowerBound() {
		return -5.12;
	}

	@Override
	public double getUpperBound() {
		return 5.12;
	}

}
