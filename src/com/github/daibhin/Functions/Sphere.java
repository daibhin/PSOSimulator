package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class Sphere extends Function {
	
	static final public String FUNCTION_NAME = "Sphere Function";

	public Sphere(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		return Benchmarker.sphere(x);
	}

	@Override
	public double getUpperBound() {
		return 5.12;
	}

	@Override
	public double getLowerBound() {
		return -5.12;
	}

}
