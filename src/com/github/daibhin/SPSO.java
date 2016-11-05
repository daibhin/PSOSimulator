package com.github.daibhin;

import java.util.ArrayList;
import java.util.Random;

import com.github.daibhin.Functions.Func;
import com.github.daibhin.Functions.Function;

public class SPSO extends PSO {
	
	private int SWARM_SIZE = 50;
	private int DIMENSIONS = 30;
	private int MAX_ITERATIONS = 10000;
	private double CONSTRICTION_FACTOR = 0.72984;
	private double C_1 = 2.05;
	private double C_2 = 2.05;
	private Random generator;
	
	private Func function;
	private Particle[] particles;
	private Position globalBest;
	private BoundaryCondition boundary;
	
	public SPSO(Func function, BoundaryCondition boundary, int dimensions) {
		this.function = function;
		this.generator = new Random();
		this.boundary = boundary;
		this.DIMENSIONS = dimensions;
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
				for(int vel=0; vel < DIMENSIONS; vel++) {
					double r1 = this.generator.nextDouble();
					double r2 = this.generator.nextDouble();
					double personalContribution = (C_1*r1)*(particle.getPersonalBest().getValues()[vel] - particle.getLocation().getValues()[vel]);
					double neighbourhoodBest = particle.getNeighbourhood().getNeighbourhoodBest().getValues()[vel];
					double socialContribution = (C_2*r2)*(neighbourhoodBest - particle.getLocation().getValues()[vel]);
					double currentVelocity = particle.getVelocity()[vel];
					newVelocity[vel] = CONSTRICTION_FACTOR*(currentVelocity + personalContribution + socialContribution);
				}
				particle.setVelocity(newVelocity);
				
				// UPDATE PARTICLE POSITION //
				double[] newLocation = new double[DIMENSIONS];
				for (int dim=0; dim < DIMENSIONS; dim++) {
					newLocation[dim] = particle.getLocation().getValues()[dim] + newVelocity[dim];
				}
				particle.setLocation(new Position(newLocation));
				
				// BOUNDARY CHECK //
				if(particle.withinBounds(function)) {
					
					// UPDATE PERSONAL BEST //
					if (function.isFitter(particle.getLocation(), particle.getPersonalBest())) {
						particle.setPersonalBest(particle.getLocation());
					}
					
					// UPDATE GLOBAL BEST //
					if (function.isFitter(particle.getLocation(), this.globalBest)) {
						this.globalBest = particle.getLocation();
					}
				} else {
					boundary.handleParticle(particle, function);
				}
				
				// UPDATE NEIGHBOURHOOD BEST //
				particle.getNeighbourhood().updateBestPosition(function);
			}
			
			if (iteration % 10 == 0) {
				System.out.println("Iteration: " + iteration + " / Fitness: " + function.evaluate(this.globalBest));
			}
			
			iteration++;
		}
		return this.globalBest;
	}
	
	private void initializeSwarm() {
		this.particles = new Particle[SWARM_SIZE];
		for(int index=0; index < SWARM_SIZE; index++) {
			Particle particle = new Particle();
			
			// SET INITIAL POSITION //
			Position location = new Position(randomProblemSpacePosition());
			particle.setLocation(location);
			
			// SET INITIAL VELOCITY //
			particle.setVelocity(halfDiffVelocityArray(particle));
			
			// ADD PARTICLE //
			this.particles[index] = particle;
			
			// SET PERSONAL AND GLOBAL BEST //
			particle.setPersonalBest(particle.getLocation());
			if(index == 0 || function.isFitter(particle.getLocation(), this.globalBest)) {
				this.globalBest = particle.getLocation();
			}
		}
	}
	
	private void setupNeighbourhoods() {
		for(int index=0; index < SWARM_SIZE; index++) {
			ArrayList<Particle> neighbourhoodParticles = new ArrayList<Particle>(); // ring topology
			if (index == 0) { // first particle
				neighbourhoodParticles.add(this.particles[SWARM_SIZE - 1]); // last
				neighbourhoodParticles.add(this.particles[1]); // second
				neighbourhoodParticles.add(this.particles[0]); // itself (first)
			} else if (index == (SWARM_SIZE - 1)) { // last particle
				neighbourhoodParticles.add(this.particles[SWARM_SIZE - 2]); // second last
				neighbourhoodParticles.add(this.particles[0]); // first
				neighbourhoodParticles.add(this.particles[SWARM_SIZE - 1]); // itself (last)
			} else { // other particles
				neighbourhoodParticles.add(this.particles[index + 1]); // before
				neighbourhoodParticles.add(this.particles[index - 1]); //after
				neighbourhoodParticles.add(this.particles[index]); // itself
			}
			
			Particle particle = this.particles[index];

			particle.setNeighbourhood(neighbourhoodParticles, this.function, this.particles);
		}
	}
	
	private void printNeighbourhood(Particle particle, Particle[] neighbourhoodParticles) {
		Particle neighbour1 = neighbourhoodParticles[0];
		Particle neighbour2 = neighbourhoodParticles[1];
		System.out.println("Particle: " + findPosition(particle));
		System.out.println("Neighbourhood Particles: [" + findPosition(neighbour1) + " , " + findPosition(neighbour2) + "]");
		double bestFitness = function.evaluate(particle.getNeighbourhood().getNeighbourhoodBest());
		System.out.println("Particle Position: " + particle.getPersonalBest());
		System.out.println("Particle fitness: " + function.evaluate(particle.getPersonalBest()));
		System.out.println("Neighbour 1 Position: " + neighbour1.getPersonalBest());
		System.out.println("Neighbour 1 Fitness: " + function.evaluate(neighbour1.getPersonalBest()));
		System.out.println("Neighbour 2 Position: " + neighbour2.getPersonalBest());
		System.out.println("Neighbour 2 Fitness: " + function.evaluate(neighbour2.getPersonalBest()));
		System.out.println("Neighbourhood fitness: " + bestFitness);
		System.out.println("************");
	}
	
	private void printConvergenceDetails() {
		for (int s=0; s < SWARM_SIZE; s++) {
			System.out.println("P" + s + ": " + function.evaluate(particles[s].getPersonalBest()) + " @ " + particles[s].getPersonalBest());
			Particle p = particles[s];
			Position nBest = p.getNeighbourhood().getNeighbourhoodBest();
			double bestFitness = function.evaluate(nBest);
			System.out.println("N" + s + ": " + bestFitness + " @ " + nBest);
			ArrayList<Particle> pars = particles[s].getNeighbourhood().getParticles();
			System.out.print(findPosition(particles[s]) + "Particles: ");
			for(int l=0; l < 3; l++) {
				System.out.print(function.evaluate(pars.get(l).getPersonalBest()) + "   ");
			}
			System.out.println("");
			System.out.println("----");
		}
		System.out.println("************");
	}
	
	private int findPosition(Particle p) {
		for(int i=0; i< SWARM_SIZE; i++) {
			if (this.particles[i] == p) {
				return i;
			}
		}
		return -1;
	}

	private double[] randomProblemSpacePosition() {
		double[] coordinates = new double[DIMENSIONS];
		for(int i=0; i < DIMENSIONS; i++) {
			double max = function.getUpperBound();
			double min = function.getLowerBound();
			double rangeRandom = min + (max - min) * this.generator.nextDouble();
			coordinates[i] = rangeRandom;
		}
		return coordinates;
	}

	private double[] halfDiffVelocityArray(Particle particle) {
		double[] values = new double[DIMENSIONS];
		for(int i=0; i < DIMENSIONS; i++) {
			double max = function.getUpperBound();
			double min = function.getLowerBound();
			double rangeRandom = min + (max - min) * this.generator.nextDouble();
			double ans = (rangeRandom - particle.getLocation().getValues()[i]) / 2;
			values[i] = ans;
		}
		return values;
	}
}
