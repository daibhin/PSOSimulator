package com.github.daibhin;

import java.util.concurrent.ExecutorService;

import com.github.daibhin.Functions.Func;

public class FunctionRunner implements Runnable {
	
	private static final int NUM_ITERATIONS = 10000;
	private static final int NUM_RUNS = 25;
	
	private Func function;
	private BoundaryCondition boundary;
	private boolean noBounds;
	private int dimensions;

	public FunctionRunner(Func function, BoundaryCondition boundary, boolean boundaries, int dims) {
		this.function = function;
		this.boundary = boundary;
		this.noBounds = boundaries;
		this.dimensions = dims;
	}

	@Override
	public void run() {
		int functionDimensions = function.hasDefinedDimensions() ? function.getDimensions() : dimensions;
		Grapher grapher = new Grapher();
		for(int algorithmIndex = 0; algorithmIndex <= 2; algorithmIndex++) {
			runSingleAlgorithm(algorithmIndex, function, boundary, functionDimensions, noBounds, grapher);
		}
		grapher.plotGraph(function.name(), "Iteration", "Fitness");
	}
	
	public void runSingleAlgorithm(int algorithmIndex, Func function, BoundaryCondition boundary, int dimensions, boolean noBoundaries, Grapher grapher) {	
		StatsTracker stats = new StatsTracker(NUM_RUNS);
		PSO algorithm = null;
		for(int run=0; run < NUM_RUNS; run++) {
			Run runStats = new Run(NUM_ITERATIONS);
			algorithm = getAlgorithm(algorithmIndex, function, boundary, dimensions, runStats, noBoundaries);
			algorithm.run();
			stats.addRun(runStats);
			if (run == 0) {
				grapher.addSeries(algorithm.getName(), runStats.getConvergenceValues());
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
