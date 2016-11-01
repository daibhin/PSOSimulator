package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F02_ShiftedSchwefel extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Schwefel's Problem 1.2";
	
	// Shifted global optimum
	private double[] o;
	
	private double[] z;
	
	public F02_ShiftedSchwefel(int dimensions, double bias) {
		super(dimensions, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		this.o = new double[dimensions];
		this.z = new double[dimensions];
		
		this.o = Benchmark.randomProblemSpaceVector(this.getLowerBound(), this.getUpperBound(), dimensions);
	}

	@Override
	public double evaluate(Position position) {
		double result = 0;
		double[] x = position.getValues();
		
		Benchmark.shift(z, x, o);
		result = Benchmark.schwefel_102(z);

		return result + bias;
	}

	public double getOptimum() {
		return bias;
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
		return this.evaluate(position) < this.evaluate(other);
	}
}
