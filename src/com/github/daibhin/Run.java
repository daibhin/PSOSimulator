package com.github.daibhin;

public class Run {
	
//	private double[] clusteringValues;
	private double[] convergenceValues;
	private double oneThousandValue;
	private double tenThousandValue;
	
	public Run(int numIter) {
		convergenceValues = new double[numIter];
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
}
