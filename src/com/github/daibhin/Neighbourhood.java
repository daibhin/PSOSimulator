package com.github.daibhin;

import java.util.ArrayList;

import com.github.daibhin.Functions.Function;

public class Neighbourhood {
	private ArrayList<Particle> particles;
	private Position neighbourhoodBest;
	private double bestFitness;

	public static void printNeighbourhood(Particle particle, ArrayList<Particle> neighbourhoodParticles) {
		System.out.println("Particle: " + findPosition(particle, neighbourhoodParticles));
		String neighbourIndices = "";
		for (int i=0; i < neighbourhoodParticles.size(); i++) {
			neighbourIndices += " " + findPosition(neighbourhoodParticles.get(i), neighbourhoodParticles);
		}
		System.out.println("Neighbourhood Particles: [" + neighbourIndices + "]");
		System.out.println("************");
	}

	private static int findPosition(Particle p, ArrayList<Particle> particles) {
		for(int i=0; i< particles.size(); i++) {
			if (particles.get(i) == p) {
				return i;
			}
		}
		return -1;
	}
	
	public Neighbourhood(ArrayList<Particle> particles, Function function) {
		this.particles = particles;
		setInitialBestPosition(function);
	}
	
	public void setInitialBestPosition(Function function) {
		if (particles.size() > 0) {
			this.neighbourhoodBest = particles.get(0).getLocation();
			this.bestFitness = particles.get(0).getBestFitness();
			this.updateBestPosition(function);
		}
	}
	
	public void updateBestPosition(Function function) {
		for(int i=0; i< particles.size(); i++) {
			double particleBest = particles.get(i).getBestFitness();
			if (function.isFitter(particleBest, this.bestFitness)) {
				this.neighbourhoodBest = particles.get(i).getPersonalBest();
				this.bestFitness = particleBest;
			}
		}
	}
	
	public ArrayList<Particle> getParticles() {
		return particles;
	}
	
	public void setParticles(ArrayList<Particle> particles) {
		this.particles = particles;
	}
	
	public Position getNeighbourhoodBest() {
		return neighbourhoodBest;
	}
	
	public void addToNeighbourhood(ArrayList<Particle> newParticles) {
		this.particles.addAll(newParticles);
	}
}
