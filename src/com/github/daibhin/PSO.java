package com.github.daibhin;

import java.util.ArrayList;
import java.util.Random;

import com.github.daibhin.Functions.Func;

public abstract class PSO {
	
	Particle[] particles;
	int maxIterations = 10000;
	Func function;

	private Position globalBest;
	private double globalFitness;

	public int SWARM_SIZE;
	public abstract Position run();
	public abstract String getName();


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
}