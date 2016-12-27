package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F13_ShiftedExpandedGriewankRosenbrock extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Expanded Griewank's plus Rosenbrock's Function";
	static final public String OPTIMUM_VALUES = applicationDirectory + "/Java-ypchen-050309/supportData/EF8F2_func_data.txt";

	// Shifted global optimum
	private double[] o;
	private double[] z;

	public F13_ShiftedExpandedGriewankRosenbrock(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		o = new double[dimensions];
		z = new double[dimensions];

		// Load the shifted global optimum
//		this.o = Benchmarker.randomProblemSpaceVector(getUpperBound(), getLowerBound(), dimensions);
		Benchmarker.loadRowVectorFromFile(OPTIMUM_VALUES, dimensions, this.o);

		// z = x - o + 1 = x - (o - 1)
		for (int i=0; i < dimensions; i ++) {
			o[i] -= 1.0;
		}
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		Benchmarker.shift(z, x, o);
		result = Benchmarker.F8F2(z);
		
		return result + bias;
	}

	@Override
	public double getUpperBound() {
		return 5;
	}

	@Override
	public double getLowerBound() {
		return -5;
	}
	
	public double[] getOptimumPosition() {
		return this.o;
	}

}
