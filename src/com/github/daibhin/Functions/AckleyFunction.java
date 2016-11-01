package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public class AckleyFunction implements Function {
	
	private static double A = 20;
	private static double B = 0.2;
	private static double C = 2.0*Math.PI;

	@Override
	public double evaluate(Position position) {
		double[] values = position.getValues();
		double inverseDimension = Math.pow(values.length, -1);
		
		double x = Math.exp(-B * Math.sqrt(inverseDimension * Function.squaredSum(values)));		
		double y = Math.exp(inverseDimension * cosSum(values));
		
		return (-A * x) - y + A + Math.exp(1);
	}
	
	private double cosSum(double[] values) {
		double sum = 0;
		for (int i=0; i < values.length; i++) {
			sum += Math.cos(C * values[i]);
		}
		return sum;
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
