package com.github.daibhin;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class StatsTracker {
	
	private static final String outputLocation = "/Users/David/Documents/College/Final Year Project/Results/";
	
	ArrayList<Run> runs;
	
	public StatsTracker(int numRuns) {
		runs = new ArrayList<Run>(numRuns);
	}
	
	public void printResults(String title) {
		System.out.println("\n" + title + "\n");
		FileWriter writer;
		try {
			writer = new FileWriter(outputLocation + title + ".txt");
			for(Run run : runs) {
				double finalValue = run.getConvergenceValues()[run.getConvergenceValues().length - 1];
				System.out.println(finalValue);
				writer.write(finalValue + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n1000 iterations");
		double[] oneThousands = getOneThousands();
		printQuartileResults(oneThousands);
		System.out.println("\n10,000 iterations");
		double[] tenThousands = getTenThousands();
		printQuartileResults(tenThousands);
	}
	
	private double[] getOneThousands() {
		double[] oneThousandValues = new double[runs.size()];
		for(int i=0; i < this.runs.size(); i++) {
			oneThousandValues[i] = this.runs.get(i).getOneThousandValue();
		}
		return oneThousandValues;
	}
	private double[] getTenThousands() {
		double[] tenThousandValues = new double[runs.size()];
		for(int i=0; i < this.runs.size(); i++) {
			tenThousandValues[i] = this.runs.get(i).getTenThousandValue();
		}
		return tenThousandValues;
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

	public void addRun(Run run) {
		this.runs.add(run);
	}

}
