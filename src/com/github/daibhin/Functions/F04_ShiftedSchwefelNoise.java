package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F04_ShiftedSchwefelNoise extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Schwefel's Problem 1.2 with Noise in Fitness";
	
	// Shifted global optimum
	private double[] o;
	
	private double[] z;
	
	public F04_ShiftedSchwefelNoise(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		this.o = new double[dimensions];
		this.z = new double[dimensions];
		
		this.o = Benchmark.randomProblemSpaceVector(this.getUpperBound(), this.getLowerBound(), dimensions);
	}

	@Override
	public double evaluate(Position position) {
		double result = 0;
		double[] x = position.getValues();
		
		Benchmark.shift(z, x, o);
		result = Benchmark.schwefel_102(z);
		
		result *= (1.0 + 0.4 * Math.abs(Benchmark.generator.nextGaussian()));
		
		return result + bias;
	}

	@Override
	public double getLowerBound() {
		return -450;
	}

	@Override
	public double getUpperBound() {
		return 450;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
