package com.github.daibhin;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by David on 28/01/2017.
 */
public class GraphUtilities {

    public static double averagePathLength(int SWARM_SIZE, Particle[] particles) {
        int[][] graph = GraphUtilities.constructGraph(SWARM_SIZE, particles);
        double sum = GraphUtilities.floydWarshall(graph, SWARM_SIZE);
        return ((double) 1/(SWARM_SIZE*(SWARM_SIZE-1))) * sum;
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

    public static double floydWarshall(int[][] graph, int SWARM_SIZE) {
        int sum = 0;
        int dist[][] = new int[SWARM_SIZE][SWARM_SIZE];
        int i, j, k;
        for (i = 0; i < SWARM_SIZE; i++) {
            for (j = 0; j < SWARM_SIZE; j++) {
//                sum += graph[i][j];
                dist[i][j] = graph[i][j];
            }
        }

        for (k = 0; k < SWARM_SIZE; k++) {
            for (i = 0; i < SWARM_SIZE; i++) {
                for (j = 0; j < SWARM_SIZE; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
//                        sum -= dist[i][j];
                        dist[i][j] = dist[i][k] + dist[k][j];
//                        sum += dist[i][j];
                    }
                }
            }
        }

        int otherSum = 0;
        for (int p = 0; p < SWARM_SIZE; p++) {
            for (int o = 0; o < SWARM_SIZE; o++) {
                if (p != o) {
                    if (dist[p][o] != Integer.MAX_VALUE && dist[p][o] != Integer.MAX_VALUE) {
                        otherSum += dist[p][o];
                    }
                }
            }
        }

        return otherSum;
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
