package com.github.daibhin.Functions;

import com.github.daibhin.Benchmarker;
import com.github.daibhin.Position;

public class F21_RotatedHybridComposition_3 extends Function {

	static final public String FUNCTION_NAME = "Rotated Hybrid Composition Function 3";
	static final public String OPTIMUM_VALUES_FILE = applicationDirectory + "/Java-ypchen-050309/supportData/hybrid_func3_data.txt";
	static final public String MATRIX_VALUES_FILE_PREFIX = applicationDirectory + "/Java-ypchen-050309/supportData/hybrid_func3_M_D";
	static final public int NUM_FUNC = 10;
	
	private final double[] sigma = {1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 2.0};
	private final double[] lambda = {5.0*5.0/100.0, 5.0/100.0, 5.0*1.0, 1.0, 5.0*1.0, 1.0, 5.0*10.0, 10.0, 5.0*5.0/200.0, 5.0/200.0};
	private final double[] biases = {0.0, 100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0};

	private final double[][] o;
	private final double[][][] M;
	
	private double[][] z;
	private double[][] zM;
	private double[] w;
	
	private final HybridComposition hc;

	
	public F21_RotatedHybridComposition_3(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);

		o = new double[NUM_FUNC][dimensions];
		M = new double[NUM_FUNC][dimensions][dimensions];
		
		w = new double[NUM_FUNC];
		z = new double[NUM_FUNC][dimensions];
		zM = new double[NUM_FUNC][dimensions];
		
		// Load the shifted global optimum
		Benchmarker.loadMatrixFromFile(OPTIMUM_VALUES_FILE, NUM_FUNC, dimensions, o);
		// Load the matrix
		String matrixFile = MATRIX_VALUES_FILE_PREFIX + dimensions + DEFAULT_FILE_SUFFIX;
		Benchmarker.loadNMatrixFromFile(matrixFile, NUM_FUNC, dimensions, dimensions, M);
		
		hc = new F21(NUM_FUNC, dimensions, lambda, biases, sigma, o, z, M, w, zM);
		hc.calculateFunctionMaximums();
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		result = Benchmarker.hybridComposition(x, hc);

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

	private class F21 extends HybridComposition {
		
		public F21(int numFunc, int dimensions, double[] lambda, double[] biases, double[] sigma, double[][] o, double[][] z, double[][][] M, double[] w, double[][] zM) {
			this.num_func = numFunc;
			this.num_dim = dimensions;
			
			this.lambda = lambda;
			this.biases = biases;
			this.sigma = sigma;
			
			this.o = o;
			this.M = M;
			
			this.w = w;
			this.z = z;
			this.zM = zM;
		}

		@Override
		public double basicFunction(int func_no, double[] x) {
			double result = 0.0;
			switch(func_no) {
				case 0:
				case 1:
					result = Benchmarker.ExpandedScaffer(x);
					break;
				case 2:
				case 3:
					result = Benchmarker.rastrigin(x);
					break;
				case 4:
				case 5:
					result = Benchmarker.F8F2(x);
					break;
				case 6:
				case 7:
					result = Benchmarker.weierstrass(x);
					break;
				case 8:
				case 9:
					result = Benchmarker.griewank(x);
					break;
				default:
					System.err.println("func_no is out of range.");
					System.exit(-1);
			}
			return result;
		}

		@Override
		public void calculateFunctionMaximums() {
			double[] testPoint = new double[dimensions];
			double[] testPointM = new double[dimensions];
			double[] fmax = new double[num_func];
			for (int i = 0 ; i < num_func ; i ++) {
				for (int j = 0 ; j < num_dim; j ++) {
					testPoint[j] = (5.0 / this.lambda[i]);
				}
				Benchmarker.rotate(testPointM, testPoint, this.M[i]);
				fmax[i] = Math.abs(this.basicFunction(i, testPointM));
			}
			this.fmax = fmax;
		}
	}

}
