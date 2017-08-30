package com.github.daibhin;

import com.github.daibhin.Functions.Function;

public class FunctionRunner implements Runnable {

	private static final String GRAPH_DIRECTORY = "./FinalGraphs/";

	private static final int NUM_RUNS = 1;
	public static final int NUM_ALGORITHMS = 7;
	
	private Function function;
	private BoundaryCondition boundary;
	private boolean noBounds;
	private int dimensions;
	private int algorithmIndex;
	private int numIterations;

	public FunctionRunner(Function function, BoundaryCondition boundary, boolean boundaries, int dims, int index, int iterations) {
		this.function = function;
		this.boundary = boundary;
		this.noBounds = boundaries;
		this.dimensions = dims;
		this.algorithmIndex = index;
		this.numIterations = iterations;
	}

	public void run() {
		int functionDimensions = function.hasDefinedDimensions() ? function.getDimensions() : dimensions;
		Grapher convergenceGraph = new Grapher();
		Grapher pathLengthGraph = new Grapher();
		Grapher infinitePathLengths = new Grapher();

		runAlgorithm(functionDimensions, convergenceGraph, pathLengthGraph, infinitePathLengths);
	}

	public void runAlgorithm(int dimensions, Grapher convergenceGraph, Grapher pathLengthGraph, Grapher infinitePathLengthGraph) {
		StatsTracker stats = new StatsTracker(NUM_RUNS);
		Run runStats = new Run(numIterations);
		PSO algorithm = getAlgorithm(algorithmIndex, dimensions, runStats);
		for(int run = 0; run < NUM_RUNS; run++) {
			algorithm.run();
			stats.addRun(runStats);

			convergenceGraph.addSeries(algorithm.getName() + " - Convergence", runStats.getConvergenceValues());
//			convergenceGraph.createChart(function.name() + " - Convergence", "Iteration", "Fitness");
//			convergenceGraph.plotChart("Convergence");
//			convergenceGraph.saveChart(GRAPH_DIRECTORY + algorithm.getName() + "/" + function.name() + "/Convergence/", run + ".png");
			convergenceGraph.clearSeries();

			pathLengthGraph.addSeries(algorithm.getName() + " - Path Length", runStats.getAvgPathLengthValues());
//			pathLengthGraph.createChart(function.name() + " - Average Path Length", "Iteration", "Path Length");
//			pathLengthGraph.plotChart("Average path length");
//			pathLengthGraph.saveChart(GRAPH_DIRECTORY + algorithm.getName() + "/" + function.name() + "/PathLength/", run + ".png");
			pathLengthGraph.clearSeries();

			infinitePathLengthGraph.addSeries(algorithm.getName() + " - Infinite Path Lengths", runStats.getAvgNumInfinitePaths());
//			infinitePathLengthGraph.createChart(function.name() + " - Average No. Disconnected Paths", "Iteration", "Average No. Disconnected Paths");
//			infinitePathLengthGraph.plotChart("Disconnected Paths");
//			infinitePathLengthGraph.saveChart(GRAPH_DIRECTORY + algorithm.getName() + "/" + function.name() + "/InfinitePaths/", run + ".png");
			infinitePathLengthGraph.clearSeries();
		}
		stats.printResults(function.name() + "_" + algorithm.getName());
//		stats.saveAveragedGraphs(algorithm.getName(), function.name());
		System.out.println("\n************************\n");
	}
	
	private PSO getAlgorithm(int index, int dimensions, Run statsTracker) {
		switch(index) {
			case 0:  return new GlobalPSO(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			case 1:  return new SPSO(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			case 2:  return new GIDN(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			case 3:  return new Structured_GIDN(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			case 4:  return new Gradual_GIDN(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			case 5:  return new Linear_GIDN(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			case 6:  return new Sphere_GIDN(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			case 7:  return new Connected_GIDN(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			case 8:  return new DynamicGlobalPSO(function, boundary, dimensions, noBounds, statsTracker, numIterations);
			default: return null;
		}
	}
}
