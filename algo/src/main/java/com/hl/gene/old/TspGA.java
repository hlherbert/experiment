package com.hl.gene.old;

import java.util.Random;


/**
 * TSP--Gene Algorithm
 * GA Model 1, permulating encoding
 *
 * @author hl
 */
public class TspGA {

    //Mutate probability CLASS:0.001~0.1
    final double MUTATE_PROB1 = 0.1;//0.001;
    final double MUTATE_PROB2 = 0.01;
    final int DEBUG_GENERATION_STEP = 1000;//For every x generations,
    int NUM_RUN = 1;//5 //how many runs for each experiment
    int POPULATION_SIZE = 500;// The population size of a generation
    //stores a parent and a child generation
    Chromosome[][] m_population = null;//new Chromosome[2][POPULATION_SIZE];
    //Max generation for each run
    int MAX_GENERATION_PER_RUN = 150000;//10000/POPULATION_SIZE*10000;//150000; //10000;
    //Crossover Method
    int CROSSOVER_METHOD = 1; //1-single point crossover, 2-double points crossover
    //Crossover probability WIKI:0.6~1
    double CROSSOVER_PROB = 0.6;//0.7;0.007
    double MUTATE_PROB = MUTATE_PROB1;
    // the debug info should be print out
    boolean SHOW_DEBUG = true; // whether show debug info
    //Random generator
    private Random m_rand = null;//new Random();
    //Output command buffer, for debug info
    private CmdPrinter m_cmdPrinter = null;
    private Problem m_problem;

    public TspGA(CmdPrinter cmdPrinter) {
        m_rand = new Random();
        m_cmdPrinter = cmdPrinter;
        m_population = new Chromosome[2][POPULATION_SIZE];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < POPULATION_SIZE; j++) {
                // length of chromosome unkown
                // allocated sapce in SolveTspGA()
                m_population[i][j] = null;//new Chromosome();
            }
        m_problem = new Problem();
    }

    public void SetParameter(
            int num_run,
            int pop_size,
            int max_gen,
            int crossover_method,
            double crossover_prob, double mutate_prob) {
        NUM_RUN = num_run;
        POPULATION_SIZE = pop_size;
        MAX_GENERATION_PER_RUN = max_gen;
        CROSSOVER_METHOD = crossover_method;
        CROSSOVER_PROB = crossover_prob;
        MUTATE_PROB = mutate_prob;
    }

    private void printCmd(String s) {
        if (m_cmdPrinter != null)
            m_cmdPrinter.PrintCmd(s);
    }

    double MIN(double A, double B) {
        return (A > B ? A : B);
    }

    int MIN(int A, int B) {
        return (A > B ? A : B);
    }

    void RANDINIT() {
        m_rand.setSeed(System.currentTimeMillis());
    }

    //generate a random double [0~1]
    double RANDOM() {
        return m_rand.nextDouble();
        //return rand()/(double)RAND_MAX;
    }

    // int random in [0..i)
    int RANDMAX(int i) {
        if (i <= 0)
            return 0;
        return m_rand.nextInt(i);
        //return rand()%i;
    }

    double RANDMAX(double i) {
        if (i <= 0)
            return 0;
        return m_rand.nextDouble() * i;
    }

    // Crossover Method
    // from two parent chromosomes in parent population,
    // generate two children chromosomes in child population.
    void Crossover(int crossoverMethod,
                   int parent_pop, int parent_1, int parent_2,
                   int child_pop, int child_1, int child_2) {
        if (crossoverMethod == 1) {
            Crossover1(parent_pop, parent_1, parent_2, child_pop, child_1, child_2);
        } else if (crossoverMethod == 2) {
            Crossover2(parent_pop, parent_1, parent_2, child_pop, child_1, child_2);
        }
    }

    // Return if a gene in a range [start,end] of chromosome c.
    boolean isGeneInChromosome(int gene, Chromosome c, int start, int end) {
//		if ( start<0)
//			start=0;
//		if (end>=c.gene.length)
//			end=c.gene.length-1;
        for (int i = start; i <= end; i++) {
            if (gene == c.gene[i])
                return true;
        }
        return false;
    }

    // Single Point Crossover (permutation encoding)
    void Crossover1(
            int parent_pop, int parent_1, int parent_2,
            int child_pop, int child_1, int child_2) {
//		Because we use the Model1and its gene is permutation encoding (Each gene
//			is unique in a chromosome), the single point crossover changes to:
//		Single point crossover - one crossover point is selected, till this point
//		the permutation is copied from the first parent, then the second parent
//		is scanned and if the number is not yet in the offspring it is added
//		E.g.
//		([1 2 3 4 5] 6 7 8 9) + ([4 5 3 6 8] 9 7 2 1) =>
//		   ([1 2 3 4 5] 6 8 9 7) + ([4 5 3 6 8] 1 2 7 9)
        int crossoverPnt = 0; //crossover point
        int num_gene = m_population[parent_pop][parent_1].gene.length;
        int j = 0;

        Chromosome p1 = m_population[parent_pop][parent_1];
        Chromosome p2 = m_population[parent_pop][parent_2];
        Chromosome c1 = m_population[child_pop][child_1];
        Chromosome c2 = m_population[child_pop][child_2];

        if (RANDOM() < CROSSOVER_PROB) {
            crossoverPnt = RANDMAX(num_gene);
        } else {
            for (j = 0; j < num_gene; j++) {
                c1.gene[j] = p1.gene[j];
                c2.gene[j] = p2.gene[j];
            }
            return;
        }

        //copy first part
        for (j = 0; j < crossoverPnt; j++) {
            c1.gene[j] = p1.gene[j];
            c2.gene[j] = p2.gene[j];
        }
        //2nd part is scanned and if the number is not yet in the offspring,
        // it is added.
        int k = crossoverPnt;
        for (j = 0; j < num_gene && k < num_gene; j++) {
            //if p2.gene[j] not in c1(0,k)
            if (!isGeneInChromosome(p2.gene[j], c1, 0, crossoverPnt - 1)) {
                c1.gene[k] = p2.gene[j];
                k++;
            }
        }
        k = crossoverPnt;
        for (j = 0; j < num_gene && k < num_gene; j++) {
            if (!isGeneInChromosome(p1.gene[j], c2, 0, crossoverPnt - 1)) {
                c2.gene[k] = p1.gene[j];
                k++;
            }
        }
    }

    // Double Point Crossover
    void Crossover2(
            int parent_pop, int parent_1, int parent_2,
            int child_pop, int child_1, int child_2) {
//		Double point crossover - two crossover points are selected,
//		the segment between this two points is copied from the first parent,
//		then the second parent is scanned and if the number is not yet in the
//		offspring it is added.
//		E.g.
//		(1 2 3 [4 5 6] 7 8 9) + (4 5 3 [6 8 9] 7 2 1)
//		=> (3 8 9 [4 5 6] 7 2 1) + (1 2 3 [6 8 9] 4 5 7)

        int crossoverPnt1 = 0, crossoverPnt2 = 0; //crossover points
        int num_gene = m_population[parent_pop][parent_1].gene.length;
        int j = 0;
        int k = 0;

        Chromosome p1 = m_population[parent_pop][parent_1];
        Chromosome p2 = m_population[parent_pop][parent_2];
        Chromosome c1 = m_population[child_pop][child_1];
        Chromosome c2 = m_population[child_pop][child_2];

        if (RANDOM() < CROSSOVER_PROB) {
            crossoverPnt1 = RANDMAX(num_gene);
            crossoverPnt2 = RANDMAX(num_gene);
            //make sure crossoverPnt2 >= crossoverPnt1
            if (crossoverPnt1 > crossoverPnt2) {
                int tmp = crossoverPnt2;
                crossoverPnt2 = crossoverPnt1;
                crossoverPnt1 = tmp;
            }
        } else {
            for (j = 0; j < num_gene; j++) {
                c1.gene[j] = p1.gene[j];
                c2.gene[j] = p2.gene[j];
            }
            return;
        }

        //copy first part
        for (j = crossoverPnt1; j <= crossoverPnt2; j++) {
            c1.gene[j] = p1.gene[j];
            c2.gene[j] = p2.gene[j];
        }

        //2nd part is scanned and if the number is not yet in the offspring,
        // it is added.
        k = 0;
        for (j = 0; j < num_gene && k < crossoverPnt1; j++) {
            //if p2.gene[j] not in c1(0,k)
            if (!isGeneInChromosome(p2.gene[j], c1, crossoverPnt1, crossoverPnt2)) {
                c1.gene[k] = p2.gene[j];
                k++;
            }
        }
        k = crossoverPnt2 + 1;
        for (; j < num_gene && k < num_gene; j++) {
            //if p2.gene[j] not in c1(0,k)
            if (!isGeneInChromosome(p2.gene[j], c1, crossoverPnt1, crossoverPnt2)) {
                c1.gene[k] = p2.gene[j];
                k++;
            }
        }
        k = 0;
        for (j = 0; j < num_gene && k < crossoverPnt1; j++) {
            if (!isGeneInChromosome(p1.gene[j], c2, crossoverPnt1, crossoverPnt2)) {
                c2.gene[k] = p1.gene[j];
                k++;
            }
        }
        k = crossoverPnt2 + 1;
        for (; j < num_gene && k < num_gene; j++) {
            if (!isGeneInChromosome(p1.gene[j], c2, crossoverPnt1, crossoverPnt2)) {
                c2.gene[k] = p1.gene[j];
                k++;
            }
        }
    }

    // Mutation Method
    // Randomly select two gene in the child chromosome,
    // and swap them.
    // pop: the population index of the member to mutate
    // member: the index of chromosome in the population to mutate
    void Mutate(double mutate_prob, int pop, int member) {
//		Order changing - two numbers are randomly selected and exchanged
//		E.g.  (1 [2] 3 4 5 6 [8] 9 7) => (1 [8] 3 4 5 6 [2] 9 7)

        int num_gene = m_problem.nCity;

        //---- Randomly select two gene to swap -------
        if (RANDOM() < mutate_prob) {
            //randomly select a gene
            int mutatePos1 = RANDMAX(num_gene);
            int mutatePos2 = RANDMAX(num_gene);

            //swap
            Chromosome c = m_population[pop][member];
            int tmp = c.gene[mutatePos1];
            c.gene[mutatePos1] = c.gene[mutatePos2];
            c.gene[mutatePos2] = tmp;
        }
    }

    // Generate a random n-permutation
    // the values are in range [0..n-1]
    // Output to lst, lst must be pre-allocated with size n
    void generateRandomPermutation(int[] lst, int n) {
        for (int i = 0; i < n; i++) {
            lst[i] = n; //assign n, to specify it is not occupied
        }

        // put 0,1,...n-1 into a random position,pos
        // If pos is occupied, then
        // find the next unoccupied position after pos.
        for (int i = 0; i < n; i++) {
            int pos = RANDMAX(n);
            while (lst[pos] != n)// pos is occupied
            {
                pos = (pos + 1) % n;
            }
            lst[pos] = i;
        }
    }

    // Init the population
    void initalize_population(int cur) {
        int i = 0;
        // Init the current population with random genes, which in range[0..n-1]
        int num_gene = m_problem.nCity;
        for (i = 0; i < POPULATION_SIZE; i++) {
            Chromosome c = m_population[cur][i];
            generateRandomPermutation(c.gene, num_gene);
        }
    }

    //Compute fitness of whole population
    void compute_population_fitness(int cur_pop,
                                    FitnessStat stat) {
        int i = 0;
        double fitness = 0;
        double mmin = Double.MAX_VALUE;
        double mmax = 0;
        double mavg = 0;
        double msum = 0;
        int mbestmember = 0;

        for (i = 0; i < POPULATION_SIZE; i++) {
            fitness = compute_fitness(cur_pop, i);
            m_population[cur_pop][i].fitness = fitness;
            if (fitness < mmin)
                mmin = fitness;
            if (fitness > mmax) {
                mmax = fitness;
                mbestmember = i;
            }
            msum += fitness;
        }
        mavg = msum / POPULATION_SIZE;

        stat.min = mmin;
        stat.max = mmax;
        stat.avg = mavg;
        stat.sum = msum;
        stat.best_member_ind = mbestmember;
        stat.best_member = m_population[cur_pop][mbestmember];
    }

    // show solution
    void trace_chromosome(int cur, int member) {
        Chromosome c = m_population[cur][member];
        print_chromosome(c);
    }

    void print_chromosome(Chromosome c) {
        int i = 0;
        for (i = 0; i < c.gene.length; i++) {
            String str = String.format("%d\t", c.gene[i]);
            printCmd(str);
        }
        printCmd("\n");
    }

    void print_travelPath(int[] t) {
        int i = 0;
        for (i = 0; i < t.length; i++) {
            String str = String.format("%d\t", t[i]);
            printCmd(str);
        }
        printCmd("\n");
    }

    // main loop for GA: a run of GA
    // Use current parameters
    // Generate MAX_GENERATION generations,
    // Return statistic information for this run
    FitnessStat_Run ga_run() {
        int cur = 0; //current population  0 or 1
        FitnessStat stat = new FitnessStat();//statistic for current generation
        FitnessStat_Run stat_run = new FitnessStat_Run();//statistic for whole run
        int generation = 0;

        RANDINIT();
        initalize_population(cur);
        compute_population_fitness(cur, stat);//first generation
        stat_run.AddStat(stat);

        while (generation < MAX_GENERATION_PER_RUN - 1) {
            if (SHOW_DEBUG && (generation % DEBUG_GENERATION_STEP) == 0) {
                String str = String.format("%6d: %g %g %g\n", generation, stat.min, stat.avg, stat.max);
                printCmd(str);
            }

            generation++;
            //printf("%d\n",generation);
            cur = perform_ga(cur, stat.sum, CROSSOVER_METHOD, MUTATE_PROB);
            compute_population_fitness(cur, stat);
            stat_run.AddStat(stat);
        }

        stat_run.DoStat();//Find the optimal sulution in current run.
        printCmd("-- Teminate at MAX_GENERATION= " + generation + " ---\n");
        printCmd("Best Chromosome: \n");
        print_chromosome(stat_run.best_member);
        printCmd("Best travel path: \n");
        print_travelPath(stat_run.best_travel_path);
        printCmd(String.format("Len=%f\n\n", stat_run.len_best_travel_path));

        return stat_run;
    }

    // The core of the GA:
    // select parents, generate a child generation
    // crossoverMethod: 1 or 2
    //   crossoverMethod=1-- single point crossover
    //   crossoverMethod=2-- double point crossover
    // mutateMethod: 1 or 2
    //   mutateMethod=1 -- use MUTATION_PROB1
    //   mutateMethod=2 -- use MUTATION_PROB2
    int perform_ga(int cur_pop, double sum,
                   int crossoverMethod, double mutate_prob) {
        int i, new_pop;
        int parent_1, parent_2;
        // We will keep the elist chromosome and clone it into the child generation
        // Elistism method can make GA a quick evolution.
        int elist = 0; //the elist(best) chromosome in cur_pop
        double elistFitness = 0; //the fitness of elist chromosome

        new_pop = (cur_pop == 0) ? 1 : 0;
        for (i = 0; i < POPULATION_SIZE; i += 2) {
            //i is cihld_1,i+1 is child_2
            parent_1 = select_parent(cur_pop, sum);
            parent_2 = select_parent(cur_pop, sum);

            Crossover(crossoverMethod, cur_pop, parent_1, parent_2, new_pop, i, i + 1);
            Mutate(mutate_prob, new_pop, i);
            Mutate(mutate_prob, new_pop, i + 1);

            //Find the elist(best) chromosome in parent generation
            if (m_population[cur_pop][i].fitness > elistFitness) {
                elist = i;
                elistFitness = m_population[cur_pop][i].fitness;
            }
        }

        //Clone the elist chromosome(twice) into child generation
        m_population[new_pop][0].CopyFrom(m_population[cur_pop][elist]);
        m_population[new_pop][1].CopyFrom(m_population[cur_pop][elist]);

        return new_pop;
    }

    int select_parent(int cur_pop, double sum) {
        // -------  Roulette Wheel Selection by Book ----------
//		int i=RANDMAX(POPULATION_SIZE);
//		int count=POPULATION_SIZE;
//		double select=0.0;
//		while(count--){
//			select=solutions[cur_pop][i].fitness;
//			if(RANDOM()<(select/sum))
//				return i;
//			if(++i>=POPULATION_SIZE)
//				i=0;
//		}
//		return (RANDMAX(POPULATION_SIZE));
        //-------------------------------------------
        // -------  Roulette Wheel Selection by HL ----------
        double r = RANDMAX(sum);
        double s = 0;
        double select = 0;
        int i = 0;
        for (i = 0; i < POPULATION_SIZE; i++) {
            select = m_population[cur_pop][i].fitness;
            s = s + select;
            if (s > r)
                return i;
        }
        return POPULATION_SIZE - 1;
    }

    // compute the lenght of travel cycle
    double compute_travel_length(int[] travel_list) {
        double len = 0;
        int city1 = 0, city2 = 0;
        int n = m_problem.nCity;
        for (int i = 0; i < n - 1; i++) {
            city1 = travel_list[i];
            city2 = travel_list[i + 1];

            len = len + m_problem.cost[city1][city2];
        }
        //make a cycle
        city1 = n - 1;
        city2 = 0;
        len = len + m_problem.cost[city1][city2];
        return len;
    }

//	// main loop for GA: a run of GA
//	// Generate MAX_GENERATION generations, 
//	// Return statistic information for this run
//	// crossoverMethod: 1 or 2
//	//   crossoverMethod=1-- single point crossover
//	//   crossoverMethod=2-- double point crossover
//	// mutateMethod: 1 or 2
//	//   mutateMethod=1 -- use MUTATION_PROB1
//	//   mutateMethod=2 -- use MUTATION_PROB2
//	FitnessStat_Run ga_run(int crossoverMethod,int mutateMethod)
//	{
//		int cur = 0; //current population  0 or 1
//		FitnessStat stat = new FitnessStat();//statistic for current generation
//		FitnessStat_Run stat_run = new FitnessStat_Run();//statistic for whole run
//		int generation=0;
//
//		RANDINIT();
//		initalize_population(cur);
//		compute_population_fitness(cur,stat);//first generation
//		stat_run.AddStat(stat);
//		
//		while ( generation<MAX_GENERATION_PER_RUN-1) {
//			if((generation%500)==0){
//				String str=String.format("%6d: %g %g %g\n",generation,stat.min,stat.avg,stat.max);
//				printCmd(str);
//			}
//			
//			generation++;
//			//printf("%d\n",generation);
//			cur=perform_ga(cur,stat.sum,crossoverMethod,mutateMethod);
//			compute_population_fitness(cur,stat);
//			stat_run.AddStat(stat);
//		}
//		
//		stat_run.DoStat();//Find the optimal sulution in current run.
//		printCmd("-- Teminate at MAX_GENERATION= "+ generation+ " ---\n");
//		printCmd("Best Chromosome: \n");
//		print_chromosome(stat_run.best_member);
//		printCmd("Best travel path: \n");
//		print_travelPath(stat_run.best_travel_path);
//		printCmd( String.format("Len=%f\n\n",stat_run.len_best_travel_path) );
//		
//		return stat_run;
//	}

    // Compute fitness
    // pop: population index
    // member: member index
    double compute_fitness(int pop, int member) {
        //Because the shorter travel length is has a higher fitness, we can simply define:
        //   fitness = n*Average(dist[i,j]) - Length of the traveling cycle
        //To make GA get faster evolution, I powered the fitness, which let:
        //	fitness = POWER(fitness,4)

        Chromosome c = m_population[pop][member];
        //1. Transform chromosome to travel list
        int[] travel_list = c.gene;
        double len = compute_travel_length(travel_list);
        double fitness = 1.0 / len;//1/len;//m_problem.estimateMaxLen-len;//m_problem.estimateAvgLen-len
        if (fitness < 0)
            fitness = 0;
        fitness = Math.pow(fitness, 4);// accelarate convergence
        return fitness;
    }

    //Transform the format of travelPath
    //  e.g.  travelPath=[0,1,2]
    //      Make it to a cycle, and add 1 for each index.
    //        ret= [1,2,3,1]
    private int[] ReformatTarvelPath(int[] travelPath) {
        if (travelPath == null)
            return null;
        int[] cycle = new int[travelPath.length + 1];
        for (int i = 0; i < travelPath.length; i++) {
            cycle[i] = travelPath[i] + 1;
        }
        cycle[travelPath.length] = cycle[0];
        return cycle;
    }

    public TspSolution SolveTspGA(TspData data) {
        int run = 1;
        FitnessStat_Run stat_run = null;
        TspSolution solution = new TspSolution();
        int[] bestTravelPath = null;
        solution.pathcost = Double.MAX_VALUE;

        m_problem.ReadProblem(data);

        //allocate space for population
        int num_gene = m_problem.nCity;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < POPULATION_SIZE; j++) {
                m_population[i][j] = new Chromosome(num_gene);//new Chromosome();// length of chromosome unkown
            }

        String cmd = String.format("CrossMethod=%s, CrossoverRate=%f, MutationRate=%f\n",
                (CROSSOVER_METHOD == 1) ? "single point" : "double point",
                CROSSOVER_PROB, MUTATE_PROB);

        printCmd(cmd);
        long startTime = System.currentTimeMillis();
        for (run = 1; run <= NUM_RUN; run++) {
            String str = String.format("run=%d\n", run);
            printCmd(str);
            //stat_run= ga_run(1,1);
            stat_run = ga_run();
            // choose the best solution
            if (stat_run.len_best_travel_path < solution.pathcost) {
                solution.pathcost = stat_run.len_best_travel_path;
                bestTravelPath = stat_run.best_travel_path;
            }
        }
        long endTime = System.currentTimeMillis();
        solution.millisecond = endTime - startTime;
        solution.path = ReformatTarvelPath(bestTravelPath);
        return solution;
    }

    /**
     * Read Tsp Problem
     * This function is called before "SolveProblem"
     *
     * @param data
     */
    public void ReadProblem(TspData data) {
        m_problem.ReadProblem(data);
    }

    /**
     * Solve Current Problem
     * This function must be called after calling "ReadProblem"
     *
     * @return
     */
    public TspSolution SolveProblem() {
        int run = 1;
        FitnessStat_Run stat_run = null;
        TspSolution solution = new TspSolution();
        int[] bestTravelPath = null;
        solution.pathcost = Double.MAX_VALUE;

        //m_problem.ReadProblem(data);

        //allocate space for population
        int num_gene = m_problem.nCity;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < POPULATION_SIZE; j++) {
                m_population[i][j] = new Chromosome(num_gene);//new Chromosome();// length of chromosome unkown
            }


        String cmd = String.format("CrossMethod=%s,CrossoverRate=%f , MutationRate=%f\n",
                (CROSSOVER_METHOD == 1) ? "single point" : "double point",
                CROSSOVER_PROB, MUTATE_PROB);
        printCmd(cmd);
        long startTime = System.currentTimeMillis();
        for (run = 1; run <= NUM_RUN; run++) {
            String str = String.format("run=%d\n", run);
            printCmd(str);
            //stat_run= ga_run(1,1);
            stat_run = ga_run();
            // choose the best solution
            if (stat_run.len_best_travel_path < solution.pathcost) {
                solution.pathcost = stat_run.len_best_travel_path;
                bestTravelPath = stat_run.best_travel_path;
            }
        }
        long endTime = System.currentTimeMillis();
        solution.millisecond = endTime - startTime;
        solution.path = ReformatTarvelPath(bestTravelPath);

        return solution;
    }

    /**
     * Set whether need show debug info (the performance of each generation)
     *
     * @param bShow
     */
    void SetDebugVisible(boolean bShow) {
        SHOW_DEBUG = bShow;
    }

    class Chromosome {
        //The i-th gene in a chromosome, xi,
        //is in the range of [0..n-1], it means the i-th city to travel is xi.
        int[] gene;
        double fitness;

        Chromosome() {
            gene = null;
            fitness = 0;
        }

        //specify number of genes in chromosome
        Chromosome(int num_gene) {
            gene = new int[num_gene];
            fitness = 0;
        }

        Chromosome Clone() {
            Chromosome x = new Chromosome();
            x.gene = gene.clone();
            x.fitness = fitness;
            return x;
        }

        void CopyFrom(Chromosome c) {
            this.fitness = c.fitness;
            if (c.gene == null) {
                if (this.gene != null) {
                    this.gene = null;
                }
            } else {
                if (this.gene == null) {
                    this.gene = new int[c.gene.length];
                }
                if (this.gene.length != c.gene.length) {
                    this.gene = new int[c.gene.length];
                }
                int n = c.gene.length;
                for (int i = 0; i < n; i++) {
                    this.gene[i] = c.gene[i];
                }
            }

        }
    }

    // A structure to store fitness statistics
    class FitnessStat {
        double min = 0;
        double max = 0;
        double avg = 0;
        double sum = 0;
        double standardDeviation = 0;
        int best_member_ind = 0; // index of best chromosome in current generation
        Chromosome best_member = null;

        FitnessStat Clone() {
            FitnessStat x = new FitnessStat();
            x.min = min;
            x.max = max;
            x.avg = avg;
            x.sum = sum;
            x.standardDeviation = this.standardDeviation;
            x.best_member_ind = this.best_member_ind;
            x.best_member = best_member.Clone();
            return x;
        }

        void Copy(FitnessStat f) {
            this.min = f.min;
            this.max = f.max;
            this.avg = f.avg;
            this.sum = f.sum;
            this.standardDeviation = f.standardDeviation;
            this.best_member_ind = f.best_member_ind;
            if (f.best_member == null) {
                this.best_member = null;
            } else {
                if (this.best_member == null)
                    this.best_member = f.best_member.Clone();
                else {
                    this.best_member.CopyFrom(f.best_member);
                }
            }

        }
    }

    //Statistic for each run
    class FitnessStat_Run {
        double max = 0;// max fitness of whole run
        Chromosome best_member = null;// best member of whole run
        int[] best_travel_path = null;// best travel path
        double len_best_travel_path = 0;// len of best travel path
        int generation = 0;

        //FitnessStat[] fitnessStats=new FitnessStat[MAX_GENERATION_PER_RUN];

        void Clear() {
            this.max = 0;
            this.best_member = null;
            this.best_travel_path = null;
            this.len_best_travel_path = 0;
            this.generation = 0;
        }

        //Add a generation's stat
        // if this generation has a better chromosome(with higher fitness),
        // substitute this Run's max and best_member
        void AddStat(FitnessStat stat) {
            if (stat == null)
                return;
            if (this.max < stat.max) {
                this.max = stat.max;
                if (this.best_member == null) {
                    this.best_member = stat.best_member.Clone();
                } else {
                    this.best_member.CopyFrom(stat.best_member);
                }
            }
            this.generation++;
        }

        // Through  max and best_member based on current stats
        //Find the best solution
        void DoStat() {
            best_travel_path = best_member.gene;
            len_best_travel_path = compute_travel_length(best_travel_path);//1/max;
        }
    }

    //TSP problem
    class Problem {
        public int nCity; //number of cities
        public double[] x;//x coords of each city, x[i-1] is x coord of city i
        public double[] y;//y coords of each city, y[i-1] is y coord of city i
        public double[][] cost; //cost matrix. from city A to city B.
        //Estimated max possible length for a travel cycle = n*MAX(dist[i,j])
        public double estimateMaxLen;// this is not used
        //Estimated average possible length for a travel cycle = n*Average(dist[i,j])
        public double estimateAvgLen;

        //load problem data
        public void ReadProblem(TspData data) {
            nCity = data.n;
            x = data.x.clone();
            y = data.y.clone();
            cost = data.cost.clone();

            double sumCost = 0;
            double maxCost = 0;
            int p = 0;
            for (int i = 0; i < nCity - 1; i++)
                for (int j = i + 1; j < nCity; j++) {
                    p++;
                    sumCost += cost[i][j];
                    if (cost[i][j] > maxCost) {
                        maxCost = cost[i][j];
                    }
                }
            estimateMaxLen = nCity * maxCost;
            estimateAvgLen = sumCost / p * nCity;
        }
    }
}
