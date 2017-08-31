package com.github.daibhin;

import java.util.*;

/**
 * Created by David on 28/01/2017.
 */
public class GraphUtilities {

    public static int[][] constructGraph(Particle[] particles, int SWARM_SIZE) {
        int[][] graph = new int[SWARM_SIZE][SWARM_SIZE];
        for(int i=0; i<graph.length; i++) {
            for(int j=0; j<graph[i].length; j++) {
                // Set unconnected path lengths to be one greater than the number of path lengths
                // acts as infinity without potential overflow & always true because of equal weights on edges (of 1)
                // thus actual cost value can never be this high in worst case (ring connected topology)
                graph[i][j] = SWARM_SIZE + 1;
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

    public static int[][] floydWarshall(int[][] graph, int SWARM_SIZE) {
        int[][] cost = new int[graph.length][graph.length];
        for (int i=0; i < SWARM_SIZE; i++) {
            for (int j=0; j < SWARM_SIZE; j++) {
                if (i == j) {
                    cost[i][j] = 0;
                } else {
                    cost[i][j] = graph[i][j];
                }
            }
        }

        for (int k=0; k < SWARM_SIZE; k++) {
            for (int i=0; i < SWARM_SIZE; i++) {
                for (int j=0; j < SWARM_SIZE; j++) {
                    int sum = cost[i][k] + cost[k][j];
                    if (sum < cost[i][j]) {
                        cost[i][j] = sum;
                    }
                }
            }
        }

        int sum = 0;
        for (int i=0; i < SWARM_SIZE; i++) {
            for (int j=0; j < SWARM_SIZE; j++) {
                sum += ((double) cost[i][j])/SWARM_SIZE-1;
            }
        }
        return cost;
    }


    public static double[] averagePathLength(Particle[] particles, int SWARM_SIZE) {
        int[][] graph = constructGraph(particles, SWARM_SIZE);
        int[][] cost = floydWarshall(graph, SWARM_SIZE);
        double sum = 0;
        double infiniteSum = 0;
        for (int i=0; i < SWARM_SIZE; i++) {
            for (int j=0; j < SWARM_SIZE; j++) {
                int distance = cost[i][j];
                if (distance == SWARM_SIZE + 1) {
                    infiniteSum++;
                } else {
                    sum += distance;
                }
            }
        }
        return new double[] {((double) 1/(SWARM_SIZE*(SWARM_SIZE-1))) * sum, infiniteSum/SWARM_SIZE};
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
