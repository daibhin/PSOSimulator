package com.github.daibhin.Functions;

import com.github.daibhin.Functions.Function;
import com.github.daibhin.Position;

public class Schaffer2DFunction implements Function {

	@Override
	public double evaluate(Position position) {
		double[] values = position.getValues();
		if(values.length != 2) {
			//write test for this
			try {
				throw new Exception("Not a valid dimension size");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		double x1squared = Math.pow(values[0], 2);
		double x2squared = Math.pow(values[1], 2);
		
		double num = Math.pow(Math.sin(x1squared - x2squared), 2) - 0.5;
		double denom = Math.pow(1 + (0.001*(x1squared + x2squared)), 2);
		
		return 0.5 + (num/denom);
	}

	@Override
	public double getLowerBound() {
		return -100;
	}

	@Override
	public double getUpperBound() {
		return 100;
	}

}
