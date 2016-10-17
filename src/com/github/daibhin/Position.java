package com.github.daibhin;

public class Position {
	
	private double[] values;
	
	public Position(double[] values) {
		this.values = values;
	}

	public boolean compareTo(Position personalBest) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public double[] getValues() {
		return values;
	}
	
	public String toString() {
		String output = "[";
		for(int i=0; i < values.length; i++) {
			output +=  ", " + values[i];
		}
		output += "]";
		return output;
	}

}
