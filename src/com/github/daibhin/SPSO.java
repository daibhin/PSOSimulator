package com.github.daibhin;

import java.util.Random;

import org.jfree.data.xy.XYSeries;

import com.github.daibhin.Functions.Function;

public class SPSO extends PSO {
	
	private int SWARM_SIZE = 50;
	private int DIMENSIONS = 3;
	private int MAX_ITERATIONS = 100;
	private double CONSTRICTION_FACTOR = 0.72984;
	private double C_1 = 2.05;
	private double C_2 = 2.05;
	private Random generator;
	
	private Function function;
	private Particle[] particles;
	private Position globalBest;
	
	public SPSO(Function function) {
		this.function = function;
		this.generator = new Random();
		initializeSwarm();
		setupNeighbourhoods();
	}

	@Override
	public Position run() {
		int iteration = 0;
		while(iteration < MAX_ITERATIONS) {
			
			// EVALUATE CONVERGENCE //
			if (function.evaluate(this.globalBest) == function.getOptimum()) {
				return this.globalBest;
			}
			
			for (int index=0; index < SWARM_SIZE; index++) {
				Particle particle = this.particles[index];
				
				// GENERATE & UPDATE PARTICLE VELOCITY //
				double[] newVelocity = new double[DIMENSIONS];
				for (int vel=0; vel < DIMENSIONS; vel++) {
					double r1 = this.generator.nextDouble();
					double r2 = this.generator.nextDouble();
					double personalContribution = (C_1*r1)*(particle.getPersonalBest().getValues()[vel] - particle.getLocation().getValues()[vel]);
					double socialContribution = (C_2*r2)*(particle.getNeighbourhood().getNeighbourhoodBest().getValues()[vel] - particle.getLocation().getValues()[vel]);
					newVelocity[vel] = CONSTRICTION_FACTOR*(particle.getVelocity()[vel] + personalContribution + socialContribution);
				}
				particle.setVelocity(newVelocity);
				
				double[] newPosition = new double[DIMENSIONS];
				for (int dim=0; dim < DIMENSIONS; dim++) {
					newPosition[dim] = particle.getLocation().getValues()[dim] + newVelocity[dim];
				}
				particle.setLocation(new Position(newPosition));
				
				// UPDATE PERSONAL BEST
				Position currentLocation = particle.getLocation();
				if (function.isFitter(currentLocation, particle.getPersonalBest())) {
					double currentFitness = function.evaluate(currentLocation);
					double bestFitness = function.evaluate(particle.getPersonalBest());
					particle.setPersonalBest(particle.getLocation());
				}
				
				// UPDATE NEIGHBOURHOOD BEST
				Neighbourhood neighbourhood = particle.getNeighbourhood();
				if (function.isFitter(currentLocation, neighbourhood.getNeighbourhoodBest())) {
					double neighbourhoodBest = function.evaluate(neighbourhood.getNeighbourhoodBest());
//					neighbourhood.setNeighbourhoodBest(currentLocation);
//					neighbourhood.updateBestPosition(function);
				}
				
				if (function.isFitter(currentLocation, this.globalBest)) {
					this.globalBest = currentLocation;
				}
				
			}
//			System.out.println("Iter: " + iteration + " / Fitness: " + function.evaluate(this.globalBest));
			iteration++;
		}
		return this.globalBest;
	}
	
	private void initializeSwarm() {
		this.particles = new Particle[SWARM_SIZE];
		for(int index=0; index < SWARM_SIZE; index++) {
			Particle particle = new Particle();
			
			// set velocity and position
			particle.setVelocity(randomInitialVelocity());
			Position location = new Position(randomInitialLocation());
			particle.setLocation(location);
			
			this.particles[index] = particle;
			
			// set bests
			particle.setPersonalBest(particle.getLocation());
			if(index == 0 || function.isFitter(particle.getLocation(), this.globalBest)) {
				this.globalBest = particle.getLocation();
			}
		}
	}
	
	private void setupNeighbourhoods() {
		Particle[] neighbourhoodParticles = new Particle[3]; //ring topology
		for(int index=0; index < SWARM_SIZE; index++) {
			if (index == 0) {
				neighbourhoodParticles[0] = this.particles[SWARM_SIZE - 1];
				neighbourhoodParticles[1] = this.particles[1];
				neighbourhoodParticles[2] = this.particles[0];
			} else if  (index == (SWARM_SIZE - 1)) {
				neighbourhoodParticles[0] = this.particles[SWARM_SIZE - 2];
				neighbourhoodParticles[1] = this.particles[0];
				neighbourhoodParticles[2] = this.particles[SWARM_SIZE - 1];
			} else {
				neighbourhoodParticles[0] = this.particles[index + 1];
				neighbourhoodParticles[1] = this.particles[index - 1];
				neighbourhoodParticles[2] = this.particles[index];
			}
			
			Particle particle = this.particles[index];
			
			particle.setNeighbourhood(neighbourhoodParticles);
			particle.getNeighbourhood().updateBestPosition(this.function);
//			System.out.println("Particle: " + index + " / Neighbourhood Particles: [" + findPosition(neighbourhoodParticles[0]) + " , " + findPosition(neighbourhoodParticles[1]) + "]");
		}
	}
	
	private int findPosition(Particle p) {
		for (int i=0; i<SWARM_SIZE; i++) {
			if (this.particles[i] == p) {
				return i;
			}
		}
		return -1;
	}
	
	private double[] randomInitialLocation() {
		double[] coordinates = new double[DIMENSIONS];
		for(int i=0; i<DIMENSIONS; i++) {
			coordinates[i] = this.generator.nextDouble()*function.getUpperBound();
		}
		return coordinates;
	}

	private double[] randomInitialVelocity() {
		double[] velocity = new double[DIMENSIONS];
		for(int i=0; i<DIMENSIONS; i++) {
			velocity[i] = this.generator.nextDouble();
		}
		return velocity;
	}
}
