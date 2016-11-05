package com.github.daibhin;

import java.util.Random;

import com.github.daibhin.Functions.Func;
import com.github.daibhin.Functions.Function;

public abstract class PSO {
	
	Particle[] particles;
	int maxIterations = 10000;
	Func function;
	Random generator;
	
	public abstract Position run();
	
}