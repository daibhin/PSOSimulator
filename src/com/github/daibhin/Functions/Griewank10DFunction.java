package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public class Griewank10DFunction implements Function {
	
	static int DIMENSION_SIZE = 10;
	
	@Override
	public double evaluate(Position position) {
		
		double[] values = position.getValues();
		if(values.length != DIMENSION_SIZE) {
			//write test for this
			try {
				throw new Exception("Not a valid dimension size");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		double sum = 0;
		double product = 1;
		
		for (int i=0; i < values.length; i++) {
			double xi = values[i];
			sum += Math.pow(xi, 2)/4000;
			product *= Math.cos(xi/Math.sqrt(i+1));
		}
		return sum - product + 1;
	}

	@Override
	public double getLowerBound() {
		return -600;
	}

	@Override
	public double getUpperBound() {
		return 600;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}
}
