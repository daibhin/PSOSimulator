package com.github.daibhin;

public abstract class Function {
	public abstract double evaluate(Position position);
	
	public boolean isMinimised(Position position) {
		return this.evaluate(position) == 0;
	}
}
