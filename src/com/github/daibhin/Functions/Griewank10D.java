package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class Griewank10D extends Function {
	
	private static final String FUNCTION_NAME = "Griewank10D Function";
	private static final int DIMENSION_SIZE = 10;
	
	public Griewank10D(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
	}
	
	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		
		if(x.length != DIMENSION_SIZE) {
			//write test for this
			try {
				throw new Exception("Not a valid dimension size");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return Benchmarker.griewank(x);
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
	public boolean hasDefinedDimensions() {
		return true;
	}
}
