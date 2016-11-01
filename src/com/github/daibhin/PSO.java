package com.github.daibhin;

import java.util.Random;

import com.github.daibhin.Functions.Function;

public abstract class PSO {
	
	Particle[] particles;
	int maxIterations = 10000;
	Function function;
	Random generator;
	
	public abstract Position run();
	
}