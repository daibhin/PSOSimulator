package com.github.daibhin;

import com.github.daibhin.Functions.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Connected_GIDN extends PSO {

	private Random generator;
	private BoundaryCondition boundary;
	private boolean ignoreBoundaries = false;

	private Position globalBest;
	private double globalFitness;

	private int iteration;
	private double avgNumberInfinitePaths;
	private double gamma = 2.0;
	private double INITIAL_NEIGHBOURHOOD_PARTICLE_COUNT = 2;

	private Run runTracker;

	public Connected_GIDN(Function function, BoundaryCondition boundary, int dimensions, boolean noBounds, Run runStats, int numIter) {
		this.function = function;
		this.boundary = boundary;
		this.DIMENSIONS = dimensions;
		this.ignoreBoundaries = noBounds;
		this.runTracker = runStats;
		this.MAX_ITERATIONS = numIter;
		this.generator = new Random();
		initializeSwarm();
		setupNeighbourhoods();
		this.iteration = 0;
	}

	@Override
	public Position run() {
		while (iteration < MAX_ITERATIONS) {

			for (int index = 0; index < SWARM_SIZE; index++) {
				Particle particle = particles[index];
				
				// UPDATE NEIGHBOURHOOD //
				ArrayList<Particle> neighbourhoodParticles = particle.getNeighbourhood().getParticles();
				if(neighbourhoodSizeForIteration() > neighbourhoodParticles.size()) {
					int particlesToAdd = neighbourhoodSizeForIteration() - neighbourhoodParticles.size();
					ArrayList<Particle> copiedParticles = new ArrayList<Particle>(Arrays.asList(this.particles));
					copiedParticles.removeAll(neighbourhoodParticles);
					copiedParticles.remove(particle);
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

			this.runTracker.setConvergenceValue(iteration, this.globalFitness);
			this.runTracker.setAvgPathLength(iteration, calculateAvgPathLength());
			this.runTracker.setAvgNumInfinitePaths(iteration, this.avgNumberInfinitePaths);
			this.runTracker.setClusteringCoefficientValue(iteration, calculateClusteringCoefficient());
			if (iteration == (1000 - 1)) {
				this.runTracker.setOneThousandValue(this.globalFitness);
			}
			if (iteration == (10000 - 1)) {
				this.runTracker.setTenThousandValue(this.globalFitness);
			}
			iteration++;
		}
//		System.out.print(this.globalFitness);
		return this.globalBest;
	}

	private double calculateAvgPathLength() {
		double[] response = GraphUtilities.averagePathLength(this.particles, SWARM_SIZE);
		double averagePathLength = response[0];
		double avgNumInfinitePaths = response[1];
		this.avgNumberInfinitePaths = avgNumInfinitePaths;
		return averagePathLength;
	}
	
	private ArrayList<Particle> randomlySelectedParticles(ArrayList<Particle> copiedParticles, int particlesToAdd) {
		ArrayList<Particle> selectedParticles = new ArrayList<Particle>();
		Collections.shuffle(copiedParticles);
		int maxPossible = Math.min(particlesToAdd, copiedParticles.size());
		for (int i=0; i < maxPossible; i++) {
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
		}
	}

	private void setupNeighbourhoods() {
		for (int index = 0; index < SWARM_SIZE; index++) {
			ArrayList<Particle> neighbourhoodParticles = new ArrayList<Particle>(); // ring topology
			if (index == (SWARM_SIZE - 1)) { // last particle
				neighbourhoodParticles.add(this.particles[0]); // first
			} else { // other particles
				neighbourhoodParticles.add(this.particles[index + 1]); // next
			}
			Particle particle = this.particles[index];

			particle.setNeighbourhood(neighbourhoodParticles, this.function);
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

	@Override
	public String getName() {
		return "GIDN_PSO";
	}
}
