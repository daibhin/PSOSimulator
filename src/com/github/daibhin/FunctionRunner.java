package com.github.daibhin;

import com.github.daibhin.Functions.Func;

public class FunctionRunner implements Runnable {

	private static final String GRAPH_DIRECTORY = "./FinalGraphs/";

	private static final int NUM_ITERATIONS = 10000;
	private static final int NUM_RUNS = 50;
	
	private Func function;
	private BoundaryCondition boundary;
	private boolean noBounds;
	private int dimensions;
	private int algorithmIndex;
	private static int NUM_ALGORITHMS = 10;

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
		Grapher infinitePathLengths = new Grapher();
		Grapher clusteringCoefficientGraph = new Grapher();
		if(algorithmIndex < 0) {
			for(int algorithm = 0; algorithm <= NUM_ALGORITHMS - 1; algorithm++) {
				runSingleAlgorithm(algorithm, function, boundary, functionDimensions, noBounds, convergenceGraph, pathLengthGraph, infinitePathLengths, clusteringCoefficientGraph);
			}
		} else {
			runSingleAlgorithm(algorithmIndex, function, boundary, functionDimensions, noBounds, convergenceGraph, pathLengthGraph, infinitePathLengths, clusteringCoefficientGraph);
		}
	}

	public void runSingleAlgorithm(int algorithmIndex, Func function, BoundaryCondition boundary, int dimensions, boolean noBoundaries, Grapher convergenceGraph, Grapher pathLengthGraph, Grapher infinitePathLengthGraph, Grapher clusteringCoefficientGraph) {
		StatsTracker stats = new StatsTracker(NUM_RUNS);
		PSO algorithm = null;
		for(int run = 0; run < NUM_RUNS; run++) {
			Run runStats = new Run(NUM_ITERATIONS);
			algorithm = getAlgorithm(algorithmIndex, function, boundary, dimensions, runStats, noBoundaries);
			algorithm.run();
			stats.addRun(runStats);

			convergenceGraph.addSeries(algorithm.getName() + " - Convergence", runStats.getConvergenceValues());
			convergenceGraph.createChart(function.name() + " - Convergence", "Iteration", "Fitness");
			convergenceGraph.saveChart(GRAPH_DIRECTORY + algorithm.getName() + "/" + function.name() + "/Convergence/", run + ".png");
			convergenceGraph.clearSeries();

			pathLengthGraph.addSeries(algorithm.getName() + " - Path Length", runStats.getAvgPathLengthValues());
			pathLengthGraph.createChart(function.name() + " - Average Path Length", "Iteration", "Path Length");
			pathLengthGraph.saveChart(GRAPH_DIRECTORY + algorithm.getName() + "/" + function.name() + "/PathLength/", run + ".png");
			pathLengthGraph.clearSeries();

			infinitePathLengthGraph.addSeries(algorithm.getName() + " - Infinite Path Lengths", runStats.getAvgNumInfinitePaths());
			infinitePathLengthGraph.createChart(function.name() + " - Average Infinite Path Lengths", "Iteration", "Average Number of Infinite Paths");
			infinitePathLengthGraph.saveChart(GRAPH_DIRECTORY + algorithm.getName() + "/" + function.name() + "/InfinitePaths/", run + ".png");
			infinitePathLengthGraph.clearSeries();

//			clusteringCoefficientGraph.addSeries(algorithm.getName() + " - Clustering Coefficient", runStats.getClusteringCoefficientValues());
//			clusteringCoefficientGraph.createChart(function.name() + " - Clustering Coefficient", "Iteration", "Percentage");
//			clusteringCoefficientGraph.saveChart("./FinalGraphs/" + algorithm.getName() + "/" + function.name() + "/ClusteringCoefficient/", run + ".png");
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
			case 8:  return new APLR_GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
			case 9:  return new LinearR_GIDN(function, boundary, dimensions, noBounds, statsTracker, NUM_ITERATIONS);
		}
		return null;
	}
}
