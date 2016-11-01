package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F13_ShiftedEpandedGriewankRosenbrock extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Expanded Griewank's plus Rosenbrock's Function";
	
	// Shifted global optimum
	private double[] o;
	private double[] z;

	public F13_ShiftedEpandedGriewankRosenbrock(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		o = new double[dimensions];
		z = new double[dimensions];

		// Load the shifted global optimum
		this.o = Benchmark.randomProblemSpaceVector(getUpperBound(), getLowerBound(), dimensions);
//		Benchmark.loadRowVectorFromFile(file_data, dimensions, m_o);

		// z = x - o + 1 = x - (o - 1)
		for (int i=0; i < dimensions; i ++) {
			o[i] -= 1.0;
		}
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		Benchmark.shift(z, x, o);
		result = Benchmark.F8F2(z);
		
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

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
