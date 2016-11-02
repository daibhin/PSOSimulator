package com.github.daibhin.Functions;

import com.github.daibhin.Benchmark;
import com.github.daibhin.Position;

public class F25_RotatedHybridCompositionWithoutBounds_4 extends Func {
	
	static final public String FUNCTION_NAME = "Rotated Hybrid Composition Function 4 without bounds";
	static final public int NUM_FUNC = 10;
	
	private final double[] sigma = {2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0};
	private final double[] lambda = {10.0, 5.0/20.0, 1.0, 5.0/32.0, 1.0, 5.0/100.0, 5.0/50.0, 1.0, 5.0/100.0, 5.0/100.0};
	private final double[] biases = {0.0, 100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0};
	
	private final double[][] o;
	private final double[][][] M;
	
	private double[][] z;
	private double[][] zM;
	private double[] w;

	private final HybridComposition hc;

	public F25_RotatedHybridCompositionWithoutBounds_4(int dimension, double bias) {
		super(dimension, bias, FUNCTION_NAME);
		
		o = new double[NUM_FUNC][dimensions];
		M = new double[NUM_FUNC][dimensions][dimensions];
		
		w = new double[NUM_FUNC];
		z = new double[NUM_FUNC][dimensions];
		zM = new double[NUM_FUNC][dimensions];
		
//		// Load the shifted global optimum
//		Benchmark.loadMatrixFromFile(file_data, NUM_FUNC, dimensions, o);
//		// Load the matrix
//		Benchmark.loadNMatrixFromFile(file_m, NUM_FUNC, dimensions, dimensions, M);
		
		hc = new F25(NUM_FUNC, dimensions, lambda, biases, sigma, o, z, M, w, zM);
		hc.calculateFunctionMaximums();
	}

	@Override
	public double evaluate(Position position) {
		double[] x = position.getValues();
		double result = 0.0;

		result = Benchmark.hybridComposition(x, hc);
		
		return result + bias;
	}

	@Override
	public double getUpperBound() {
		return 5;
	}

	@Override
	public double getLowerBound() {
		return 2;
	}

	@Override
	public boolean isFitter(Position position, Position other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class F25 extends HybridComposition {
		
		public F25(int numFunc, int dimensions, double[] lambda, double[] biases, double[] sigma, double[][] o, double[][] z, double[][][] M, double[] w, double[][] zM) {
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
			// This part is according to Matlab reference code
			switch(func_no) {
				case 0:
					result = Benchmark.weierstrass(x);
					break;
				case 1:
					result = Benchmark.ExpandedScaffer(x);
					break;
				case 2:
					result = Benchmark.F8F2(x);
					break;
				case 3:
					result = Benchmark.ackley(x);
					break;
				case 4:
					result = Benchmark.rastrigin(x);
					break;
				case 5:
					result = Benchmark.griewank(x);
					break;
				case 6:
					result = Benchmark.nonContExpandedScaffer(x);
					break;
				case 7:
					result = Benchmark.nonContRastrigin(x);
					break;
				case 8:
					result = Benchmark.elliptic(x);
					break;
				case 9:
					result = Benchmark.sphereNoise(x);
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
			for (int i=0; i < num_func; i ++) {
				for (int j=0; j < num_dim; j ++) {
					testPoint[j] = (5.0 / this.lambda[i]);
				}
				Benchmark.rotate(testPointM, testPoint, this.M[i]);
				fmax[i] = Math.abs(this.basicFunction(i, testPointM));
			}
			this.fmax = fmax;
		}
		
		
	}

}