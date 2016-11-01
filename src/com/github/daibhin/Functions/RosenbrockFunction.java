package com.github.daibhin.Functions;

import com.github.daibhin.Functions.Function;
import com.github.daibhin.Position;

public class RosenbrockFunction implements Function {

	@Override
	public double evaluate(Position position) {
		double sum = 0;
		double[] values = position.getValues();
		
		for (int i=0; i < values.length - 1; i++) {
			double xi = values[i];
			double xnext = values[i+1];
			
			double z = xnext - Math.pow(xi, 2);
			double value = 100*Math.pow(z, 2) + Math.pow((xi - 1), 2);
			
			sum += value;
		}
		return sum;
	}

	@Override
	public double getLowerBound() {
		return -2.048;
	}

	@Override
	public double getUpperBound() {
		return 2.048;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
