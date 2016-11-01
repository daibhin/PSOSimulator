package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F07_ShiftedRotatedGriewank extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Rotated Griewank's Function without Bounds";
	
	// Shifted global optimum
	private double[] o;
	private final double[][] matrix;
	private double[] z;
	private double[] zM;

	public F07_ShiftedRotatedGriewank(int dimensions, double bias) {
		super(dimensions, bias, FUNCTION_NAME);
		
		o = new double[dimensions];
		matrix = new double[dimensions][dimensions];
		z = new double[dimensions];
		zM = new double[dimensions];
		
		this.o = Benchmark.randomProblemSpaceVector(this.getLowerBound(), this.getUpperBound(), dimensions);
//		NOT COMPLETE
//		Benchmark.loadMatrixFromFile(file_m, m_dimension, m_dimension, m_matrix);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		Benchmark.shift(z, x, o);
		Benchmark.rotate(zM, z, matrix);

		result = Benchmark.griewank(zM);

		return result + bias;
	}

	@Override
	public double getUpperBound() {
		return 600;
	}

	@Override
	public double getLowerBound() {
		return 0;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
