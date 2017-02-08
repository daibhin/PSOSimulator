package com.github.daibhin;

import java.io.FileWriter;
import java.io.IOException;
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

	public void saveAveragedGraphs(String algorithmName, String functionName) {
		double[] avgConvergenceValues = new double[runs.get(0).getConvergenceValues().length];
		double[] avgPathLengthValues = new double[avgConvergenceValues.length];
		double[] avgCluesteringCoefficientValues = new double[avgConvergenceValues.length];
		for(Run run : runs) {
			for (int i=0; i < run.getConvergenceValues().length; i++) {
				avgConvergenceValues[i] += run.getConvergenceValues()[i];
				avgPathLengthValues[i] += run.getAvgPathLengthValues()[i];
				avgCluesteringCoefficientValues[i] += run.getClusteringCoefficientValues()[i];
			}
		}
		for (int j=0; j < avgConvergenceValues.length; j++) {
			avgConvergenceValues[j] /= runs.size();
			avgPathLengthValues[j] /= runs.size();
			avgCluesteringCoefficientValues[j] /= runs.size();
		}

		saveGraph(algorithmName, functionName, "Convergence Graph", "Iteration", "Best Fitness", "Convergence.png", avgConvergenceValues);
		saveGraph(algorithmName, functionName, "Average Path Length Graph", "Iteration", "Average Path Length", "PathLength.png", avgPathLengthValues);
		saveGraph(algorithmName, functionName, "Clustering Coefficient Graph", "Iteration", "Clustering Coefficient", "ClusteringCoefficient.png", avgCluesteringCoefficientValues);

	}

	private void saveGraph(String algorithmName, String functionName, String graphName, String xLabel, String yLabel, String filename, double[] values) {
		Grapher graph = new Grapher();
		graph.addSeries(algorithmName, values);
		graph.createChart(functionName + " - " + graphName, xLabel, yLabel);
		graph.saveChart("./Graphs/" + algorithmName + "/" + functionName + "/", filename);
	}
}
