package com.github.daibhin;

import java.util.Random;

public class Particle {
	
	private double CONSTRICTION_FACTOR = 0.72984;
	private double C_1 = 2.05;
	private double C_2 = 2.05;
	
	private Position location;
	private double[] velocity;
	private double bestPersonalFitness;
	private Position personalBest;
	
//	public void updatePosition() {
//		double[] values = new double[location.getValues().length];
//		for(int i=0; i < velocity.length; i++) {
//			values[i] = this.location.getValues()[i] + velocity[i];
//		}
//		this.location = new Position(values);
//	}
	
	public Position getPosition() {
		return this.location;
	}
	
	public void setPosition(Position position) {
		this.location = position;
	}
	
	public double[] getVelocity() {
		return this.velocity;
	}
	
	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	public void setBestPersonalFitness(double fitness) {
		this.bestPersonalFitness = fitness;
	}
	
	public void setPersonalBest(Position position) {
		this.personalBest = position;
	}
	
	public double getBestPersonalFitness() {
		return this.bestPersonalFitness;
	}
	
	public Position getBestPersonalPosition() {
		return this.personalBest;
	}

}
