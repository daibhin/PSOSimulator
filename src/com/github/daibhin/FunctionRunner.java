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
		Grapher clusteringGraph = new Grapher();
		Grapher clusteringCoefficientGraph = new Grapher();
		if(algorithmIndex < 0) {
			for(int algorithm = 0; algorithm <= NUM_ALGORITHMS - 1; algorithm++) {
				runSingleAlgorithm(algorithm, function, boundary, functionDimensions, noBounds, convergenceGraph, clusteringGraph, clusteringCoefficientGraph);
			}
		} else {
			runSingleAlgorithm(algorithmIndex, function, boundary, functionDimensions, noBounds, convergenceGraph, clusteringGraph, clusteringCoefficientGraph);
		}
		convergenceGraph.plotGraph("Convergence Chart", function.name(), "Iteration", "Fitness");
		clusteringGraph.plotGraph("Clustering Chart", function.name(), "Iteration", "Enclosing Radius");
		clusteringCoefficientGraph.plotGraph("Clustering Coefficient Chart", function.name(), "Iteration", "Percentage");
	}
	
	public void runSingleAlgorithm(int algorithmIndex, Func function, BoundaryCondition boundary, int dimensions, boolean noBoundaries, Grapher convergenceGraph, Grapher clusteringGraph, Grapher clusteringCoefficientGraph) {
		StatsTracker stats = new StatsTracker(NUM_RUNS);
		PSO algorithm = null;
		for(int run=0; run < NUM_RUNS; run++) {
			Run runStats = new Run(NUM_ITERATIONS);
			algorithm = getAlgorithm(algorithmIndex, function, boundary, dimensions, runStats, noBoundaries);
			algorithm.run();
			stats.addRun(runStats);
			if (run == 0) {
				convergenceGraph.addSeries(algorithm.getName(), runStats.getConvergenceValues());
				clusteringGraph.addSeries(algorithm.getName(), runStats.getClusteringValues());
				clusteringCoefficientGraph.addSeries(algorithm.getName(), runStats.getClusteringCoefficientValues());
			}
		}
		stats.printResults(function.name() + "_" + algorithm.getName());
		System.out.println("\n************************\n");
	}
	
	private PSO getAlgorithm(int index, Func function, BoundaryCondition boundary, int dimensions, Run statsTracker, boolean noBounds) {
		switch(index) {
			case 0:  return new GlobalPSO(function, boundary, dimensions, statsTracker, noBounds, NUM_ITERATIONS);
			case 1:  return new SPSO(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
			case 2:  return new GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
		}
		return null;
	}
}
