package com.github.daibhin;

import org.jfree.chart.plot.XYPlot;
import com.github.daibhin.Functions.*;

public class Simulator {
	
	public static void main(String[] args) {
		BoundaryCondition boundary = new InvisibleBoundary();
		
		Function sphere = new SphereFunction();
//		PSO algorithm = new SPSO(sphere);
		PSO algorithm = new GlobalPSO(sphere, boundary);
		System.out.println(sphere.evaluate(algorithm.run()));


//		Function shiftedSphere = new ShiftedSphereFunction();
//		PSO algorithm = new GlobalPSO(shiftedSphere, sphere, boundary);
		
//		Function rosenbrock = new RosenbrockFunction();
//		PSO algorithm = new GlobalPSO(rosenbrock, boundary);
//        System.out.println(rosenbrock.evaluate(algorithm.run()));
		
//		Function ackley = new AckleyFunction();
//		PSO algorithm = new GlobalPSO(ackley, boundary);
//        System.out.println(ackley.evaluate(algorithm.run()));
		
//		Function schaffer2D = new Schaffer2DFunction();
//		PSO algorithm = new GlobalPSO(schaffer2D, boundary);
//        System.out.println(schaffer2D.evaluate(algorithm.run()));

//		Function shiftedSchwefel = new ShiftedSchwefelFunction();
//		PSO algorithm = new GlobalPSO(shiftedSchwefel, boundary);
		
//		double sum = 0;
//		int loopCount = 1;
//		for(int i=0; i < loopCount; i++) {
//			PSO algorithm = new GlobalPSO(sphere, boundary);
//			sum += sphere.evaluate(algorithm.run());
//			System.out.println("Loop:" + i + " / Average: " + (sum/(i+1)));
//		}
//		System.out.println(sum/loopCount);
	}

}
