package com.github.daibhin;

public class Run {
	
	private double[] convergenceValues;
	private double[] clusteringValues;
	private double oneThousandValue;
	private double tenThousandValue;
	
	public Run(int numIter) {
		convergenceValues = new double[numIter];
		clusteringValues = new double[numIter];
	}

	public double getOneThousandValue() {
		return this.oneThousandValue;
	}
	public void setOneThousandValue(double fitness) {
		this.oneThousandValue = fitness;
	}
	public double getTenThousandValue() {
		return this.tenThousandValue;
	}
	public void setTenThousandValue(double fitness) {
		this.tenThousandValue = fitness;
	}
	public double[] getConvergenceValues() {
		return convergenceValues;
	}
	public void setConvergenceValue(int i, double fitness) {
		convergenceValues[i] = fitness;
	}
	public double[] getClusteringValues() {
		return clusteringValues;
	}
	public void setClusteringValue(int i, double radius) {
		clusteringValues[i] = radius;
	}
}
