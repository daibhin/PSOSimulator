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
	private Random generator;
	private BoundaryCondition boundary;
	
	private Position globalBest;
	private Function function;
	private Particle[] particles;

	public GlobalPSO(Function function, BoundaryCondition boundary) {
		this.function = function;
		this.boundary = boundary;
		this.generator = new Random();
		initializeSwarm();
	}

	public Position run() {
		int boundaryExits = 0;
		int iteration = 0;
		while (iteration < MAX_ITERATIONS) {

			for (int index=0; index < PARTICLE_COUNT; index++) {
				Particle particle = particles[index];
				
				// EVALUATE CONVERGENCE //
				if (function.evaluate(particle.getLocation()) == function.getOptimum()) {
					System.out.println("Solution found at iteration " + (iteration));
					System.out.println("Position: " + this.globalBest);
					return particle.getLocation();
				}
				
				// GENERATE & UPDATE PARTICLE VELOCITY //
				double[] newVelocity = new double[DIMENSIONS];
				for(int vel=0; vel < DIMENSIONS; vel++) {
					double r1 = this.generator.nextDouble();
					double r2 = this.generator.nextDouble();
					double personalContribution = (C_1 * r1) * (particle.getPersonalBest().getValues()[vel] - particle.getLocation().getValues()[vel]);
					double socialContribution = (C_2 * r2) * (this.globalBest.getValues()[vel] - particle.getLocation().getValues()[vel]);
					double currentVelocity = particle.getVelocity()[vel];
					double updatedVel = CONSTRICTION_FACTOR*(currentVelocity + personalContribution + socialContribution);
					newVelocity[vel] = updatedVel;
				}
				particle.setVelocity(newVelocity);
				
				// UDPATE PARTICLE POSITION //
				double[] newLocation = new double[DIMENSIONS];
				for(int dim=0; dim < DIMENSIONS; dim++) {
					newLocation[dim] = particle.getLocation().getValues()[dim] + newVelocity[dim];
				}
				Position newPosition = new Position(newLocation);
				particle.setLocation(newPosition);

				// BOUNDARY CHECK
				if (particle.withinBounds(function)) {
					// UPDATE GLOBAL BEST
					if(iteration == 0 || function.isFitter(particle.getLocation(), this.globalBest)) {
						this.globalBest = particle.getLocation();
					}
					
					// UPDATE PERSONAL BEST
					if(function.isFitter(particle.getLocation(), particle.getPersonalBest())) {
						particle.setPersonalBest(particle.getLocation());
					}
					
				} else {
					boundary.handleParticle(particle, function);
					boundaryExits++;
				}
			}

			iteration++;
			System.out.println("Iteration: " + iteration + " / Fitness: " + function.evaluate(this.globalBest));
		}		
		System.out.println("Particles exited the boundary: " + boundaryExits);
		System.out.println("Solution found after max iterations of " + iteration + " / Final fitness: " + function.evaluate(this.globalBest));
		return this.globalBest;
	}

	private void initializeSwarm() {
		this.particles = new Particle[PARTICLE_COUNT];
		Particle particle;
		for (int i=0; i < PARTICLE_COUNT; i++) {
			particle = new Particle();
			
			// SET INITIAL POSITION
			Position position = new Position(randomProblemSpacePosition(DIMENSIONS));
			particle.setLocation(position);
			
			// SET INITIAL VELOCITY
			double[] initialVelocity = halfDiffVelocityArray(DIMENSIONS, particle);
			particle.setVelocity(initialVelocity);
			
			particles[i] = particle;
			
			// SET PERSONAL & GLOBAL BESTS
			particle.setPersonalBest(particle.getLocation());
			if(i == 0 || function.isFitter(particle.getLocation(), this.globalBest)) {
				this.globalBest = particle.getLocation();
			}
		}
	}
	
	private double[] randomProblemSpacePosition(int dimensions) {
		double[] values = new double[dimensions];
		for(int i=0; i < dimensions; i++) {
			double max = function.getUpperBound();
			double min = function.getLowerBound();
			double rangeRandom = min + (max - min) * this.generator.nextDouble();
			values[i] = rangeRandom;
		}
		return values;
	}
	
	private double[] halfDiffVelocityArray(int dimensions, Particle particle) {
		double[] values = new double[dimensions];
		for(int i=0; i < dimensions; i++) {
			double max = function.getUpperBound();
			double min = function.getLowerBound();
			double rangeRandom = min + (max - min) * this.generator.nextDouble();
			double ans = (rangeRandom - particle.getLocation().getValues()[i]) / 2;
			values[i] = ans;
		}
		return values;
	}
}
