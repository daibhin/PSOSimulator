package com.github.daibhin;

public class GriewankFunction extends Function {

	@Override
	public double evaluate(Position position) {
		double sum = 0;
		double product = 1;
		double[] values = position.getValues();
		
		for (int i=0; i < values.length; i++) {
			double xi = values[i];
			sum += (Math.pow(xi, 2)/4000);
			product *= Math.cos(xi/Math.sqrt(xi));
		}
		return sum - product + 1;
	}

}
