package com.github.daibhin;

import java.util.ArrayList;
import java.util.Arrays;

import com.github.daibhin.Functions.Function;

public class Particle {
	
	private Position location;
	private double[] velocity;
	private Position personalBest;
	private Neighbourhood neighbourhood;
	private double bestFitness;
	
	public Position getLocation() {
		return this.location;
	}
	
	public void setLocation(Position position) {
		this.location = position;
	}
	
	public double[] getVelocity() {
		return this.velocity;
	}
	
	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}
	
	public void setPersonalBest(double fitness) {
		this.personalBest = this.getLocation();
		this.bestFitness = fitness;
	}
	
	public Position getPersonalBest() {
		return this.personalBest;
	}
	
	public double getBestFitness() {
		return this.bestFitness;
	}
	
	public Neighbourhood getNeighbourhood() {
		return this.neighbourhood;
	}
	
	public void setNeighbourhood(ArrayList<Particle> particles, Function function) {
		this.neighbourhood = new Neighbourhood(particles, function);
	}

	public boolean withinBounds(Function function) {
		double[] locationValues = this.location.getValues();
		for (int i=0; i < locationValues.length; i++) {
			if (locationValues[i] < function.getLowerBound() || locationValues[i] > function.getUpperBound()) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		return Arrays.toString(this.velocity);
	}
}
