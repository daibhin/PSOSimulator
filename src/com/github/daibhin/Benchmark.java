package com.github.daibhin;

import java.util.Random;

import com.github.daibhin.Functions.Func;
import com.github.daibhin.Functions.Function;
import com.github.daibhin.Functions.HybridComposition;

public class Benchmark {

	static final public ClassLoader loader = ClassLoader.getSystemClassLoader();
	
	static final public String[] FUNCTION_CLASS_NAMES = {
			"F01_Sphere",
			"F02_shifted_schwefel",
			"F03_shifted_rotated_high_cond_elliptic",
			"F04_shifted_schwefel_noise",
			"F05_schwefel_global_opt_bound",
			"F06_shifted_rosenbrock",
			"F07_shifted_rotated_griewank",
			"F08_shifted_rotated_ackley_global_opt_bound",
			"F09_shifted_rastrigin",
			"F10_shifted_rotated_rastrigin",
			"F11_shifted_rotated_weierstrass",
			"F12_schwefel",
			"F13_shifted_expanded_griewank_rosenbrock",
			"F14_shifted_rotated_expanded_scaffer",
			"F15_hybrid_composition_1",
			"F16_rotated_hybrid_composition_1",
			"F17_rotated_hybrid_composition_1_noise",
			"F18_rotated_hybrid_composition_2",
			"F19_rotated_hybrid_composition_2_narrow_basin_global_opt",
			"F20_rotated_hybrid_composition_2_global_opt_bound",
			"F21_rotated_hybrid_composition_3",
			"F22_rotated_hybrid_composition_3_high_cond_num_matrix",
			"F23_noncontinuous_rotated_hybrid_composition_3",
			"F24_rotated_hybrid_composition_4",
			"F25_rotated_hybrid_composition_4_bound"
		};
	
	static final public double[] biases = {
			0,
			0,
			0,
			0,
			0,
			-450,
			-450,
			-450,
			-450,
			-310,
			390,
			-180,
			-140,
			-330,
			-330,
			90,
			-460,
			-130,
			-300,
			120,
			120,
			120,
			10,
			10,
			10,
			360,
			360,
			360,
			260,
			260
		};
	
	static final public int MAX_SUPPORT_DIM = 100;
	static final public int NUM_TEST_FUNC = 30;
	
	static final public Random generator = new Random();
	
	// Class variables
	static private double[] iSqrt;
	
	public static void main(String[] args) {
		new Benchmark();
	}

	public Benchmark() {
		this.iSqrt = new double[MAX_SUPPORT_DIM];
		
		for (int i = 0 ; i < MAX_SUPPORT_DIM ; i ++) {
			double z = Math.sqrt( ((double) i) + 1.0 );
			this.iSqrt[i] = z;
		}
	}
	
	static final Class[] test_func_arg_types = { int.class, double.class };
	public Func testFunctionFactory(int func_num, int dimension) {
		Func function = null;
		try {
			function = (Func)
				loader.loadClass(FUNCTION_CLASS_NAMES[func_num-1])
					.getConstructor(test_func_arg_types)
					.newInstance(
						new Object[] {
							new Integer(dimension),
							new Double(biases[func_num-1])
						}
					);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return function;
	}
	
	// Random Problem Space Vector
	public static double[] randomProblemSpaceVector(double max, double min, int dimensions) {
		Random generator = new Random();
		double[] values = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			double rangeRandom = min + (max - min) * generator.nextDouble();
			values[i] = rangeRandom;
		}
		return values;
	}
	
	// *** SHIFT & ROTATE *** //
	
	// Shift
	static public void shift(double[] results, double[] x, double[] o) {
		for (int i = 0 ; i < x.length ; i ++) {
			results[i] = x[i] - o[i];
		}
	}
	
	// Rotate
	static public void rotate(double[] results, double[] x, double[][] matrix) {
		xA(results, x, matrix);
	}
	
	// *** MATRIX OPERATORS *** //
	
	// (1xD) row vector * (DxD) matrix = (1xD) row vector
	static public void xA(double[] result, double[] x, double[][] A) {
		for (int i = 0 ; i < result.length ; i ++) {
			result[i] = 0.0;
			for (int j = 0 ; j < result.length ; j ++) {
				result[i] += (x[j] * A[j][i]);
			}
		}
	}
	
	// (DxD) matrix * (Dx1) column vector = (Dx1) column vector
	static public void Ax(double[] result, double[][] A, double[] x) {
		for (int i = 0 ; i < result.length ; i ++) {
			result[i] = 0.0;
			for (int j = 0 ; j < result.length ; j ++) {
				result[i] += (A[i][j] * x[j]);
			}
		}
	}
	
	// *** BENCHMARK FUNCTIONS *** //
	
	// Sphere function
	static public double sphere(double[] x) {
		double sum = 0;
		for (int i = 0 ; i < x.length ; i ++) {
			sum += x[i] * x[i];
		}
		return sum;
	}
	
	// Schwefel's problem 1.2
	static public double schwefel_102(double[] x) {

		double prev_sum, curr_sum, outer_sum;

		curr_sum = x[0];
		outer_sum = (curr_sum * curr_sum);

		for (int i = 1 ; i < x.length ; i ++) {
			prev_sum = curr_sum;
			curr_sum = prev_sum + x[i];
			outer_sum += (curr_sum * curr_sum);
		}

		return outer_sum;
	}
	
	// Rosenbrock's function
	static public double rosenbrock(double[] x) {
		double sum = 0.0;

		for (int i = 0 ; i < (x.length-1) ; i ++) {
			double temp1 = (x[i] * x[i]) - x[i+1];
			double temp2 = x[i] - 1.0;
			sum += (100.0 * temp1 * temp1) + (temp2 * temp2);
		}
	
		return sum;
	}
	
	// Griewank's function
	static public double griewank(double[] x) {

		double sum = 0.0;
		double product = 1.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += ((x[i] * x[i]) / 4000.0);
			product *= Math.cos(x[i] / Math.sqrt(i+1));
		}

		return (sum - product + 1.0);
	}
	
	// Ackley's function
	static public double ackley(double[] x) {
		double sum1 = 0.0;
		double sum2 = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum1 += x[i]*x[i];
			sum2 += Math.cos(2*Math.PI*x[i]);
		}

		return (-20.0 * Math.exp(-0.2 * Math.sqrt(sum1 /(double) x.length))) - Math.exp(sum2 /(double) x.length) + 20.0 + Math.E;
	}
	
	// Rastrigin's function
	static public double rastrigin(double[] x) {
		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += (x[i] * x[i]) - (10.0 * Math.cos(2 * Math.PI * x[i])) + 10.0;
		}

		return sum;
	}
	
	// Weierstrass function
	static public double weierstrass(double[] x) {
		return (weierstrass(x, 0.5, 3.0, 20));
	}

	static public double weierstrass(double[] x, double a, double b, int Kmax) {
		double sum1 = 0.0;
		for (int i=0; i < x.length; i ++) {
			for (int k=0; k <= Kmax; k ++) {
				sum1 += Math.pow(a, k) * Math.cos(2 * Math.PI * Math.pow(b, k) * (x[i] + 0.5));
			}
		}

		double sum2 = 0.0;
		for (int k=0; k <= Kmax; k ++) {
			sum2 += Math.pow(a, k) * Math.cos(2 * Math.PI * Math.pow(b, k) * 0.5);
		}

		return sum1 - (sum2*((double) x.length));
	}
	
	// F8F2
	static public double F8F2(double[] x) {
		double sum = 0.0;

		for (int i=1; i < x.length; i ++) {
			sum += F8(F2(x[i-1], x[i]));
		}
		sum += F8(F2(x[x.length-1], x[0]));
	
		return sum;
	}
	
	// F2: Rosenbrock's Function (2D)
	static public double F2(double x, double y) {
		double temp1 = (x * x) - y;
		double temp2 = x - 1.0;
		return ((100.0 * temp1 * temp1) + (temp2 * temp2));
	}
	
	// F8: Griewank's Function (1D)
	static public double F8(double x) {
		return ((x * x) / 4000.0) - Math.cos(x) + 1.0;
	}
	
	// Scaffer's F6 function
	static public double Scaffer(double x, double y) {
		double squaredSum = x*x + y*y;
		double sin = Math.sin(Math.sqrt(squaredSum));
		double denom = 1.0 + 0.001 * squaredSum;
		return 0.5 + ((sin * sin - 0.5)/(denom * denom));
	}

	// Expanded Scaffer's F6 function
	static public double ExpandedScaffer(double[] x) {
		double sum = 0.0;

		for (int i = 1 ; i < x.length ; i ++) {
			sum += Scaffer(x[i-1], x[i]);
		}
		sum += Scaffer(x[x.length-1], x[0]);

		return sum;
	}
	
	// Hybrid composition
	static public double hybridComposition(double[] x, HybridComposition hc) {
		
		int num_func = hc.num_func;
		int num_dim = hc.num_dim;

		// Get the raw weights
		double maxW = Double.NEGATIVE_INFINITY;
		for (int i=0; i < num_func; i ++) {
			double sumSquared = 0.0;
			shift(hc.z[i], x, hc.o[i]);
			for (int j = 0 ; j < num_dim ; j ++) {
				sumSquared += (hc.z[i][j] * hc.z[i][j]);
			}
			double denom = 2.0 * num_dim * hc.sigma[i] * hc.sigma[i];
			hc.w[i] = Math.exp(-1.0 * (sumSquared/denom) );
			if (maxW < hc.w[i])
				maxW = hc.w[i];
		}

		// Modify the weights
		double wSum = 0.0;
		double w1mMaxPow = 1.0 - Math.pow(maxW, 10.0);
		for (int i=0; i < num_func; i ++) {
			if (hc.w[i] != maxW) {
				hc.w[i] *= w1mMaxPow;
			}
			wSum += hc.w[i];
		}

		// Normalize the weights
		for (int i = 0 ; i < num_func ; i ++) {
			hc.w[i] /= wSum;
		}

		double sumF = 0.0;
		for (int i=0; i < num_func; i ++) {
			for (int j = 0 ; j < num_dim ; j ++) {
				hc.z[i][j] /= hc.lambda[i];
			}
			rotate(hc.zM[i], hc.z[i], hc.M[i]);
			double fit = hc.C * hc.basicFunction(i, hc.zM[i]) / hc.fmax[i];
			sumF += hc.w[i] *(fit + hc.biases[i]);
		}
		return sumF;
	}
}
