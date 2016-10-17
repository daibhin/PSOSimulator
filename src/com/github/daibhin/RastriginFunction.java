package com.github.daibhin;

public class RastriginFunction extends Function {

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

}
