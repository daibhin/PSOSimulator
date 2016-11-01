package com.github.daibhin.Functions;

public abstract class HybridComposition {
	
	// Constant
	public static final double C = 2000.0;

	public int num_func;
	public int num_dim;
	
	// Stretch/Compress each basic function
	public double[] lambda;
	// Basic function biases
	public double[] biases;
	public double[] sigma;
	
	public double[] fmax;
	public double[][] o;
	// Linear transformation matrix for each basic function
	public double[][][] M;

	public double[] w;
	public double[][] z;
	public double[][] zM;

	public abstract double basicFunction(int func_no, double[] x);

	public abstract void calculateFunctionMaximums();
}
