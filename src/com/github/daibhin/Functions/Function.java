package com.github.daibhin.Functions;

import com.github.daibhin.Position;

public interface Function {
	public abstract double evaluate(Position position);
	
	public abstract double getLowerBound();
	public abstract double getUpperBound();
	public abstract boolean isFitter(Position position, Position other);
	
	public default double getOptimum() {
		return 0.0;
	}
}
