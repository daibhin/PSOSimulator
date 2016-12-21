package com.github.daibhin;

import com.dreizak.miniball.highdim.Miniball;
import com.dreizak.miniball.model.ArrayPointSet;
import com.dreizak.miniball.model.PointSet;
import com.github.daibhin.Functions.Func;

import java.util.Arrays;
import java.util.Random;

public class GlobalPSO extends PSO {

	private int SWARM_SIZE = 50;
	private int DIMENSIONS = 30;
	private int MAX_ITERATIONS = 10000;
	private double CONSTRICTION_FACTOR = 0.72984;
	private double C_1 = 2.05;
	private double C_2 = 2.05;
	private Random generator;
	private BoundaryCondition boundary;
	private boolean ignoreBoundaries = false;
	private Func function;
	private Particle[] particles;

	private Position globalBest;
	private double globalFitness;

	private Run runTracker;

	public GlobalPSO(Func function, BoundaryCondition boundary, int dimensions, Run runTracker, boolean noBounds, int numIter) {
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
		int boundaryExits = 0;
		int iteration = 0;
		while (iteration < MAX_ITERATIONS) {

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

					// EVALUATE CONVERGENCE //
//					if (function.evaluate(particle.getLocation()) == function.getOptimum()) {
////						System.out.println("Solution found at iteration " + (iteration));
////						System.out.println("Position: " + this.globalBest);
//						return particle.getLocation();
//					}
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
					boundaryExits++;
				}
			}
			
			this.runTracker.setConvergenceValue(iteration, this.globalFitness);
			this.runTracker.setClusteringValue(iteration, calculateEnclosingRadius());
			if (iteration == 1000 - 1) {
				this.runTracker.setOneThousandValue(this.globalFitness);
			}
			if (iteration == 10000 - 1) {
				this.runTracker.setTenThousandValue(this.globalFitness);
			}
			if (iteration % 100 == 0) {
//				System.out.println("Iteration: " + iteration + " / Fitness: " + this.globalFitness);
			}
			System.out.println("Iteration: " + iteration + " / Fitness: " + this.globalFitness);
			iteration++;
		}
//		System.out.println("Particles exited the boundary: " + boundaryExits);
//		System.out.println("Solution found after max iterations of " + iteration + " / Final fitness: "
//				+ this.globalFitness);
		return this.globalBest;
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
		return "GlobalPSO";
	}
}
