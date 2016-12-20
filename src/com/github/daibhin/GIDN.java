package com.github.daibhin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.github.daibhin.Functions.Func;

public class GIDN extends PSO {
	
	private int SWARM_SIZE = 50;
	private int DIMENSIONS = 30;
	private double MAX_ITERATIONS = 10000.0;
	private double CONSTRICTION_FACTOR = 0.72984;
	private double C_1 = 2.05;
	private double C_2 = 2.05;
	private Random generator;
	private BoundaryCondition boundary;
	private boolean ignoreBoundaries = false;
	
	private Position globalBest;
	private double globalFitness;
	
	private int iteration;
	private double gamma = 2.0;
	private double INITIAL_NEIGHBOURHOOD_PARTICLE_COUNT = 2;
		
	public GIDN(Func function, BoundaryCondition boundary, int dimensions, boolean noBounds) {
		this.function = function;
		this.boundary = boundary;
		this.ignoreBoundaries = noBounds;
		this.DIMENSIONS = dimensions;
		this.generator = new Random();
		initializeSwarm();
		this.iteration = 0;
	}

	@Override
	public Position run() {
		while (iteration < MAX_ITERATIONS) {
			
			// EVALUATE CONVERGENCE //
//			if (this.globalFitness == function.getOptimum()) {
//				return this.globalBest;
//			}
			
			for (int index = 0; index < SWARM_SIZE; index++) {
				Particle particle = particles[index];
				
				// UPDATE NEIGHBOURHOOD //
				ArrayList<Particle> neighbourhoodParticles = particle.getNeighbourhood().getParticles();
				if(neighbourhoodSizeForIteration() > neighbourhoodParticles.size()) {
					int particlesToAdd = neighbourhoodSizeForIteration() - neighbourhoodParticles.size();
					ArrayList<Particle> copiedParticles = new ArrayList<Particle>(Arrays.asList(this.particles));
					copiedParticles.removeAll(neighbourhoodParticles);
					ArrayList<Particle> newParticles = randomlySelectedParticles(copiedParticles, particlesToAdd);
					particle.getNeighbourhood().addToNeighbourhood(newParticles);
				}
				if (iteration == 0) {
					particle.getNeighbourhood().setInitialBestPosition(function);
				}
				
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
//				printNeighbourhood(particle, particle.getNeighbourhood().getParticles());
				particle.setVelocity(newVelocity);
				
				// UPDATE PARTICLE POSITION //
				double[] newLocation = new double[DIMENSIONS];
				for (int dim=0; dim < DIMENSIONS; dim++) {
					newLocation[dim] = particle.getLocation().getValues()[dim] + newVelocity[dim];
				}
				particle.setLocation(new Position(newLocation));
				
				// BOUNDARY CHECK //
				if(ignoreBoundaries || particle.withinBounds(function)) {
					
					double currentFitness = function.evaluate(particle.getLocation());
					
					// UPDATE PERSONAL BEST //
					if (function.isFitter(currentFitness, particle.getBestFitness())) {
						particle.setPersonalBest(currentFitness);
					}
					
					// UPDATE GLOBAL BEST //
					if (function.isFitter(currentFitness, this.globalFitness)) {
						this.globalBest = particle.getLocation();
						this.globalFitness = currentFitness;
					}
				} else {
					boundary.handleParticle(particle, function);
				}
				
				// UPDATE NEIGHBOURHOOD BEST //
				particle.getNeighbourhood().updateBestPosition(function);
				
			}
			
			
//			System.out.println(neighbourhoodSizeForIteration());
//			if (iteration % 10 == 0) {
//				System.out.println("Iteration: " + iteration + " / Fitness: " + this.globalFitness));
//			}
			iteration++;
		}
		return this.globalBest;
	}
	
	private void printNeighbourhood(Particle particle, ArrayList<Particle> neighbourhoodParticles) {
		System.out.println("Particle: " + findPosition(particle));
		String neighbourIndices = "";
		for (int i=0; i<neighbourhoodParticles.size(); i++) {
			neighbourIndices += " " + findPosition(neighbourhoodParticles.get(i));
		}
		System.out.println("Neighbourhood Particles: [" + neighbourIndices + "]");
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
	
	private ArrayList<Particle> randomlySelectedParticles(ArrayList<Particle> copiedParticles, int particlesToAdd) {
		ArrayList<Particle> selectedParticles = new ArrayList<Particle>();
		Collections.shuffle(copiedParticles);
		for (int i=0; i < particlesToAdd; i++) {
			selectedParticles.add(copiedParticles.get(i));
		}
		return selectedParticles;
	}

	private int neighbourhoodSizeForIteration() {
		double iterationIncrease = (Math.pow(this.iteration/MAX_ITERATIONS, this.gamma)*(SWARM_SIZE-1));
		return (int) Math.floor(iterationIncrease + INITIAL_NEIGHBOURHOOD_PARTICLE_COUNT);
	}
	
	private void initializeSwarm() {
		this.particles = new Particle[SWARM_SIZE];
		Particle particle;
		for (int i = 0; i < SWARM_SIZE; i++) {
			particle = new Particle();

			// SET INITIAL POSITION //
			Position position = new Position(randomProblemSpacePosition());
			particle.setLocation(position);

			// SET INITIAL VELOCITY //
			double[] initialVelocity = halfDiffVelocityArray(particle);
			particle.setVelocity(initialVelocity);

			// ADD PARTICLE //
			particles[i] = particle;

			// SET PERSONAL & GLOBAL BESTS //
			double currentFitness = function.evaluate(particle.getLocation());
			particle.setPersonalBest(currentFitness);
			if (i == 0 || function.isFitter(currentFitness, this.globalFitness)) {
				this.globalBest = particle.getLocation();
				this.globalFitness = currentFitness;
			}
			
			// INITIALIZE NEIGHBOURHOOD //
			particle.setNeighbourhood(new ArrayList<Particle>(), this.function, this.particles);
		}
	}
	
	private double[] randomProblemSpacePosition() {
		double[] values = new double[DIMENSIONS];
		for (int i = 0; i < DIMENSIONS; i++) {
			double max = function.getUpperBound();
			double min = function.getLowerBound();
			double rangeRandom = min + (max - min) * this.generator.nextDouble();
			values[i] = rangeRandom;
		}
		return values;
	}

	private double[] halfDiffVelocityArray(Particle particle) {
		double[] values = new double[DIMENSIONS];
		for (int i = 0; i < DIMENSIONS; i++) {
			double max = function.getUpperBound();
			double min = function.getLowerBound();
			double rangeRandom = min + (max - min) * this.generator.nextDouble();
			double ans = (rangeRandom - particle.getLocation().getValues()[i]) / 2;
			values[i] = ans;
		}
		return values;
	}
	
	private void updateNeighbourhood(Particle particle) {
		
	}
}
