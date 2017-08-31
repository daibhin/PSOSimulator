package com.github.daibhin;

import java.util.ArrayList;

import com.github.daibhin.Functions.Function;

public abstract class PSO {

    protected int SWARM_SIZE = 50;
    protected int DIMENSIONS = 30;
    protected int MAX_ITERATIONS = 10000;
    protected double CONSTRICTION_FACTOR = 0.72984;
    protected double C_1 = 2.05;
    protected double C_2 = 2.05;

	Particle[] particles;
	Function function;

	private Position globalBest;
	private double globalFitness;

	public abstract Position run();
    public abstract String getName();

	protected double calculateClusteringCoefficient() {
		return GraphUtilities.clusteringCoefficient(SWARM_SIZE, this.particles);
	}
}