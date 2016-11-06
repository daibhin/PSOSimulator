package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F03_ShiftedRotatedElliptic extends Func {
	
	static final public String FUNCTION_NAME = "Shifted Rotated High Conditioned Elliptic Function";
	static final public String OPTIMUM_VALUES_FILE = "/Users/David/Documents/College/Final Year Project/Java-ypchen-050309/supportData/high_cond_elliptic_rot_data.txt";
	static final public String MATRIX_VALUES_FILE_PREFIX = "/Users/David/Documents/College/Final Year Project/Java-ypchen-050309/supportData/Elliptic_M_D";
	
	// Shifted global optimum
	private final double[] o;
	private final double[][] matrixM;
	private double[] z;
	private double[] zM;
	private	double constant;
	
	public F03_ShiftedRotatedElliptic (int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		// Note: dimension starts from 0
		o = new double[dimensions];
		matrixM = new double[dimensions][dimensions];
		z = new double[dimensions];
		zM = new double[dimensions];
		
//		this.o = Benchmarker.randomProblemSpaceVector(this.getUpperBound(), this.getLowerBound(), dimensions);
		// Load the shifted global optimum
		Benchmarker.loadRowVectorFromFile(OPTIMUM_VALUES_FILE, dimensions, o);
		// Load the matrix
		String matrixFile = MATRIX_VALUES_FILE_PREFIX + dimensions + DEFAULT_FILE_SUFFIX;
		Benchmarker.loadMatrixFromFile(matrixFile, dimensions, dimensions, matrixM);
		
		constant = Math.pow(1.0e6, 1.0/(dimensions-1.0));
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		
		Benchmarker.shift(z, x, o);
		Benchmarker.rotate(zM, z, matrixM);
		
		double sum = 0.0;
		
		for (int i = 0 ; i < dimensions ; i++) {
			sum += Math.pow(constant, i) * (zM[i] * zM[i]);
		}
		
//		return Benchmarker.elliptic(x) + bias;
		
		return sum + bias;
	}

	@Override
	public double getLowerBound() {
		return -100;
	}

	@Override
	public double getUpperBound() {
		return 100;
	}
	
	public double[] getOptimumPosition() {
		return this.o;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}

}
