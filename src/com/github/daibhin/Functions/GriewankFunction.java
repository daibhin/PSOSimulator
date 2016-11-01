package com.github.daibhin.Functions;

import com.github.daibhin.Functions.Function;
import com.github.daibhin.Position;

public class GriewankFunction implements Function {

	@Override
	public double evaluate(Position position) {
		double sum = 0;
		double product = 1;
		double[] values = position.getValues();
		
		for (int i=0; i < values.length; i++) {
			double xi = values[i];
			sum += Math.pow(xi, 2)/4000;
			product *= Math.cos(xi/Math.sqrt(i+1));
		}
		return sum - product + 1;
	}

	@Override
	public double getLowerBound() {
		return -600;
	}

	@Override
	public double getUpperBound() {
		return 600;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
