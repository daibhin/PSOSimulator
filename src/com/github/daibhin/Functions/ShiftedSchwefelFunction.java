package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public class ShiftedSchwefelFunction implements Function {
	
	double bias = -450;

	@Override
	public double evaluate(Position position) {
		double sum = 0;
		double[] values = position.getValues();
		
		for (int i=0; i < values.length; i++) {
			for (int j=0; j < i; j++) {
				double z = values[j] - bias;
				sum += (Math.pow(z, 2));
			}
		}
		return sum + bias;
	}

	@Override
	public double getOptimum() {
		return bias;
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
		// TODO Auto-generated method stub
		return false;
	}
}
