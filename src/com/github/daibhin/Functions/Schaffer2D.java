package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public class Schaffer2D extends Function {

	private static final String FUNCTION_NAME = "Schaffer2D Function";
	private static final int DIMENSION_SIZE = 2;
	
	public Schaffer2D(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
	}
	
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
		double x1 = values[0];
		double x2 = values[1];
		double x1squared = Math.pow(x1, 2);
		double x2squared = Math.pow(x2, 2);
		double squaredSin = Math.pow(Math.sin(x1squared - x2squared), 2);
		
		double num = squaredSin - 0.5;
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

	@Override
	public boolean hasDefinedDimensions() {
		return true;
	}
}
