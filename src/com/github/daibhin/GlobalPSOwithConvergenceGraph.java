package com.github.daibhin;

import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.github.daibhin.Functions.Function;

public class GlobalPSOwithConvergenceGraph {
	
	private int PARTICLE_COUNT = 50;
	private int DIMENSIONS = 30;
	private int MAX_ITERATIONS = 10000;
	private double CONSTRICTION_FACTOR = 0.72984;
	private double C_1 = 2.05;
	private double C_2 = 2.05;
	private Random generator;
	
	private Position globalBest;
	private Function function;
	private Particle[] particles;
	
	private XYSeries series;

	public GlobalPSOwithConvergenceGraph(Function function) {
		this.function = function;
		this.generator = new Random();
		this.series = new XYSeries("Convergence");
		initializeSwarm();
	}

	public Position run() {
		
		int iteration = 0;
		while (iteration < MAX_ITERATIONS) {

			for (int index=0; index < PARTICLE_COUNT; index++) {
				Particle particle = particles[index];
				
				// EVALUATE CONVERGENCE //
				if (function.evaluate(particle.getLocation()) == function.getOptimum()) {
					System.out.println("Solution found at iteration " + (iteration));
					System.out.println("Position: " + this.globalBest);
					return particle.getLocation();
				}
				
				// GENERATE & UPDATE PARTICLE VELOCITY //
				double[] newVelocity = new double[DIMENSIONS];
				for(int vel=0; vel < DIMENSIONS; vel++) {
					double r1 = this.generator.nextDouble();
					double r2 = this.generator.nextDouble();
					double personalDifference = particle.getPersonalBest().getValues()[vel] - particle.getLocation().getValues()[vel];
					double personalContribution = (C_1 * r1) * personalDifference;
					double globalDifference = this.globalBest.getValues()[vel] - particle.getLocation().getValues()[vel];
					double globalContribution = (C_2 * r2) * globalDifference;
					double currentVelocity = particle.getVelocity()[vel];
					double updatedVel = CONSTRICTION_FACTOR*(currentVelocity + personalContribution + globalContribution);
					newVelocity[vel] = updatedVel;
				}
				particle.setVelocity(newVelocity);
				
				// UDPATE PARTICLE POSITION //
				double[] newLocation = new double[DIMENSIONS];
				for(int dim=0; dim < DIMENSIONS; dim++) {
					newLocation[dim] = particle.getLocation().getValues()[dim] + newVelocity[dim];
				}
				Position newPosition = new Position(newLocation);
				particle.setLocation(newPosition);

				//invisible boundary
				if (particle.withinBounds(function)) {
					// UPDATE GLOBAL BEST
					if(iteration == 0 || function.isFitter(particle.getLocation(), this.globalBest)) {
						this.globalBest = particle.getLocation();
					}
	
					// UPDATE PERSONAL BEST
					if(function.isFitter(particle.getLocation(), particle.getPersonalBest())) {
						particle.setPersonalBest(particle.getLocation());
					}
				}
			}

			iteration++;
			this.series.add(iteration, function.evaluate(this.globalBest));
			if (iteration % 10 == 0) {
				System.out.println("Iteration: " + iteration + " / Fitness: " + function.evaluate(this.globalBest));
			}
		}
		
		System.out.println("Solution found at iteration: " + iteration + " / Final fitness: " + function.evaluate(this.globalBest));
//		System.out.println("Position: " + this.globalBest);
		
//		plotConvergence(this.series);
		
		return this.globalBest;
	}

	private void initializeSwarm() {
		this.particles = new Particle[PARTICLE_COUNT];
		Particle particle;
		for (int i=0; i < PARTICLE_COUNT; i++) {
			particle = new Particle();
			particle.setVelocity(generateRandomlyInitialisedArray(DIMENSIONS, 1));
			Position position = new Position(generateRandomlyInitialisedArray(DIMENSIONS, 5));
			particle.setLocation(position);
			particles[i] = particle;
			
			particle.setPersonalBest(particle.getLocation());
			
			if(i == 0 || function.isFitter(particle.getLocation(), this.globalBest)) {
				this.globalBest = particle.getLocation();
			}
		}
	}
	
	private double[] generateRandomlyInitialisedArray(int dimensions, double amount) {
		double[] values = new double[dimensions];
		for(int i=0; i < dimensions; i++) {
			values[i] = this.generator.nextDouble()*amount;
		}
		return values;
	}
	
	private void plotConvergence(XYSeries series) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Convergence", "Iteration", "Fitness", dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		JFrame frame = new JFrame("Convergence Chart");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}