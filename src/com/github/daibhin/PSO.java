package com.github.daibhin;

public abstract class PSO {
	
	Particle[] particles;
	int maxIterations = 10000;
	
	public abstract Position run();
	
}