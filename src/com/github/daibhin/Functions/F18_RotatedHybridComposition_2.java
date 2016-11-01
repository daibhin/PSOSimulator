package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public class F18_RotatedHybridComposition_2 extends Func {

	static final public String FUNCTION_NAME = "Rotated Hybrid Composition Function 2";
	
	private final double[] sigma = {1.0, 2.0, 1.5, 1.5, 1.0, 1.0, 1.5, 1.5, 2.0, 2.0};
	

	public F18_RotatedHybridComposition_2(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		
	}

	@Override
	public double evaluate(Position position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getUpperBound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLowerBound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		// TODO Auto-generated method stub
		return false;
	}

}
