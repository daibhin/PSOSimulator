package com.github.daibhin;

public class SphereFunction extends Function {

	public double evaluate(Position position) {
		double sum = 0;
		double[] values = position.getValues();
		
		for (int i=0; i < values.length; i++) {
			sum += Math.pow(values[i], 2);
		}
		if (sum == 0.0) {
			double[] sum2 = new double[values.length];
			for (int i=0; i < values.length; i++) {
				sum2[i] = Math.pow(values[i], 2);
			}
			String me = "hello";
			String me2 = me;
		}
		return sum;
	}

	public boolean isMinimised(Position position) {
		double fitness = this.evaluate(position);
		return fitness == 0;
	}
}
