package com.github.daibhin;

import com.github.daibhin.Functions.Function;

public class Particle {
	
	private Position location;
	private double[] velocity;
	private double currentFitness;
	private double bestPersonalFitness;
	private Position personalBest;
	
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

	public void setBestFitnessToCurrent() {
		this.bestPersonalFitness = this.currentFitness;
	}
	
	public void setPersonalBestPosition(Position position) {
		this.personalBest = position;
	}
	
	public Position getBestPersonalPosition() {
		return this.personalBest;
	}
	
	public boolean currentlyBetterThanPersonalBest() {
		return this.currentlyBetterThan(this.bestPersonalFitness);
	}
	
	public boolean currentlyBetterThan(double fitness) {
		return this.currentFitness < fitness;
	}

	public void updateCurrentFitness(Function function) {
		this.currentFitness = function.evaluate(this.location);
	}
	
	public double getCurrentFitness() {
		return this.currentFitness;
	}
	
	public boolean bestBetterThan(double fitness) {
		return this.bestPersonalFitness < fitness;
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
}
