package com.github.daibhin;

import java.util.Arrays;

public class Run {
	
//	private double[] clusteringValues;
	private double[] convergenceValues;
	private double oneThousandValue;
	private double tenThousandValue;
	 
	

	private int numRuns;
	private double[] oneThousands;
	private double[] tenThousands;
	private double[] oneHundredThousands;
	
	public Run(int runs) {
		this.numRuns = runs;
		oneThousands = new double[numRuns];
		tenThousands = new double[numRuns];
		oneHundredThousands = new double[numRuns];
	}

	public void setOneThousandValue(double fitness) {
		this.oneThousandValue = fitness;
	}
	public void setTenThousandValue(double fitness) {
		this.tenThousandValue = fitness;
	}
	
	public void addThousand(double fitness) {
		for (int i=0; i < oneThousands.length; i++) {
			if (oneThousands[i] == 0.0) {
				oneThousands[i] = fitness;
				break;
			}
		}
	}

	public void addTenThousand(double evaluate) {
		for (int i=0; i < tenThousands.length; i++) {
			if (tenThousands[i] == 0.0) {
				tenThousands[i] = evaluate;
				break;
			}
		}
	}
	
	public static void printResults(String funcName) {
		System.out.println("\n" + funcName);
		
		System.out.println("\n1000 iterations");
		printQuartileResults(oneThousands);
		
		System.out.println("\n10,000 iterations");
		printQuartileResults(tenThousands);
	}
	
	public static void documentResults(String title, double[] values) {
		System.out.println("\n" + title);
		
		System.out.println("\n1000 iterations");
		printQuartileResults(values);
		
		System.out.println("\n10,000 iterations");
		printQuartileResults(values);
	}
	
	private static void printQuartileResults(double[] values) {
		Arrays.sort(values);
		System.out.println("1st (Best): " + values[0]);
		
		int firstQuarter = (int) Math.ceil(values.length/4.0);
		System.out.println(firstQuarter + "th: " + values[firstQuarter - 1]);
		
		int thirdQuarter = (int) Math.ceil((values.length/4.0)*3);
		System.out.println(thirdQuarter + "th: " + values[thirdQuarter - 1]);
		
		int last = values.length;
		System.out.println(last + "th (Worst): " + values[last - 1]);
		
		double mean = calculateMean(values);
		System.out.println("Mean: " + mean);
		System.out.println("Standard Deviation: " + calculateStandardDeviation(mean, values) + "\n");
	}
	
	private static double calculateMean(double[] values) {
		double sum = 0.0;
		for (int i=0; i < values.length; i++) {
			sum += values[i];
		}
		return sum/values.length;
	}
	
	private static double calculateStandardDeviation(double mean, double[] values) {
		double sum = 0.0;
		for (int i=0; i < values.length; i++) {
			sum += Math.pow((values[i] - mean), 2);
		}
		return Math.sqrt(sum/values.length);
	}
}
