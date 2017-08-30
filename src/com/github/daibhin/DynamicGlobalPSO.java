package com.github.daibhin;

import com.dreizak.miniball.highdim.Miniball;
import com.dreizak.miniball.model.ArrayPointSet;
import com.github.daibhin.Functions.Function;

import java.util.Random;

public class DynamicGlobalPSO extends PSO {

	private Random generator;
	private BoundaryCondition boundary;
	private boolean ignoreBoundaries = false;
	private Function function;
	private Particle[] particles;

	private Position globalBest;
	private double globalFitness;

	private Run runTracker;

	public DynamicGlobalPSO(Function function, BoundaryCondition boundary, int dimensions, Run runTracker, boolean noBounds, int numIter) {
		this.function = function;
		this.boundary = boundary;
		this.DIMENSIONS = dimensions;
		this.generator = new Random();
		this.runTracker = runTracker;
		this.ignoreBoundaries = noBounds;
		this.MAX_ITERATIONS = numIter;
		initializeSwarm();
	}

	public Position run() {
		int iteration = 0;
		while (iteration < MAX_ITERATIONS) {

			recalculateC1AndC2(iteration);

			for (int index = 0; index < SWARM_SIZE; index++) {
				Particle particle = particles[index];

				// GENERATE & UPDATE PARTICLE VELOCITY //
				double[] newVelocity = new double[DIMENSIONS];
				for (int vel = 0; vel < DIMENSIONS; vel++) {
					double r1 = this.generator.nextDouble();
					double r2 = this.generator.nextDouble();
					double personalContribution = (C_1 * r1)
							* (particle.getPersonalBest().getValues()[vel] - particle.getLocation().getValues()[vel]);
					double socialContribution = (C_2 * r2)
							* (this.globalBest.getValues()[vel] - particle.getLocation().getValues()[vel]);
					double currentVelocity = particle.getVelocity()[vel];
					double updatedVel = CONSTRICTION_FACTOR
							* (currentVelocity + personalContribution + socialContribution);
					newVelocity[vel] = updatedVel;
				}
				particle.setVelocity(newVelocity);

				// UDPATE PARTICLE POSITION //
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
			}
			
			this.runTracker.setConvergenceValue(iteration, this.globalFitness);
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

	private static double cTotal = 4.1;
	private void recalculateC1AndC2(int iteration) {
		double c1Percentage = (((double)iteration)/MAX_ITERATIONS);
		C_1 = c1Percentage * cTotal;
		C_2 = cTotal - C_1;
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
		return "DynamicGlobalPSO";
	}
}
