package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public abstract class Func {
	
	protected int dimensions;
	protected double bias;
	protected String funcName;
	
	public abstract double evaluate(Position position);
	public abstract double getUpperBound();
	public abstract double getLowerBound();
	public abstract boolean isFitter(Position position, Position other);
	
	public Func(int dimensions, double bias, String name) {
		this.dimensions = dimensions;
		this.bias = bias;
		this.funcName = name;
	}
	
	// Property functions common for all child classes
	public int dimension() {
		return this.dimensions;
	}

	public double bias() {
		return this.bias;
	}

	public String name() {
		return this.funcName;
	}
}
