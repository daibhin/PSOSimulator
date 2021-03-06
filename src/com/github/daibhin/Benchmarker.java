package com.github.daibhin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.daibhin.Functions.*;

public class Benchmarker {
	
	static final public double[] BIASES = {
			0, 0, 0, 0, 0, 0, 0, -450, -450, -450,
			-450, -310, 390, -180, -140, -330, -330, 90, -460, -130,
			-300, 120, 120, 120, 10, 10, 10, 360, 360, 360,
			260, 260
	};
	static final public boolean[] NO_BOUNDARIES = {
			false, false, false, false, false, false, false, false, false, false,
			false, false, false, true, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,false,
			false, true
	};

    private static final int NUM_ITERATIONS = 10000;
	
	// precomputed values
	static final public double PIx2 = Math.PI * 2.0;
	
	static final public int NUM_TEST_FUNC = 32;
	private static final int BOUNDARY_INDEX = 0;
	private static final int DIMENSIONS = 30;
	private static final int NUM_THREADS = 5;
	
	static final public Random generator = new Random();
	
	public static void main(String[] args) {
		new Benchmarker();
	}

	public Benchmarker() {
		runSingleAlgorithm(1);
//		runEntireExperiment();
	}


	private void runEntireExperiment() {
        for (int index = 0; index < FunctionRunner.NUM_ALGORITHMS; index++) {
            runSingleAlgorithm(index);
        }
	}

    private void runSingleAlgorithm(int algorithmIndex) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        for(int functionIndex = 0; functionIndex < NUM_TEST_FUNC; functionIndex++) {
            double bias = Benchmarker.BIASES[functionIndex];
            boolean noBoundaries = Benchmarker.NO_BOUNDARIES[functionIndex];
            BoundaryCondition boundary = getBoundaryCondition(BOUNDARY_INDEX);

            Function function = getFunction(functionIndex, DIMENSIONS, bias);
            int functionDimensions = function.hasDefinedDimensions() ? function.getDimensions() : DIMENSIONS;
            FunctionRunner fr = new FunctionRunner(function, boundary, noBoundaries, functionDimensions, algorithmIndex, NUM_ITERATIONS);
            executor.execute(fr);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	// *** PARAMETERS *** //
	
	private BoundaryCondition getBoundaryCondition(int index) {
		switch(index) {
			case 0:  return new InvisibleBoundary();
			case 1:  return new ReflectingBoundary();
            default: return null;
		}
	}
	
	private static final int TWO_DIMENSIONS = 2;
	private static final int TEN_DIMENSIONS = 10;
	
	private Function getFunction(int index, int dimensions, double bias) {
		switch(index) {
			case 0:  return new Sphere(dimensions, bias);
			case 1:  return new Rosenbrock(dimensions, bias);
			case 2:  return new Ackley(dimensions, bias);
			case 3:  return new Griewank(dimensions, bias);
			case 4:  return new Rastrigin(dimensions, bias);
			case 5:  return new Schaffer2D(TWO_DIMENSIONS, bias);
			case 6:  return new Griewank10D(TEN_DIMENSIONS, bias);
			case 7:  return new F01_ShiftedSphere(dimensions, bias);
			case 8:  return new F02_ShiftedSchwefel(dimensions, bias);
			case 9:  return new F03_ShiftedRotatedElliptic(dimensions, bias);
			case 10:  return new F04_ShiftedSchwefelNoise(dimensions, bias);
			case 11:  return new F05_ShiftedSchwefelGlobalOptBound(dimensions, bias);
			case 12:  return new F06_ShiftedRosenbrock(dimensions, bias);
			case 13:  return new F07_ShiftedRotatedGriewank(dimensions, bias);
			case 14:  return new F08_ShiftedRotatedAckleyGlobalOptBound(dimensions, bias);
			case 15:  return new F09_ShiftedRastrigin(dimensions, bias);
			case 16:  return new F10_ShiftedRotatedRastrigin(dimensions, bias);
			case 17:  return new F11_ShiftedRotatedWeierstrass(dimensions, bias);
			case 18:  return new F12_Schwefel(dimensions, bias);
			case 19:  return new F13_ShiftedExpandedGriewankRosenbrock(dimensions, bias);
			case 20:  return new F14_ShiftedRotatedExpandedScaffer(dimensions, bias);
			case 21:  return new F15_HybridComposition_1(dimensions, bias);
			case 22:  return new F16_RotatedHybridComposition_1(dimensions, bias);
			case 23:  return new F17_RotatedHybridCompositionNoise_1(dimensions, bias);
			case 24:  return new F18_RotatedHybridComposition_2(dimensions, bias);
			case 25:  return new F19_RotatedHybridCompositionNarrowBasinGlobalOpt_2(dimensions, bias);
			case 26:  return new F20_RotatedHybridCompositionGlobalOptBound_2(dimensions, bias);
			case 27:  return new F21_RotatedHybridComposition_3(dimensions, bias);
			case 28:  return new F22_RotatedHybridCompositionHighConditionNumMatrix_3(dimensions, bias);
			case 29:  return new F23_NoncontinuousRotatedHybridComposition_3(dimensions, bias);
			case 30:  return new F24_RotatedHybridComposition_4(dimensions, bias);
			case 31:  return new F25_RotatedHybridCompositionWithoutBounds_4(dimensions, bias);
            default:  return null;
		}
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
	
	// *** ROUND FUNCTIONS *** //

	// 1. "o" is provided
	static public double zRound(double x, double o) {
		double absoluteZ = Math.abs(x - o);
		return (absoluteZ < 0.5) ? x : ( round(2.0 * x) / 2.0);
	}
	// 2. "o" is not provided
	static public double round(double x) {
		int absoluteInteger = (int) Math.abs(x);
		double decimal = Math.abs(x) - absoluteInteger;
		return (decimal < 0.5) ? (int) x : roundUp(x);
//		return ((Math.abs(x) < 0.5) ? x : (myRound(2.0 * x) / 2.0));
	}
	// 3. Matlab rounding (Round positive numbers to next highest integer, round neg numbers to next lowest integer)
	static private double roundUp(double x) {
		double integer = (int) x;
		return (x <= 0) ? integer - 1 : integer + 1;
//		return (Math.signum(x) * Math.round(Math.abs(x)));
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
	
	// Sphere function with noise
	static public double sphereNoise(double[] x) {
		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += x[i] * x[i];
		}

		// Noise
		sum *= (1.0 + 0.1 * Math.abs(Benchmarker.generator.nextGaussian()));

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

		for (int i=0; i < x.length-1; i ++) {
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

		return sum - product + 1.0;
	}
	
	// Ackley's function
	static public double ackley(double[] x) {
		double A = 20.0;
		double B = 0.2;
		
		double sum1 = 0.0;
		double sum2 = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum1 += x[i]*x[i];
			sum2 += Math.cos(PIx2 * x[i]);
		}

		return (-A * Math.exp(-B * Math.sqrt(sum1 /(double) x.length))) - Math.exp(sum2 /(double) x.length) + A + Math.E;
	}
	
	// Rastrigin's function
	static public double rastrigin(double[] x) {
		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += (x[i] * x[i]) - (10.0 * Math.cos(2 * Math.PI * x[i])) + 10.0;
		}

		return sum;
	}
	
	// Non-Continuous Rastrigin's function
	static public double nonContRastrigin(double[] x) {
		double sum = 0.0;
		double yi;

		for (int i = 0 ; i < x.length ; i ++) {
			yi = round(x[i]);
			sum += (yi * yi) - (10.0 * Math.cos(PIx2 * yi)) + 10.0;
		}

		return sum;
	}
	
	// Weierstrass function
	static final public double a = 0.5;
	static final public double b = 3.0;
	static final public int k = 20;
	static public double weierstrass(double[] x) {
		return (weierstrass(x, a, b, k));
	}
	
	static public double weierstrass(double[] x, double a, double b, int Kmax) {
		double sum1 = 0.0;
		for (int i=0; i < x.length; i ++) {
			for (int k=0; k <= Kmax; k ++) {
				sum1 += Math.pow(a, k) * Math.cos(PIx2 * Math.pow(b, k) * (x[i] + 0.5));
			}
		}

		double sum2 = 0.0;
		for (int k=0; k <= Kmax; k ++) {
			sum2 += Math.pow(a, k) * Math.cos(PIx2 * Math.pow(b, k) * (0.5));
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
	
	// Non-Continuous Expanded Scaffer's F6 function
	static public double nonContExpandedScaffer(double[] x) {
		double sum = 0.0;
		double prevX, currX;

		currX = round(x[0]);
		for (int i = 1 ; i < x.length ; i ++) {
			prevX = currX;
			currX = round(x[i]);
			sum += Scaffer(prevX, currX);
		}
		prevX = currX;
		currX = round(x[0]);
		sum += Scaffer(prevX, currX);

		return sum;
	}
	
	// Elliptic
	static public double elliptic(double[] x) {
		double sum = 0.0;

		for (int i = 0 ; i < x.length ; i ++) {
			sum += Math.pow(1e6, (double) i/((double) (x.length - 1)) ) * (x[i] * x[i]);
		}

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
			hc.w[i] = Math.exp(-1.0 * sumSquared / denom);
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
			double fit = hc.C * hc.basicFunction(i, hc.zM[i]) / hc.fmax[i] + hc.biases[i];
			sumF += hc.w[i] * fit;
		}
		return sumF;
	}
	
	// *** LOAD DATA FUNCTIONS *** //
	
	static public void loadRowVectorFromFile(String file, int columns, double[] row) {
		try {
			BufferedReader brSrc = new BufferedReader(new FileReader(file));
			loadRowVector(brSrc, columns, row);
			brSrc.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	static public void loadRowVector(BufferedReader brSrc, int columns, double[] row) throws Exception {
		String stToken;
		StringTokenizer stTokenizer = new StringTokenizer(brSrc.readLine());
		for (int i = 0 ; i < columns ; i ++) {
			stToken = stTokenizer.nextToken();
			row[i] = Double.parseDouble(stToken);
		}
	}
	
	static public void loadNMatrixFromFile(String file, int N, int rows, int columns, double[][][] matrix) {
		try {
			BufferedReader brSrc = new BufferedReader(new FileReader(file));
			for (int i = 0 ; i < N ; i ++) {
				loadMatrix(brSrc, rows, columns, matrix[i]);
			}
			brSrc.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	static public void loadMatrixFromFile(String file, int rows, int columns, double[][] matrix) {
		try {
			BufferedReader brSrc = new BufferedReader(new FileReader(file));
			loadMatrix(brSrc, rows, columns, matrix);
			brSrc.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	static public void loadMatrix(BufferedReader brSrc, int rows, int columns, double[][] matrix) throws Exception {
		for (int i = 0 ; i < rows ; i ++) {
			loadRowVector(brSrc, columns, matrix[i]);
		}
	}

}
