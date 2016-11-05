package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F04_ShiftedSchwefelNoise extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Schwefel's Problem 1.2 with Noise in Fitness";
	static final public String OPTIMUM_VALUES_FILE = "/Users/David/Documents/College/Final Year Project/Java-ypchen-050309/supportData/schwefel_102_data.txt";
	
	// Shifted global optimum
	private double[] o;
	
	private double[] z;
	
	public F04_ShiftedSchwefelNoise(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		this.o = new double[dimensions];
		this.z = new double[dimensions];
		
//		this.o = Benchmarker.randomProblemSpaceVector(this.getUpperBound(), this.getLowerBound(), dimensions);
		// Load the shifted global optimum
		Benchmarker.loadRowVectorFromFile(OPTIMUM_VALUES_FILE, dimensions, o);
	}

	@Override
	public double evaluate(Position position) {
		double result = 0;
		double[] x = position.getValues();
		
		Benchmarker.shift(z, x, o);
		result = Benchmarker.schwefel_102(z);
		
		result *= (1.0 + 0.4 * Math.abs(Benchmarker.generator.nextGaussian()));
		
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
		return this.evaluate(position) < this.evaluate(other);
	}

}
