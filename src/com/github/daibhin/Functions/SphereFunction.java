package com.github.daibhin.Functions;

import com.github.daibhin.Functions.Function;
import com.github.daibhin.Position;

public class SphereFunction implements Function {

	public double evaluate(Position position) {
		return Function.squaredSum(position.getValues());
	}
	
	public double getUpperBound() {
		return 5.12;
	}
	
	public double getLowerBound() {
		return -5.12;
	}
	
	public boolean isFitter(Position position, Position other) {
		return this.evaluate(position) < this.evaluate(other);
	}
}
