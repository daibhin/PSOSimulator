package com.github.daibhin;

import java.util.*;

/**
 * Created by David on 28/01/2017.
 */
public class GraphUtilities {

    public static double[] averagePathLength(int SWARM_SIZE, Particle[] particles) {
        DirectedGraph<Particle> newGraph = createGraph(particles, SWARM_SIZE);
        int sum = 0;
        double infiniteSum = 0;
        for(int i=0; i<SWARM_SIZE; i++) {
            Particle particle = particles[i];
            Map<Particle, Double> distances = Dijkstra.shortestPaths(newGraph, particle);
            for(Double d : distances.values()) {
                if((d != 0.0) && (!d.isInfinite())) {
                    sum += d;
                }
                else {
                    if (d.isInfinite()) {
                        infiniteSum++;
                    }
                }
            }
        }
        return new double[] {((double) 1/(SWARM_SIZE*(SWARM_SIZE-1))) * sum, infiniteSum/SWARM_SIZE};
    }

    public static DirectedGraph<Particle> createGraph(Particle[] particles, int SWARM_SIZE) {
        DirectedGraph<Particle> graph = new DirectedGraph<Particle>();
        for(int i=0; i<SWARM_SIZE; i++) {
            graph.addNode(particles[i]);
        }
        for(int i=0; i<SWARM_SIZE; i++) {
            Particle particle = particles[i];
            ArrayList<Particle> neighbours = particle.getNeighbourhood().getParticles();
            for(Particle neighbour : neighbours) {
                graph.addEdge(particle, neighbour, 1);
            }
        }
        return graph;
    }

    public static int[][] constructGraph(int SWARM_SIZE, Particle[] particles) {
        int[][] graph = new int[SWARM_SIZE][SWARM_SIZE];
        for(int i=0; i<graph.length; i++) {
            for(int j=0; j<graph[i].length; j++) {
                graph[i][j] = Integer.MAX_VALUE;
            }
        }

        for(int u=0; u<SWARM_SIZE; u++) {
            for(int v=0; v<SWARM_SIZE; v++) {
                if(u == v) {
                    graph[u][v] = 0;
                } else {
                    Particle p = particles[u];
                    Particle o = particles[v];
                    if (p.getNeighbourhood().getParticles().contains(o)) {
                        graph[u][v] = 1;
                    }
                }
            }
        }

        return graph;
    }

    public static double clusteringCoefficient(int SWARM_SIZE, Particle[] particles) {
        double sum = 0;
        for (int i = 0; i < SWARM_SIZE; ++i) {
            Particle particle = particles[i];
            ArrayList<Particle> particlesNeighbours = (ArrayList<Particle>) particle.getNeighbourhood().getParticles().clone();
            particlesNeighbours.remove(particle);

            double count = 0;
            for(Particle neighbour : particlesNeighbours) {
                ArrayList<Particle> neighboursNeighbours = (ArrayList<Particle>) neighbour.getNeighbourhood().getParticles().clone();
                neighboursNeighbours.remove(neighbour);
                for(Particle neighboursNeighbour : neighboursNeighbours) {
                    // increment if a particles neighbour has one of the same neighbours as it
                    if(particlesNeighbours.contains(neighboursNeighbour)) {
                        count++;
                    }
                }
            }
            sum += (count/(particlesNeighbours.size() * (particlesNeighbours.size() - 1.0)));
        }
        return sum/SWARM_SIZE;
    }
}
