package com.github.daibhin;

import com.github.daibhin.Functions.Sphere;
import com.panayotis.gnuplot.plot.Graph;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.*;

/**
 * Created by David on 31/08/2017.
 */
public class GraphUtilitiesTest {

    private static int SWARM_SIZE = 5;

    @Test
    public void testAveragePathLengthWithNoInfinitePaths() throws Exception {
        Particle[] particles = initializeSwarm();
        setupStructuredNeighbourhoods(particles);
        double[] pathLength = GraphUtilities.averagePathLength(particles, particles.length);
        assertEquals(pathLength[0], 1.5);
        assertEquals(pathLength[1], 0.0);
    }

    @Test
    public void testAveragePathLengthWithInfinitePaths() throws Exception {
        Particle[] particles = initializeSwarm();
        setupRandomNeighbourhoods(particles);
        double[] pathLength = GraphUtilities.averagePathLength(particles, particles.length);
        assertEquals(pathLength[0], 0.9);
        assertEquals(pathLength[1], 1.2);
    }

    private Particle[] initializeSwarm() {
        Particle[] particles = new Particle[SWARM_SIZE];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
        }
        return particles;
    }

    private void setupStructuredNeighbourhoods(Particle[] particles) {
        int swarmSize = particles.length;
        for (int index = 0; index < swarmSize; index++) {
            ArrayList<Particle> neighbourhoodParticles = new ArrayList<Particle>(); // ring topology
            if (index == 0) { // first particle
                neighbourhoodParticles.add(particles[swarmSize - 1]); // last
                neighbourhoodParticles.add(particles[1]); // second
            } else if (index == (swarmSize - 1)) { // last particle
                neighbourhoodParticles.add(particles[swarmSize - 2]); // second last
                neighbourhoodParticles.add(particles[0]); // first
            } else { // other particles
                neighbourhoodParticles.add(particles[index + 1]); // before
                neighbourhoodParticles.add(particles[index - 1]); //after
            }

            Particle particle = particles[index];

            particle.setNeighbourhood(neighbourhoodParticles, new Sphere(30, 0));
        }
    }

    private void setupRandomNeighbourhoods(Particle[] particles) {
        int[][] connectionArray = {{1,2}, {0,2}, {0,1}, {0,4}, {1,3}};
        int swarmSize = particles.length;
        for (int index = 0; index < swarmSize; index++) {
            Particle particle = particles[index];
            ArrayList<Particle> neighbourhoodParticles = new ArrayList<>();

            int[] particleIndicesToAdd = connectionArray[index];
            ArrayList<Particle> copiedParticles = new ArrayList<>(Arrays.asList(particles));

            for (int i = 0; i < particleIndicesToAdd.length; i++) {
                neighbourhoodParticles.add(copiedParticles.get(particleIndicesToAdd[i]));
            }
            particle.setNeighbourhood(neighbourhoodParticles, new Sphere(30, 0));
        }
    }
}