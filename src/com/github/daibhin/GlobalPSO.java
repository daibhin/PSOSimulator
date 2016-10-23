package com.github.daibhin;

import com.github.daibhin.Functions.Function;

import java.util.Random;

public class GlobalPSO extends PSO {
	
	private int PARTICLE_COUNT = 50;
	private int DIMENSIONS = 30;
	private int MAX_ITERATIONS = 10000;
	private double CONSTRICTION_FACTOR = 0.72984;
	private double C_1 = 2.05;
	private double C_2 = 2.05;
	
	private Position globalBest;
	private double globalFitness;
	private Function function;
	private Particle[] particles;

	public GlobalPSO(Function function) {
		this.function = function;
		initializeSwarm();
	}

	public Position run() {

		for(int i=0; i < PARTICLE_COUNT; i++) {
			Particle particle = particles[i];
			particle.updateCurrentFitness(function);
			particle.setBestFitnessToCurrent();
			particle.setPersonalBestPosition(particle.getLocation());
			
			if(i == 0 || particle.bestBetterThan(this.globalFitness)) {
				this.globalFitness = particle.getCurrentFitness();
				this.globalBest = particle.getLocation();
			}
		}

		int iteration = 0;
		while (iteration < MAX_ITERATIONS) {

			for (int i=0; i < PARTICLE_COUNT; i++) {
				Particle particle = particles[i];
				
				// algorithm has converged
				if (particle.getCurrentFitness() == function.getOptimum()) {
					System.out.println("Solution found at iteration " + (iteration));
					System.out.println("Position: " + this.globalBest);
					return particle.getLocation();
				}

				if (particle.withinBounds(function)) {
					// update global best
					if(iteration == 0 || particle.currentlyBetterThan(this.globalFitness)) {
						this.globalFitness = particle.getCurrentFitness();
						this.globalBest = particle.getLocation();
					}
	
					// update personal best
					if(particle.currentlyBetterThanPersonalBest()) {
						particle.setBestFitnessToCurrent();
						particle.setPersonalBestPosition(particle.getLocation());
					}
				}
				
				// generate & update particle velocity
				Random generator = new Random();
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();

				double[] updatedVelocity = new double[DIMENSIONS];
				for(int vel=0; vel < DIMENSIONS; vel++) {
					double personalContribution = (C_1 * r1) * (particle.getBestPersonalPosition().getValues()[vel] - particle.getLocation().getValues()[vel]);
					double globalContribution = (C_2 * r2) * (this.globalBest.getValues()[vel] - particle.getLocation().getValues()[vel]);
					double newVel = CONSTRICTION_FACTOR*(particle.getVelocity()[vel] + personalContribution + globalContribution);
					updatedVelocity[vel] = newVel;
				}
				particle.setVelocity(updatedVelocity);
				
				// update particle position & fitness
				double[] newLocation = new double[DIMENSIONS];
				for(int dim=0; dim < DIMENSIONS; dim++) {
					newLocation[dim] = particle.getLocation().getValues()[dim] + updatedVelocity[dim];
				}
				Position updatedPosition = new Position(newLocation);
				particle.setLocation(updatedPosition);	
				particle.updateCurrentFitness(function);
			}

			iteration++;
			System.out.println("Iteration: " + iteration + " / Fitness: " + function.evaluate(this.globalBest));
		}
		
		System.out.println("Solution found at iteration: " + iteration + " / Final fitness: " + function.evaluate(this.globalBest));
//		System.out.println("Position: " + this.globalBest);
		return this.globalBest;
	}

	private void initializeSwarm() {
		this.particles = new Particle[PARTICLE_COUNT];
		Particle particle;
		for (int i=0; i < PARTICLE_COUNT; i++) {
			particle = new Particle();
			particle.setVelocity(generateRandomlyInitialisedArray(DIMENSIONS));
			Position position = new Position(generateRandomlyInitialisedArray(DIMENSIONS));
			particle.setLocation(position);
			particles[i] = particle;
		}
	}
	
	private double[] generateRandomlyInitialisedArray(int dimensions) {
		Random generator = new Random();
		double[] values = new double[dimensions];
		for(int i=0; i < dimensions; i++) {
			values[i] = generator.nextDouble();
		}
		return values;
	}
}
