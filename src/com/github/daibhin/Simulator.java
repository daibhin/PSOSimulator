package com.github.daibhin;

import com.github.daibhin.Functions.*;

public class Simulator {
	
	public static void main(String[] args) {
		Function sphere = new SphereFunction();
		PSO algorithm = new GlobalPSO(sphere);
		System.out.println(sphere.evaluate(algorithm.run()));
		
//		Function shiftedSphere = new ShiftedSphereFunction();
//		PSO algorithm = new GlobalPSO(shiftedSphere);
		
//		Function rosenbrock = new RosenbrockFunction();
//		PSO algorithm = new GlobalPSO(rosenbrock);
		
//		Function ackley = new AckleyFunction();
//		PSO algorithm = new GlobalPSO(ackley);
		
//		Function schaffer2D = new Schaffer2DFunction();
//		PSO algorithm = new GlobalPSO(schaffer2D);

//		Function shiftedSchwefel = new ShiftedSchwefelFunction();
//		PSO algorithm = new GlobalPSO(shiftedSchwefel);
		
//		double sum = 0;
//		for(int i=0; i < 25; i++) {
//			PSO algorithm = new GlobalPSO(sphere);
//			sum += sphere.evaluate(algorithm.run());
//		}
//		System.out.println(sum/25);
	}

}
