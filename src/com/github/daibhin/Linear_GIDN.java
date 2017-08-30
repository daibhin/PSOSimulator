package com.github.daibhin;

import com.github.daibhin.Functions.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Linear_GIDN extends PSO {

	private BoundaryCondition boundary;
	private boolean ignoreBoundaries = false;
	private Position globalBest;
	private double globalFitness;

	private Random generator;
	private Run runTracker;

	private int INITIAL_NEIGHBOURHOOD_PARTICLE_COUNT = 2;
	private double avgPathLength;
	private double avgNumberInfinitePaths;
	private double startingAverage;
	private int edgeAdditionIndex = 0;

	public Linear_GIDN(Function function, BoundaryCondition boundary, int dimensions, boolean noBounds, Run runStats, int numIter) {
		this.function = function;
		this.boundary = boundary;
		this.DIMENSIONS = dimensions;
		this.ignoreBoundaries = noBounds;
		this.runTracker = runStats;
		this.MAX_ITERATIONS = numIter;
		this.generator = new Random();

		initializeSwarm();
	}

	@Override
	public Position run() {
		this.avgPathLength = calculateAvgPathLength();
		int iteration = 0;
		while (iteration < MAX_ITERATIONS) {

			for (int index = 0; index < SWARM_SIZE; index++) {
				Particle particle = particles[index];

				// UPDATE NEIGHBOURHOOD //
				if (iteration == 0) {
					addParticlesToNeighbourhood(INITIAL_NEIGHBOURHOOD_PARTICLE_COUNT, particle);
					this.startingAverage = this.avgPathLength;
//					this.startingAverage = calculateAvgPathLength();
				} else {
					double desiredAverage = averagePathLengthForIteration(iteration);
					if (desiredAverage <= this.avgPathLength && (index == edgeAdditionIndex)) {
						addParticlesToNeighbourhood(1, particle);
					}
				}
				if (iteration == 0) {
					particle.getNeighbourhood().setInitialBestPosition(function);
				}

				// GENERATE & UPDATE PARTICLE VELOCITY //
				double[] newVelocity = new double[DIMENSIONS];
				for (int vel = 0; vel < DIMENSIONS; vel++) {
					double r1 = this.generator.nextDouble();
					double r2 = this.generator.nextDouble();
					double personalContribution = (C_1 * r1) * (particle.getPersonalBest().getValues()[vel] - particle.getLocation().getValues()[vel]);
					double neighbourhoodBest = particle.getNeighbourhood().getNeighbourhoodBest().getValues()[vel];
					double socialContribution = (C_2 * r2) * (neighbourhoodBest - particle.getLocation().getValues()[vel]);
					double currentVelocity = particle.getVelocity()[vel];
					newVelocity[vel] = CONSTRICTION_FACTOR * (currentVelocity + personalContribution + socialContribution);
				}
				particle.setVelocity(newVelocity);

				// UPDATE PARTICLE POSITION //
				double[] newLocation = new double[DIMENSIONS];
				for (int dim = 0; dim < DIMENSIONS; dim++) {
					newLocation[dim] = particle.getLocation().getValues()[dim] + newVelocity[dim];
				}
				particle.setLocation(new Position(newLocation));

				// BOUNDARY CHECK //
				if (ignoreBoundaries || particle.withinBounds(function)) {

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
			this.runTracker.setAvgPathLength(iteration, this.avgPathLength);
			this.runTracker.setAvgNumInfinitePaths(iteration, this.avgNumberInfinitePaths);
			this.runTracker.setClusteringCoefficientValue(iteration, calculateClusteringCoefficient());
			if (iteration == 1000 - 1) {
				this.runTracker.setOneThousandValue(this.globalFitness);
			}
			if (iteration == 10000 - 1) {
				this.runTracker.setTenThousandValue(this.globalFitness);
			}
			iteration++;
		}
		System.out.println(this.globalFitness);
		return this.globalBest;
	}

	private void addParticlesToNeighbourhood(int numParticlesToAdd, Particle particle) {
		ArrayList<Particle> neighbourhoodParticles = particle.getNeighbourhood().getParticles();
		ArrayList<Particle> copiedParticles = new ArrayList<Particle>(Arrays.asList(this.particles));
		copiedParticles.removeAll(neighbourhoodParticles);
		ArrayList<Particle> newParticles = randomlySelectedParticles(copiedParticles, numParticlesToAdd);
		particle.getNeighbourhood().addToNeighbourhood(newParticles);
		updateEdgeAdditionParticleIndex(); // add to a different particle each iteration
		this.avgPathLength = calculateAvgPathLength();
	}

	private void updateEdgeAdditionParticleIndex() {
		if (this.edgeAdditionIndex == (SWARM_SIZE - 1)) {
			this.edgeAdditionIndex = 0;
		} else {
			edgeAdditionIndex++;
		}
	}

	private double calculateAvgPathLength() {
		double[] response = GraphUtilities.averagePathLength(SWARM_SIZE, this.particles);
		double averagePathLength = response[0];
		double avgNumInfinitePaths = response[1];
		this.avgNumberInfinitePaths = avgNumInfinitePaths;
		return averagePathLength;
	}

	private static double MINIMUM_PATH_LENGTH = 1.0;
	private double averagePathLengthForIteration(int iteration) {
		return startingAverage - ((startingAverage - MINIMUM_PATH_LENGTH) * (iteration/MAX_ITERATIONS));
	}

	private double calculateClusteringCoefficient() {
		return GraphUtilities.clusteringCoefficient(SWARM_SIZE, this.particles);
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

	@Override
	public String getName() {
		return "LinearR_GIDN";
	}
}