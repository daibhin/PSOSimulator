package com.github.daibhin;

import com.github.daibhin.Functions.Function;

public interface BoundaryCondition {
	public void handleParticle(Particle particle, Function function);
}
