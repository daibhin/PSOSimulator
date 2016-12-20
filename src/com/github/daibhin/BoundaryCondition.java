package com.github.daibhin;

import com.github.daibhin.Functions.Func;

public interface BoundaryCondition {
	public void handleParticle(Particle particle, Func function);
}
