package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F02_ShiftedSchwefel extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Schwefel's Problem 1.2";
	static final public String OPTIMUM_VALUES_FILE = "/Users/David/Documents/College/Final Year Project/Java-ypchen-050309/supportData/schwefel_102_data.txt";
	
	// Shifted global optimum
	private double[] o;
	
	private double[] z;
	
	public F02_ShiftedSchwefel(int dimensions, double bias) {
		super(dimensions, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		this.o = new double[dimensions];
		this.z = new double[dimensions];
		
//		this.o = Benchmarker.randomProblemSpaceVector(this.getLowerBound(), this.getUpperBound(), dimensions);
		// Load the shifted global optimum
		Benchmarker.loadRowVectorFromFile(OPTIMUM_VALUES_FILE, dimensions, o);
	}

	@Override
	public double evaluate(Position position) {
		double result = 0;
		double[] x = position.getValues();
		
		Benchmarker.shift(z, x, o);
		result = Benchmarker.schwefel_102(z);

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
}
