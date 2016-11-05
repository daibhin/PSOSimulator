package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class Ackley extends Func {
	
	private static final String FUNCTION_NAME = "Ackley Function";
	
	public Ackley(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		return Benchmarker.ackley(x);
	}

	@Override
	public double getLowerBound() {
		return -32;
	}

	@Override
	public double getUpperBound() {
		return 32;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
