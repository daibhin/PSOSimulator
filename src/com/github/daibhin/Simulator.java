package com.github.daibhin;

import java.util.Random;

import com.github.daibhin.Functions.*;

public class Simulator {
	
	public static void main(String[] args) {
		int DIMENSIONS = 30;
		
//		BoundaryCondition boundary = new InvisibleBoundary();
//		Function function = new SphereFunction();
//		PSO algorithm = new GlobalPSO(function, boundary);
//		
//		Func function1 = new Benchmark().testFunctionFactory(0, 30);
		
		
		System.out.println(Benchmark.round(-5.65));
		
//		PSO algorithm = new SPSO(function, boundary);
//		Position soln = new Position(((ShiftedRosenbrockFunction) function).globalOptimum);
//		System.out.println(soln);
//		System.out.println(function1.evaluate(algorithm.run()));
		
//		double sum = 0;
//		int loopCount = 25;
//		for(int i=0; i < loopCount; i++) {
//			PSO algorithm = new GlobalPSO(sphere, boundary);
//			sum += sphere.evaluate(algorithm.run());
//			System.out.println("Loop:" + i + " / Average: " + (sum/(i+1)));
//		}
//		System.out.println("FINAL: " + sum/loopCount);
	}

}
