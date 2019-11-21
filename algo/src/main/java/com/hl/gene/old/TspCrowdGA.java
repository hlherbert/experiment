package com.hl.gene.old;

import java.util.ArrayList;

/**
 * Artificial Intelligence
 * Project 5
 * Li Huang
 * Gene Algorithm with Wisdom of Crowd
 *
 * @author hl
 * <p>
 * (1)Use GA with different parameters, run many times, and get different solutions.
 * (2)Choose a certain percentage of  the fittest individuals in the population of
 * solutions.(let's call them experts)
 * (3)Combine these experts, (expected to) get a better solution.
 * The"Combine" method is similar to what used in [2]: Substitute Euclidian
 * distance by a new ��cost�� for each edge in the TSP map. Then solve this new TSP
 * with a "Greedy" heuristic, also with a 2-Opt improvement.
 * <p>
 * Reference:
 * [2] S. K. M. Yi, M. Steyvers, M. D. Lee, and M. J. Dry. (2010, Wisdom of
 * the Crowds in Traveling Salesman Problems.
 */
public class TspCrowdGA {

    //Output command buffer, for debug info
    private CmdPrinter m_cmdPrinter = null;
    private TspGA m_GASolver = null;
    private ArrayList<TspSolution> m_crowd;
    private ArrayList<TspSolution> m_experts;

    //Fixed parameters
    private int m_gaRun = 1;//How many time should GA run for each solution
    // The output solution should be the best from these runs
    private int m_numRun = 1;//5; // How many solutions should be generate for each
    //  GA parameter setting
    private int m_popSize = 500;
    private int m_maxGen = 10000;
    private int m_crossover_method = 1; //Single point crossover
    //Variable parameters
    private double m_crossoverProb_min = 0.4;
    private double m_crossoverProb_max = 0.8;
    private double m_crossoverProb_step = 0.1;
    private double m_mutationProb_min = 0.001;
    private double m_mutationProb_max = 0.010;
    private double m_mutationProb_step = 0.003; //0.001

    //Choose the best p% solutions as expert
    private double m_expertRatio = 0.5;

    public TspCrowdGA(CmdPrinter cmdPrinter) {
        m_cmdPrinter = cmdPrinter;
        m_GASolver = new TspGA(cmdPrinter);
        m_crowd = new ArrayList<TspSolution>();
        m_experts = new ArrayList<TspSolution>();

        m_GASolver.SetDebugVisible(false);
    }

    private void initCrowd() {
        m_crowd.clear();
        m_experts.clear();
    }


    /**
     * |!| Only for test
     *
     * @param data
     * @return
     */
    public TspSolution SolveTspCrowdGATest(TspData data) {
        int n = data.n;
        int[] path = new int[n + 1];
        greedyTsp(data.cost, n, path);
        // Use Opt-2 to improve the solution.
        // This Opt-2 can only do one time
        //   more times of Opt-2 improvement do not get better result.
        TspOpt2.ImproveOpt2(n, path, data.cost);

        // Then transform the path into a normal format(city index start from 1)
        for (int i = 0; i < n; i++)
            path[i] += 1;
        // Close a cycle: The last city is the first city.
        path[n] = path[0];
        TspSolution sol = new TspSolution();
        sol.path = path;
        sol.pathcost = Tool.PathCost(path, data.cost);
        return sol;
    }

    public TspSolution SolveTspCrowdGA(TspData data) {
        long startTime = System.currentTimeMillis();


        initCrowd();

        //*(1)Use GA with different parameters, run many times, and get different solutions.
        printCmd("--- (1)  Generate Crowd --------\n");
        m_GASolver.ReadProblem(data);
        TspSolution gaSolution = null;
        TspSolution crowdSolution = null;

        int p = 0;
        for (double crossProb = m_crossoverProb_min;
             crossProb <= m_crossoverProb_max;
             crossProb += m_crossoverProb_step) {
            for (double mutateProb = m_mutationProb_min;
                 mutateProb <= m_mutationProb_max;
                 mutateProb += m_mutationProb_step) {
                m_GASolver.SetParameter(m_gaRun, m_popSize, m_maxGen, m_crossover_method,
                        crossProb, mutateProb);
                for (int iRun = 0; iRun < m_numRun; iRun++) {
                    p++;
                    String cmd = String.format("Gen Crowd %d: crossProb=%f,mutateProb=%f\n",
                            p, crossProb, mutateProb);
                    printCmd(cmd);

                    gaSolution = m_GASolver.SolveProblem();
                    //Add it to crowd
                    m_crowd.add(gaSolution);
                }
            }
        }

        // *(2)Choose a certain percentage of  the fittest individuals in the population of
        // *  solutions.(let's call them experts)
        //solution.pathcost;
        printCmd("-------- (2) Select Expert  --------------\n");
        selectExpert(m_crowd, m_experts, m_expertRatio);

        //*(3)Combine these experts, (expected to) get a better solution.
        // *  The"Combine" method is similar to what used in [2]: Substitute Euclidian
        // *  distance by a new ��cost�� for each edge in the TSP map. Then solve this new TSP
        // *  with a "Greedy" heuristic, which was used in Project 3.
        printCmd("-------- (3) Combine Solutions  --------------\n");
        crowdSolution = combineSolutions(m_experts, data.cost);

        long endTime = System.currentTimeMillis();
        crowdSolution.millisecond = endTime - startTime;


        TspIO tspIO = new TspIO();
        tspIO.SetToConsoleMode();
        tspIO.OutputTSP(crowdSolution);


        //-------- Debug --------
        // Show avg,best performance of crowd
        printCrowdPerformance();

        return crowdSolution;
    }

    // Select expertRaio  best solutions from crowd into experts
    // (0<expertRatio<=1)
    private void selectExpert(ArrayList<TspSolution> crowd,
                              ArrayList<TspSolution> experts,
                              double expertRatio) {
        int n = crowd.size();
        int m = (int) (expertRatio * n); //how many experts to select
        //record if the k-th solution has been selected as experts
        boolean[] selected = new boolean[n];
        for (int i = 0; i < n; i++) {
            selected[i] = false;
        }

        experts.clear();
        //Time = O(p*n*n)
        for (int i = 0; i < m; i++) {
            // Find the first unselected solution
            int k = 0;
            while (selected[k] && k < n) {
                k++;
            }

            // Find the best unselected solution , crowd[k]
            for (int j = k + 1; j < n; j++) {
                if (crowd.get(j).pathcost < crowd.get(k).pathcost
                        && !selected[j]) {
                    k = j;
                }
            }

            // Add this solution to experts
            experts.add(crowd.get(k));
            selected[k] = true;
        }
        //finished
    }


    /**
     * Combine the solutions from the crowd
     * And get a (expecting) better solution.
     * experts - all the experts populations
     * cost_city_dist - cost matrix of city distance
     *
     * @param crowd
     */
    TspSolution combineSolutions(ArrayList<TspSolution> experts,
                                 double[][] cost_city_dist) {
        // The ��Combine�� method is similar to what used in [2]: Substitute Euclidian
        // distance by a new ��cost�� for each edge in the TSP map. Then solve this
        // new TSP with a ��Greedy�� heuristic and an 2-opt improvement later.
        //
        //The ��cost�� for each edge in the ��new TSP�� is a function of agreement
        //proportions of this edge. The basic idea is ��The good local connections
        //between cities will tend to be selected by more individuals than those
        //connections which are part of bad solutions.�� Therefore, if an edge occurs
        //in most of the expert solutions, the ��cost�� for this edge should be low;
        //If an edge occurs rarely in the expert solutions, the ��cost�� for this edge
        //should be high.
        //
        //The ��new cost��, cij, for each edge,(i,j), in the ��Combination Experts�� step:
        //	c_ij=1-a_ij
        //	aij is the proportion of agreements of this edge. For example,
        //  if the crowd have totally 10 solutions, and 3 of them contains the
        //  edge (i,j), then aij =3/10=0.3
        if (experts == null)
            return null;
        if (experts.size() == 0)
            return null;

        // Calulate new cost for new tsp
        int n = experts.get(0).path.length - 1;// number of cities
        int num_experts = experts.size(); // number of experts
        double sum_edges = n * num_experts; // total number of edges
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                c[i][j] = 1;
        for (int i = 0; i < num_experts; i++) {
            int[] path = experts.get(i).path;
            for (int j = 0; j < n; j++) {
                int x = path[j];// city 1
                int y = path[j + 1];// city 2
                c[x - 1][y - 1] = c[x - 1][y - 1] - 1.0 / sum_edges; // cost matrix start from 0
                c[y - 1][x - 1] = c[x - 1][y - 1]; // but city index start from 1
            }
        }


        int[] path = new int[n + 1];
        greedyTsp(c, n, path);

        // Use Opt-2 to improve the solution.
        TspOpt2.ImproveOpt2(n, path, c);

        // Then transform the path into a normal format(city index start from 1)
        for (int i = 0; i < n; i++)
            path[i] += 1;
        // Close a cycle: The last city is the first city.
        path[n] = path[0];
        TspSolution sol = new TspSolution();
        sol.path = path;
        sol.pathcost = Tool.PathCost(path, cost_city_dist);
        return sol;
    }//end function


    //solve tsp using a Greedy algorithm
    // Input: cost matrix
    //        n- num of cities
    // Output: tour
    //        tour must be predifined that has n+1 elements
    //		  The output tour is start from 0.
    //		  If want to output to solution, please transform the tour to index 1
    //		  ,and close it. (tour[n]=tour[0])
    private void greedyTsp(double[][] cost, int n, int[] tour) {
        // Let's make the city index start from 0
        int startCity = 0;
        tour[0] = startCity;
        boolean[] selected = new boolean[n];//record whether the city has been selected
        for (int i = 0; i < n; i++) {
            selected[i] = false;
        }
        selected[startCity] = true;

        for (int i = 1; i < n; i++) {
            int cur_city = tour[i - 1];
            // Find the first unselected city
            int k = 0;
            while (selected[k] && k < n) {
                k++;
            }

            // Find the best unselected city , which is nearest to current city
            for (int j = k + 1; j < n; j++) {
                if (cost[cur_city][j] < cost[cur_city][k]
                        && !selected[j]) {
                    k = j;
                }
            }

            // Set city k as next city
            tour[i] = k;
            selected[k] = true;
        }
    }

    // Show avg,best performance of crowd
    private void printCrowdPerformance() {
        double sum = 0;
        double best = Double.MAX_VALUE;// a big number
        double sum_time = 0;
        int n = m_crowd.size();
        for (int i = 0; i < n; i++) {
            TspSolution s = m_crowd.get(i);
            sum += s.pathcost;
            if (s.pathcost < best) {
                best = s.pathcost;
            }
            sum_time += s.millisecond;
        }
        double avg = sum / n;
        double avg_time = sum_time / n;

        printCmd("--Crowd Performance--\n");
        String cmd = String.format("  avg pathcost=%f\n  best pathcost=%f\n  avg_time=%f\n",
                avg, best, avg_time);
        printCmd(cmd);
    }

    private void printCmd(String s) {
        if (m_cmdPrinter != null)
            m_cmdPrinter.PrintCmd(s);
    }

}// end class
