package com.github.daibhin.Functions;

import java.util.Random;

import com.github.daibhin.Position;

public interface Function {

	public abstract double evaluate(Position position);

	public abstract double getLowerBound();

	public abstract double getUpperBound();

	public abstract boolean isFitter(Position position, Position other);

	public default double getOptimum() {
		return 0.0;
	}

	static double[] randomProblemSpaceVector(double max, double min, int dimensions) {
		Random generator = new Random();
		double[] values = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			double rangeRandom = min + (max - min) * generator.nextDouble();
			values[i] = rangeRandom;
		}
		return values;
	}

	static double squaredSum(double[] values) {
		double sum = 0;
		for (int i = 0; i < values.length; i++) {
			double xSquared = Math.pow(values[i], 2);
			sum += xSquared;
		}
		return sum;
	}
	
	static double[] shiftedVector(double[] x, double[] o) {
		double[] z = new double[x.length];
		for(int i=0; i<x.length; i++) {
			z[i] = x[i] - o[i];
		}
		return z;
	}
}
