package com.github.daibhin.Functions;

import com.github.daibhin.Functions.Function;
import com.github.daibhin.Position;

public class RosenbrockFunction implements Function {

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
		if (sum < 35) {
			String me = "hel";
			String you = me;
		}
		return sum;
	}

	@Override
	public double getLowerBound() {
		return -30;
	}

	@Override
	public double getUpperBound() {
		return 30;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		// TODO Auto-generated method stub
		return false;
	}

}
