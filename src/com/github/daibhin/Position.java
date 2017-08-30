package com.github.daibhin;

		import java.util.Arrays;

public class Position {

	private double[] values;

	public Position(double[] values) {
		this.values = values;
	}

	public double[] getValues() {
		return values;
	}

	public String toString() {
		return Arrays.toString(values);
	}

}
