package com.github.daibhin;

import com.dreizak.miniball.highdim.Miniball;
import com.dreizak.miniball.model.ArrayPointSet;
import com.github.daibhin.Functions.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Sigmoid_GIDN extends PSO {

	private int iteration = 0;
	private int SWARM_SIZE = 50;
	private int DIMENSIONS = 30;
	private double MAX_ITERATIONS = 10000.0;
	private double CONSTRICTION_FACTOR = 0.72984;
	private double C_1 = 2.05;
	private double C_2 = 2.05;
	private BoundaryCondition boundary;
	private boolean ignoreBoundaries = false;
	private Position globalBest;
	private double globalFitness;

	private Random generator;
	private Run runTracker;

	private double avgPathLength;

	public Sigmoid_GIDN(Function function, BoundaryCondition boundary, int dimensions, boolean noBounds, Run runStats, int numIter) {
		this.function = function;
		this.boundary = boundary;
		this.DIMENSIONS = dimensions;
		this.ignoreBoundaries = noBounds;
		this.runTracker = runStats;
		this.MAX_ITERATIONS = numIter;
		this.generator = new Random();
		initializeSwarm();
		setupNeighbourhoods();
	}

	@Override
	public Position run() {
		this.avgPathLength = calculateAvgPathLength();
		int particleIndex = 0;
		while (iteration < MAX_ITERATIONS) {

			// UPDATE NEIGHBOURHOOD //
			this.avgPathLength = calculateAvgPathLength();
			double desiredPathLength = pathLengthForIteration();
			while(this.avgPathLength > desiredPathLength) {
				Particle particle = this.particles[particleIndex];
				ArrayList<Particle> neighbourhoodParticles = particle.getNeighbourhood().getParticles();
				ArrayList<Particle> copiedParticles = new ArrayList<Particle>(Arrays.asList(this.particles));
				copiedParticles.removeAll(neighbourhoodParticles);
				copiedParticles.remove(particle);
				ArrayList<Particle> newParticles = randomlySelectedParticles(copiedParticles, 1);
				particle.getNeighbourhood().addToNeighbourhood(newParticles);

				if(particleIndex == (SWARM_SIZE-1)) {
					particleIndex = 0;
				} else {
					particleIndex++;
				}
				this.avgPathLength = calculateAvgPathLength();
			}

			for (int index = 0; index < SWARM_SIZE; index++) {
				Particle particle = particles[index];

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


//			if (iteration % 10 == 0) {
//				System.out.println("Iteration: " + iteration + " / Path Length: " + calculateAvgPathLength());
//			}

			this.runTracker.setConvergenceValue(iteration, this.globalFitness);
			this.runTracker.setAvgPathLength(iteration, this.avgPathLength);
			this.runTracker.setClusteringCoefficientValue(iteration, calculateClusteringCoefficient());
			if (iteration == 1000 - 1) {
				this.runTracker.setOneThousandValue(this.globalFitness);
			}
			if (iteration == 10000 - 1) {
				this.runTracker.setTenThousandValue(this.globalFitness);
			}
			iteration++;
		}
		System.out.println(function.evaluate(this.globalBest));
		return this.globalBest;
	}

	private double calculateAvgPathLength() {
		double[] response = GraphUtilities.averagePathLength(SWARM_SIZE, this.particles);
		double averagePathLength = response[0];
		return averagePathLength;
	}

	private double calculateClusteringCoefficient() {
		return GraphUtilities.clusteringCoefficient(SWARM_SIZE, this.particles);
	}

	private double calculateEnclosingRadius() {
		ArrayPointSet ps = new ArrayPointSet(DIMENSIONS, SWARM_SIZE);
		for (int i = 0; i < SWARM_SIZE; ++i) {
			for (int j = 0; j < DIMENSIONS; ++j) {
				ps.set(i, j, particles[i].getLocation().getValues()[j]);
			}
		}
		Miniball miniball = new Miniball(ps);
		return miniball.radius();
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
		int min = Math.min(copiedParticles.size(), particlesToAdd);
		for (int i=0; i < min; i++) {
			selectedParticles.add(copiedParticles.get(i));
		}
		return selectedParticles;
	}

	private double min = 1.0;
	private double max = 12.75;
	private double pathLengthForIteration() {
		double mapped = (double) this.iteration / 500.0;
		double shifted = mapped - 10;
		double sigmoid = (1.0/(1 + Math.exp(shifted)));

		double mapUp = sigmoid * (max - min);
		double shiftUp = mapUp + min;

		return shiftUp;
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
		return "APL_GIDN";
	}
}