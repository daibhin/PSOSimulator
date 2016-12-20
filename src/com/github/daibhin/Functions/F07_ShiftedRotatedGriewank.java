package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F07_ShiftedRotatedGriewank extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Rotated Griewank's Function without Bounds";
	static final public String OPTIMUM_VALUES_FILE = "/Users/David/Documents/College/Final Year Project/Java-ypchen-050309/supportData/griewank_func_data.txt";
	static final public String MATRIX_VALUES_FILE_PREFIX = "/Users/David/Documents/College/Final Year Project/Java-ypchen-050309/supportData/griewank_M_D";
	
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
		
//		this.o = Benchmarker.randomProblemSpaceVector(this.getLowerBound(), this.getUpperBound(), dimensions);
		// Load the shifted global optimum
		Benchmarker.loadRowVectorFromFile(OPTIMUM_VALUES_FILE, dimensions, o);
		// Load the matrix
		String matrixFile = MATRIX_VALUES_FILE_PREFIX + dimensions + DEFAULT_FILE_SUFFIX;
		Benchmarker.loadMatrixFromFile(matrixFile, dimensions, dimensions, matrix);
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		Benchmarker.shift(z, x, o);
		Benchmarker.rotate(zM, z, matrix);

		result = Benchmarker.griewank(zM);

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
	public boolean isOptimumOutsideBounds() {
		return true;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
