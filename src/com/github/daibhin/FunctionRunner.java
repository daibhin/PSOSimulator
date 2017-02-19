package com.github.daibhin;

import com.github.daibhin.Functions.Func;

public class FunctionRunner implements Runnable {
	
	private static final int NUM_ITERATIONS = 10000;
	private static final int NUM_RUNS = 25;
	
	private Func function;
	private BoundaryCondition boundary;
	private boolean noBounds;
	private int dimensions;
	private int algorithmIndex;
	private static int NUM_ALGORITHMS = 3;

	public FunctionRunner(Func function, BoundaryCondition boundary, boolean boundaries, int dims, int algorithmIndex) {
		this.function = function;
		this.boundary = boundary;
		this.noBounds = boundaries;
		this.dimensions = dims;
		this.algorithmIndex = algorithmIndex;
	}

	public void run() {
		int functionDimensions = function.hasDefinedDimensions() ? function.getDimensions() : dimensions;
		Grapher convergenceGraph = new Grapher();
		Grapher pathLengthGraph = new Grapher();
		Grapher clusteringCoefficientGraph = new Grapher();
		if(algorithmIndex < 0) {
			for(int algorithm = 0; algorithm <= NUM_ALGORITHMS - 1; algorithm++) {
				runSingleAlgorithm(algorithm, function, boundary, functionDimensions, noBounds, convergenceGraph, pathLengthGraph, clusteringCoefficientGraph);
			}
		} else {
			runSingleAlgorithm(algorithmIndex, function, boundary, functionDimensions, noBounds, convergenceGraph, pathLengthGraph, clusteringCoefficientGraph);
		}
	}

	public void runSingleAlgorithm(int algorithmIndex, Func function, BoundaryCondition boundary, int dimensions, boolean noBoundaries, Grapher convergenceGraph, Grapher pathLengthGraph, Grapher clusteringCoefficientGraph) {
		StatsTracker stats = new StatsTracker(NUM_RUNS);
		PSO algorithm = null;
		for(int run = 0; run < NUM_RUNS; run++) {
			Run runStats = new Run(NUM_ITERATIONS);
			algorithm = getAlgorithm(algorithmIndex, function, boundary, dimensions, runStats, noBoundaries);
			algorithm.run();
			stats.addRun(runStats);

			double[] convergenceValues = runStats.getConvergenceValues();
			double lastVal = convergenceValues[NUM_ITERATIONS-1];
			if (lastVal < 0) {
				convergenceGraph.addSeries(algorithm.getName() + " - Convergence", convergenceValues);
				convergenceGraph.createChart(function.name() + " - Convergence", "Iteration", "Fitness");
			} else {
				convergenceGraph.addDecibleSeries(algorithm.getName() + " - Convergence", convergenceValues, "Iteration", "Fitness (Log Scale)");
			}
			convergenceGraph.saveChart("./NewGraphs/" + algorithm.getName() + "/" + function.name() + "/Convergence/", run + ".png");
			convergenceGraph.clearSeries();

			pathLengthGraph.addSeries(algorithm.getName() + " - Path Length", runStats.getAvgPathLengthValues());
			pathLengthGraph.createChart(function.name() + " - Average Path Length", "Iteration", "Path Length");
			pathLengthGraph.saveChart("./NewGraphs/" + algorithm.getName() + "/" + function.name() + "/PathLength/", run + ".png");
			pathLengthGraph.clearSeries();

//			clusteringCoefficientGraph.addSeries(algorithm.getName() + " - Clustering Coefficient", runStats.getClusteringCoefficientValues());
//			clusteringCoefficientGraph.createChart(function.name() + " - Clustering Coefficient", "Iteration", "Percentage");
//			clusteringCoefficientGraph.saveChart("./NewGraphs/" + algorithm.getName() + "/" + function.name() + "/ClusteringCoefficient/", run + ".png");
//			clusteringCoefficientGraph.clearSeries();
		}
		stats.printResults(function.name() + "_" + algorithm.getName());
		stats.saveAveragedGraphs(algorithm.getName(), function.name());
		System.out.println("\n************************\n");
	}
	
	private PSO getAlgorithm(int index, Func function, BoundaryCondition boundary, int dimensions, Run statsTracker, boolean noBounds) {
		switch(index) {
			case 0:  return new GlobalPSO(function, boundary, dimensions, statsTracker, noBounds, NUM_ITERATIONS);
			case 1:  return new SPSO(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
			case 2:  return new GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
			case 3:  return new APL_GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
			case 4:  return new Linear_GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
			case 5:  return new Sigmoid_GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
			case 6:  return new Structured_GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
			case 7:  return new EandE_GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
		}
		return null;
	}
}
