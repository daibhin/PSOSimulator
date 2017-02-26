package com.github.daibhin;

public class Run {
	
	private double[] convergenceValues;
	private double[] avgPathLength;
	private double[] avgInfinitePaths;
	private double[] clusteringCoefficientValues;
	private double oneThousandValue;
	private double tenThousandValue;
	
	public Run(int numIter) {
		convergenceValues = new double[numIter];
		avgPathLength = new double[numIter];
		avgInfinitePaths = new double[numIter];
		clusteringCoefficientValues = new double[numIter];
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
	public double[] getAvgPathLengthValues() {
		return avgPathLength;
	}
	public void setAvgPathLength(int i, double apl) {
		avgPathLength[i] = apl;
	}
	public void setAvgNumInfinitePaths(int i, double value) {
		avgInfinitePaths[i] = value;
	}
	public double[] getAvgNumInfinitePaths() {
		return avgInfinitePaths;
	}
	public double[] getClusteringCoefficientValues() {
		return clusteringCoefficientValues;
	}
	public void setClusteringCoefficientValue(int i, double value) {
		clusteringCoefficientValues[i] = value;
	}
}
