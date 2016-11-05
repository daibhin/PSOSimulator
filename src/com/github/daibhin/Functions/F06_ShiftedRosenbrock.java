package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F06_ShiftedRosenbrock extends Func {

	static final public String FUNCTION_NAME = "Shifted Rosenbrock's Function";
	
	// Shifted global optimum
	private double[] o;
	private double[] z;
	
	public F06_ShiftedRosenbrock(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		this.o = new double[dimensions];
		this.z = new double[dimensions];
		
		this.o = Function.randomProblemSpaceVector(this.getUpperBound(), this.getLowerBound(), dimensions);
		
		// z = x - o + 1 = x - (o - 1)
		// Do the "(o - 1)" part first
		for (int i = 0 ; i < dimensions ; i ++) {
			o[i] -= 1.0;
		}
	}
	
	@Override
	public double evaluate(Position position) {
		
		double[] x = position.getValues();
		double result = 0.0;

		Benchmarker.shift(z, x, o);

		result = Benchmarker.rosenbrock(z);

		return result + bias;
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
	public boolean isFitter(Position position, Position other) {
		return Math.abs(this.bias - this.evaluate(position)) < Math.abs(this.bias - this.evaluate(other));
	}

}
