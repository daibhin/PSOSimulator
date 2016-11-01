package com.github.daibhin;

import com.github.daibhin.Functions.Function;

public class Neighbourhood {
	private Particle[] particles;
	private Position neighbourhoodBest;
	
	
	public Neighbourhood(Particle[] particles, Function function, Particle[] allParticles) {
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
	
	public void setInitialBestPosition(Function function) {
		this.neighbourhoodBest = particles[0].getLocation();
		this.updateBestPosition(function);
	}
	
	public void updateBestPosition(Function function) {
		for(int i=0; i< particles.length; i++) {
			Position particleBest = particles[i].getPersonalBest();
			if (function.isFitter(particleBest, this.neighbourhoodBest)) {
				this.neighbourhoodBest = particleBest;
			}
		}
	}
	
	public Particle[] getParticles() {
		return particles;
	}
	
	public void setParticles(Particle[] particles) {
		this.particles = particles;
	}
	
	public Position getNeighbourhoodBest() {
		return neighbourhoodBest;
	}
	
	public void setNeighbourhoodBest(Position neighbourhoodBest) {
		this.neighbourhoodBest = neighbourhoodBest;
	}
}
