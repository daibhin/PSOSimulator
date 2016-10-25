package com.github.daibhin;

import com.github.daibhin.Functions.Function;

public class ReflectingBoundary implements BoundaryCondition {

	@Override
	public void handleParticle(Particle particle, Function function) {
		double[] positionValues = particle.getLocation().getValues();
		double[] correctedPositionValues = new double[positionValues.length];
		
		double[] velocityValues = particle.getVelocity();
		double[] correctedVelocityValues = new double[velocityValues.length];
		
		for(int j=0; j < positionValues.length; j++) {
			if (positionValues[j] < function.getLowerBound()) {
				correctedPositionValues[j] = function.getLowerBound();
				correctedVelocityValues[j] = -1 * velocityValues[j];
			} else if (positionValues[j] > function.getUpperBound()) {
				correctedPositionValues[j] = function.getUpperBound();
				correctedVelocityValues[j] = -1 * velocityValues[j];
			} else {
				correctedPositionValues[j] = positionValues[j];
				correctedVelocityValues[j] = velocityValues[j];
			}
		}
		
		particle.setLocation(new Position(correctedPositionValues));
		particle.setVelocity(correctedVelocityValues);
	}

}
