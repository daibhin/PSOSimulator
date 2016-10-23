package com.github.daibhin.Functions;

import com.github.daibhin.Functions.Function;
import com.github.daibhin.Position;

public class SphereFunction implements Function {
	
	public static double UPPER_BOUND = 5.12;
	public static double LOWER_BOUND = -5.12;

	public double evaluate(Position position) {
		double sum = 0;
		double[] values = position.getValues();
		
		for (int i=0; i < values.length; i++) {
			sum += Math.pow(values[i], 2);
		}
		return sum;
	}
	
	public double getUpperBound() {
		return 5.12;
	}
	
	public double getLowerBound() {
		return -5.12;
	}
}
