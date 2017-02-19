package com.github.daibhin;

import java.util.Random;

import com.github.daibhin.Functions.Func;

public abstract class PSO {
	
	Particle[] particles;
	int maxIterations = 10000;
	Func function;

	private Position globalBest;
	private double globalFitness;

	public abstract Position run();
	public abstract String getName();
	
}