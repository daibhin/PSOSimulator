package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F09_ShiftedRastrigin extends Func {

	static final public String FUNCTION_NAME = "Shifted Rastrigin's Function";
	static final public String OPTIMUM_VALUES_FILE = applicationDirectory + "/Java-ypchen-050309/supportData/rastrigin_func_data.txt";
	
	private final double[] o;
	private double[] z;
	
	public F09_ShiftedRastrigin(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		o = new double[dimensions];
		z = new double[dimensions];

		// Load the shifted global optimum
//		Benchmarker.randomProblemSpaceVector(getUpperBound(), getLowerBound(), dimensions);
		Benchmarker.loadRowVectorFromFile(OPTIMUM_VALUES_FILE, dimensions, o);
	}
	
	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		Benchmarker.shift(z, x, o);

		result = Benchmarker.rastrigin(z);
		
		return result + bias;
	}

	@Override
	public double getLowerBound() {
		return -5;
	}

	@Override
	public double getUpperBound() {
		return 5;
	}

}
