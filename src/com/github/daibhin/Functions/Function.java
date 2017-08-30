package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public abstract class Function {
	
	static final public String DEFAULT_FILE_SUFFIX = ".txt";
	static final public String applicationDirectory = System.getProperty("user.dir");
	
	protected int dimensions;
	protected double bias;
	protected String funcName;
	
	public abstract double evaluate(Position position);
	public abstract double getUpperBound();
	public abstract double getLowerBound();
	
	public Function(int dimensions, double bias, String name) {
		this.dimensions = dimensions;
		this.bias = bias;
		this.funcName = name;
	}
	
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}
	
	public boolean isFitter(double position, double other) {
		return position < other;
	}
	
	// Property functions common for all child classes
	public int dimension() {
		return this.dimensions;
	}
	
	public double getOptimum() {
		return 0;
	}
	
	public boolean isOptimumOutsideBounds() {
		return false;
	}

	public double bias() {
		return this.bias;
	}

	public String name() {
		return this.funcName;
	}
	
	public int getDimensions() {
		return this.dimensions;
	}
	
	public boolean hasDefinedDimensions() {
		return false;
	}
}
