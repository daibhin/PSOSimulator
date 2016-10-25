package com.github.daibhin.Functions;

import com.github.daibhin.Functions.Function;
import com.github.daibhin.Position;

public class RastriginFunction implements Function {

	@Override
	public double evaluate(Position position) {
		double[] values = position.getValues();
		int dimensions = values.length;
		double sum = 0.0;
		for (int i=0; i < dimensions; i++) {
			double xi = values[i];
			sum += Math.pow(xi, 2) - 10*Math.cos(2*Math.PI*xi);
		}
		return 10*dimensions + sum;
	}

	@Override
	public double getLowerBound() {
		return -5.12;
	}

	@Override
	public double getUpperBound() {
		return 5.12;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		// TODO Auto-generated method stub
		return false;
	}

}
