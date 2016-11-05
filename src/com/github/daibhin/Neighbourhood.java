package com.github.daibhin;

import java.util.ArrayList;

import com.github.daibhin.Functions.Func;

public class Neighbourhood {
	private ArrayList<Particle> particles;
	private Position neighbourhoodBest;
	
	
	public Neighbourhood(ArrayList<Particle> particles, Func function, Particle[] allParticles) {
		this.particles = particles;
		setInitialBestPosition(function);
		
//		for(int i=0; i< particles.length; i++) {
//			Particle p = particles[i];
//			for(int j=0; j< allParticles.length; j++) {
//				if (p == allParticles[j]) {
//					System.out.print(j + " ");
//				}
//			}
//		}
//		System.out.println("");
//		System.out.println("****");
	}
	
	public void setInitialBestPosition(Func function) {
		if (particles.size() > 0) {
			this.neighbourhoodBest = particles.get(0).getLocation();
			this.updateBestPosition(function);
		}
	}
	
	public void updateBestPosition(Func function) {
		for(int i=0; i< particles.size(); i++) {
			Position particleBest = particles.get(i).getPersonalBest();
			if (function.isFitter(particleBest, this.neighbourhoodBest)) {
				this.neighbourhoodBest = particleBest;
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
	
	public void setNeighbourhoodBest(Position neighbourhoodBest) {
		this.neighbourhoodBest = neighbourhoodBest;
	}
	
	public void addToNeighbourhood(ArrayList<Particle> newParticles) {
		this.particles.addAll(newParticles);
	}
}
