package com.github.daibhin;

import java.util.Random;

public class GlobalPSO extends PSO {
	
	private int PARTICLE_COUNT = 50;
	private int DIMENSIONS = 10;
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
			double fitness = function.evaluate(particle.getPosition());
			particle.setBestPersonalFitness(fitness);
			particle.setPersonalBest(particle.getPosition());
		}

		int iteration = 0;
		while (iteration < MAX_ITERATIONS) {
			
			// update global best
			Particle currentBestParticle = getMinimumFitnessParticle();
			double fitness = function.evaluate(currentBestParticle.getPosition());
			if(iteration == 0 || fitness < this.globalFitness) {
				this.globalFitness = fitness;
				this.globalBest = currentBestParticle.getPosition();
			}
			
			for (int i=0; i < PARTICLE_COUNT; i++) {
				Particle particle = particles[i];
				
				
				// update personal best
				double personalFitness = function.evaluate(particle.getPosition());
				
				if(personalFitness < particle.getBestPersonalFitness()) {
					particle.setBestPersonalFitness(personalFitness);
					particle.setPersonalBest(particle.getPosition());
				}

				if (function.evaluate(particle.getPosition()) == 0.0) {
					System.out.println("\nSolution found at iteration " + (iteration));
					System.out.println(function.evaluate(this.globalBest));
					return particle.getPosition();
				}
				
				// update particle velocity
				Random generator = new Random();
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();

				double[] updatedVelocity = new double[DIMENSIONS];
				for(int vel=0; vel < DIMENSIONS; vel++) {
					double personalContribution = (C_1 * r1) * (particle.getBestPersonalPosition().getValues()[vel] - particle.getPosition().getValues()[vel]);
					double globalContribution = (C_2 * r2) * (this.globalBest.getValues()[vel] - particle.getPosition().getValues()[vel]);
					double newVel = CONSTRICTION_FACTOR*(particle.getVelocity()[vel] + personalContribution + globalContribution);
					updatedVelocity[vel] = newVel;
				}
				particle.setVelocity(updatedVelocity);
				
				// update particle position
				double[] newLocation = new double[DIMENSIONS];
				for(int dim=0; dim < DIMENSIONS; dim++) {
					newLocation[dim] = particle.getPosition().getValues()[dim] + updatedVelocity[dim];
				}
				Position updatedPosition = new Position(newLocation);
				particle.setPosition(updatedPosition);				
			}
			iteration++;
		}
		
		System.out.println("\nSolution found at iteration " + (iteration));
		System.out.println(function.evaluate(this.globalBest));
		return this.globalBest;
	}
	
	private Particle getMinimumFitnessParticle() {
		Particle fittestParticle = null;
		for (int i=0; i < PARTICLE_COUNT; i++) {
			Particle particle = particles[i];
			double fitness = function.evaluate(particle.getPosition());
			
			if(i == 0 || fitness < function.evaluate(fittestParticle.getPosition())) {
				fittestParticle = particle;
			}
		}
		return fittestParticle;
	}

	private void initializeSwarm() {
		Random generator = new Random();
		this.particles = new Particle[PARTICLE_COUNT];
		for (int i=0; i < PARTICLE_COUNT; i++) {
			Particle particle = new Particle();
			
			double[] velocity = new double[DIMENSIONS];
			for(int j=0; j < DIMENSIONS; j++) {
				velocity[j] = generator.nextDouble();
			}
			
			particle.setVelocity(velocity);
			particle.setPosition(generatePosition());
			
			particles[i] = particle;
		}
	}
	
	private Position generatePosition() {
		Random generator = new Random();
		double[] values = new double[DIMENSIONS];
		for(int i=0; i < DIMENSIONS; i++) {
			values[i] = generator.nextDouble();
		}
		return new Position(values);
	}
}
