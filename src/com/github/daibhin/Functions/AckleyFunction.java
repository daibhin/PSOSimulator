package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public class AckleyFunction implements Function {
	
	private static double A = 2.0;
	private static double B = 0.2;
	private static double C = 2.0*Math.PI;

	@Override
	public double evaluate(Position position) {
		double[] values = position.getValues();
		double dimInverse = Math.pow(values.length, -1);
		
		double z = -B * Math.sqrt(dimInverse*squaredSum(values));
		double x = -A*Math.exp(z);
		
		double y = - Math.exp(dimInverse*cosSum(values));
		
		return x + y + A + Math.exp(1);
	}
	
	private double cosSum(double[] values) {
		double sum = 0;
		for (int i=0; i < values.length; i++) {
			sum += Math.cos(C*values[i]);
		}
		return sum;
	}

	private static double squaredSum(double[] values) {
		double sum = 0;
		for (int i=0; i < values.length; i++) {
			sum += Math.pow(values[i], 2);
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

}
