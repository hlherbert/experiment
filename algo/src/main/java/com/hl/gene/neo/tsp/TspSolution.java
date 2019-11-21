package com.hl.gene.neo.tsp;

public class TspSolution {
    public int[] path;//solution path
    public double pathcost;//path cost of this solution
    public long millisecond; //time spent on solving this problem
    public int transition; //number of state transition on solving this problem

    public TspSolution() {
        path = null;
        pathcost = -1;
        millisecond = -1;
        transition = -1;
    }
}
