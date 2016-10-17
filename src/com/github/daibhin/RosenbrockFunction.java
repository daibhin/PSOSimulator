package com.github.daibhin;

public class RosenbrockFunction extends Function {

	@Override
	public double evaluate(Position position) {
		double sum = 0;
		double[] values = position.getValues();
		
		for (int i=0; i < values.length - 1; i++) {
			double z = values[i+1] - Math.pow(values[i], 2);
			double x = Math.pow(z, 2);
			double y = Math.pow((values[i] - 1), 2);
			
			sum += (100*x) + y;
		}
		return sum;
	}

}
