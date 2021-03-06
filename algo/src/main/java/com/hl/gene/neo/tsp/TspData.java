package com.hl.gene.neo.tsp;

/**
 * MODULE 2
 * Data structure of TSP input data
 *
 * @author hl
 */
public class TspData {
    //*********  Input data ***************/
    public int n; //number of cities
    public double[] x;//x coords of each city, x[i-1] is x coord of city i
    public double[] y;//y coords of each city, y[i-1] is y coord of city i
    public double[][] cost; //cost of each city pair. cost[i-1][j-1] means cost from
    // city i to city j
}


