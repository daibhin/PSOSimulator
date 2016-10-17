package com.github.daibhin;

public class Simulator {
	
	public static void main(String[] args) {
		Function sphere = new SphereFunction();
//		Function rosenbrock = new RosenbrockFunction();
//		Function ackley = new AckleyFunction();
//		Function schaffer2D = new Schaffer2DFunction();
		PSO algorithm = new GlobalPSO(sphere);
//		PSO algorithm = new GlobalPSO(rosenbrock);
//		PSO algorithm = new GlobalPSO(ackley);
//		PSO algorithm = new GlobalPSO(schaffer2D);
		System.out.println(algorithm.run());
	}

}
