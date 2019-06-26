package com.hl.gene.old; /**
 * CECS-545 Artificial Intelligence
 * Project 3. Traveling Salesperson Problem �\ Greedy
 * Li Huang   l0huan08@louisville.edu
 * CECS-Speed School-University of Louisville
 * 2013.9.22
 */

import java.util.*;

/**
 *  An agent solve TSP problem using Greedy strategy
 * @author hl
 *
 */
public class TspGreedy {

	public TspSolution SolveTspGreedy(TspData data, int startCity)
	{
		Problem problem = new Problem();
		problem.ReadProblem(data);
		problem.startCity = startCity;
		Strategy strategy = new Strategy();
		
		Performance performance = new Performance();
		Solution solution = Greedy(problem, strategy, performance);
		
		//FAIL
		if (solution==null)
		{
			return null;
		}
		
		return ConvertSolution(solution,performance);
	}
	
	
	private TspSolution ConvertSolution(Solution solution, Performance performance)
	{
		TspSolution tspSol= new TspSolution();
		tspSol.pathcost = solution.pathcost;
		Object[] path =solution.path.toArray();
		tspSol.path = new int[path.length];
		for (int i=0;i<path.length;i++)
		{
			tspSol.path[i]= (Integer)path[i];
		}
		tspSol.millisecond = performance.millisecond;//measure running time 
		tspSol.transition = performance.transition; //measure transitions
		
		return tspSol;
	}
	// Use greedy strategy solve problem
	public Solution Greedy(Problem problem, Strategy strategy, Performance performance)
	{
//		Function Greedy(problem, strategy) returns a solution, or failure
//		node <- a node with state = problem.InitialState
//		while (true)
//			If problem.TestGoal(node.state)
//				return MakeSolution(node)
//			hMin <- +infinity
//			for each action among problem.Actions(node.state)
//				hAction <- strategy.Heuristic(problem,action)
//				if hAction<hMin
//					choiceAction <-action
//					hMin <- hAction
//			node <- ChildNode(problem,node,choiceAction)
		
		int transition =0;
		// measure running time
		long startTime=System.currentTimeMillis();//milliseconds of current time
		//-------------------------------------------------------
		Solution solution=null;
		Node node = new Node();
		node.state = problem.InitialState();
		while (true)
		{
			if (problem.GoalTest(node.state)){
				solution= strategy.MakeSolution(problem, node);
				break;
			}
			//--------- TEST ----
//			if (transition==4){
//				solution= strategy.MakeSolution(problem, node);
//				break;
//			}
			//---------- END TEST ----
			Action action = strategy.ChooseAction(problem, node.state);
			node = strategy.GetChildNode(problem,node, action);
			transition++;
		}
		//-------------------------------------------------------
		long endTime=System.currentTimeMillis();//milliseconds of current time
		performance.millisecond = endTime-startTime;//measure running time 
		performance.transition = transition;
		return solution;
	}
	
	
//------------------------CLASSES FOR GREEDY SEARCH ----------------------------	
	
	//-------------  Data structure for the Search Tree ----------------
	// Each node stores:
	// * current state in this step.
	// * a pointer to its parent node, and the total cost so far.
	// For space efficiency, We do not need to store the whole state in the Node.
	class Node
	{
		public State state; //current state
		public Node parent; //the parent Node,stores the last step
		
		public Node()
		{
			state=null;
			parent=null;
		}
		public Node(State st,double pc,Node pr)
		{
			state=st;
			parent=pr;
		}
	}

	class State
	{
		//Graph of current travel cycle, G = (T, E). 
		//T is a subset of cities which are ��traveled��. 
		//E is all the edges in current travel cycle. 
		//All the rest ��untraveled�� cities besides T are in the subset R. 
		//Assume V is the set of all the cities, R=V-T
		public LinkedList<Integer> T; //also stored E
		public LinkedList<Integer> R;

		//The edges E is stored in the order of T
		// T(i,i+1) is an edge
		public State clone()
		{
			State s = new State();
			s.T = (LinkedList<Integer>)(T.clone());
			s.R = (LinkedList<Integer>)(R.clone());
			return s;
		}
	}

	// each Action is insert a city, x, into an edge, with two vertex a and b.
	class Action
	{
		public int city_insert;
		public int edge_vertex_a;
		public int edge_vertex_b;
	}

	class Problem
	{
		public int nCity; //number of cities
		public int startCity; //the start city
		public double[] x;//x coords of each city, x[i-1] is x coord of city i
		public double[] y;//y coords of each city, y[i-1] is y coord of city i
		public double[][] cost; //cost matrix. from city A to city B.
		
		//Whether the state achieves the goal
		public boolean GoalTest(State state)
		{
			// when all the city were traveled, goal achieved
			if (state.T==null)
				return false;
			if (state.T.size()==nCity)
				return true;
			else return false;
		}
		
		// return the list of cities can traveled
		public List<Integer> GetActions(State state)
		{
			return state.R;
		}
				
		// Do a action, return the next state
		public State DoAction(State state, Action action)
		{
			State s = (State)(state.clone());
			int a = action.edge_vertex_a; // the edge's first vertex
			Iterator<Integer> it = s.T.iterator();
			int i=0;
			// pick a city from R, move it to T
			while ( it.hasNext() )
			{
				int aa = it.next();
				if (a==aa)
				{
					s.T.add(i+1, action.city_insert);
					break;
				}
				i++;
			}
			s.R.removeFirstOccurrence(action.city_insert);
			return s;
		}
		
		//load problem data
		public void ReadProblem(TspData data)
		{
			nCity = data.n;
			x = data.x.clone();
			y =	data.y.clone();
			cost = data.cost.clone();
		}
		
		//make the Initial State
		//choose a random start city
		public State InitialState()
		{
			//int startCity = (int)Math.random()*nCity+1;
			State state = new State();
			state.T = new LinkedList<Integer>();
			state.R = new LinkedList<Integer>();
			//Add all the city except startCity to unexplored city set R
			for (int i=1;i<=nCity;i++)
			{
				if (i != startCity)
					state.R.add(i);
			}
			state.T.add(startCity);
			return state;
		}
	}


	class Solution
	{
		public LinkedList<Integer> path;//solution path
		public double pathcost;//path cost of this solution
		
		// copy data from solution b
		public void CopyFrom(Solution b)
		{
			if (b==null)
				return;
			this.path = (LinkedList<Integer>)b.path.clone();
			this.pathcost = b.pathcost;
		}
	}
	
	/**
	 * Greedy Strategy
	 */
	class Strategy
	{
		// back trace from the dist city(node) to the start city.
		public Solution MakeSolution(Problem problem, 
				Node node)
		{
			Solution s = new Solution();
			if (node==null)
				return null;
			s.path = (LinkedList<Integer>)node.state.T.clone();
			s.path.add(node.state.T.getFirst()); // make a cycle
			s.pathcost = Tool.PathCost(s.path, problem.cost);
			return s;
		}
		// Do action, goto the next city, here action is the next city number.
		// And update current state: pick the next city from state.R into state.T
		public Node GetChildNode(Problem problem,Node node, Action action)
		{
			Node child = new Node();
			child.parent = node;
			State nextState = problem.DoAction(node.state,action);
			child.state = nextState;
			return child;
		}
		
		// The next action we choose, according to current state
		public Action ChooseAction(Problem problem, State state)
		{
			// here we use the greedy strategy,
			//choose the city which has the minimum heuristic function value.
			List<Integer> cities_to_choose = problem.GetActions(state);
			Iterator<Integer> it = cities_to_choose.iterator();
			if (!it.hasNext())
				return null;
			int choose_city = 0;
			int city = it.next();
			choose_city = city;
			EdgeAndDistance edge_and_dist = Heuristic(problem,city,state);
			double hMin = edge_and_dist.dist;
			int edge_a = edge_and_dist.edge_vertex_a; // the edge to be insert e=(a,b)
			int edge_b= edge_and_dist.edge_vertex_b;
			while (it.hasNext())
			{
				city = it.next();
				edge_and_dist = Heuristic(problem,city,state);
				double h = edge_and_dist.dist;
				if (h<hMin)
				{
					hMin = h;
					choose_city=city;
					edge_a = edge_and_dist.edge_vertex_a;
					edge_b = edge_and_dist.edge_vertex_b;
				}
			}
			Action action = new Action();
			action.city_insert =choose_city;
			action.edge_vertex_a=edge_a;
			action.edge_vertex_b=edge_b;
			return action;
		}
		
		//
		public EdgeAndDistance Heuristic(Problem problem, Integer city, State state)
		{
			return DistCityGraph(problem,city,state.T);
		}
		
		// used only for DistCityGraph
		class EdgeAndDistance
		{
			//edge = (a,b)
			public int edge_vertex_a; //the index of vertex a
			public int edge_vertex_b;
			public double dist;
			
			public EdgeAndDistance()
			{
				edge_vertex_a=0;
				edge_vertex_b=0;
				dist=0;
			}
			public EdgeAndDistance(int a, int b,double d)
			{
				edge_vertex_a=a;
				edge_vertex_b=b;
				dist =d ;
			}
		}
		
		//Calulate the dist from a city,x, to current sub graph.
		//  dist(x,G)=Min({dist(x,e)|e in E})
		//  From T we can get E, E=just add the start city into the tail of T
		// Returns the nearest edge to this city and the their distance
		private EdgeAndDistance DistCityGraph(Problem problem, Integer city, LinkedList<Integer> T)
		{
			//find each edge e in E
			Iterator<Integer> it = T.iterator();
			if (!it.hasNext())
				return null; //if T is an empty set, FAIL!
			
			int i=0,j=0; // the edge vertex index. e=(i,j)
			double d=0; //nearest distance
			i = it.next();
			if (!it.hasNext())
			{
				// only one city in T
				j = i;
				d = DistCityEdge(problem,city,i,j);
				return new EdgeAndDistance(i,j,d);
			}
			j=it.next();
			int minI=i;
			int minJ=j; //the vertex index of the nearest edge.
			d = DistCityEdge(problem,city,i,j);
			
			while (it.hasNext())
			{
				i=j;
				j=it.next();
				double dd = DistCityEdge(problem,city,i,j);
				if (dd<d)
				{
					d=dd;
					minI=i;
					minJ=j;
				}
			}
			// the last edge is from T[n-1] to T[0]
			i=j;
			j=T.getFirst();
			double dd = DistCityEdge(problem,city,i,j);
			if (dd<d)
			{
				d=dd;
				minI=i;
				minJ=j;
			}
			return new EdgeAndDistance(minI,minJ,d);
		}
		
		// Distance from a city to an edge=(e1,e2)
		// input are indices of cities
		private double DistCityEdge(Problem problem,int city, int e1, int e2)
		{
			double x0=problem.x[city-1];
			double y0=problem.y[city-1];
			double x1= problem.x[e1-1];
			double y1= problem.y[e1-1];
			double x2=problem.x[e2-1];
			double y2=problem.y[e2-1];
			
			return DistPointEdge(x0,y0,x1,y1,x2,y2);
		}
		
		//Calulate the distance from a point C(x0,y0) to the edge AB(x1,y1--x2,y2)
		private double DistPointEdge(double x0,double y0, double x1,double y1,
				double x2,double y2)
		{
			//Special case 1: if the edge��s length is 0, then d=dist(x,a), 
			//which is the straight-line distance from x to a.
			double d = 0;
			if (x1==x2 && y1==y2)
				d=DistPointToPoint(x0,y0,x1,y1);
			else
			{
				//Special case 2
				//Calculate distance from a city C to an edge AB:
				//(a)When the angle a(BAC) and angle b(ABC) are both less than or equal to 90 degree, then distance=length of  perpendicular line (CD).
				//(b)When any of angle a or angle b is more than 90 degree, then distance=min{ length(CA), length(CB) }
				//Judge the angle of two vectors a and b : 
				//When a.b>=0,the angle is less than or equal to 90 degree.
				//When a.b<0,the angle is more than 90 degree.
				boolean is_angleA_more_than_90 = Tool.degreeMoreThan90(x0, y0, x1, y1, x2, y2);
				boolean is_angleB_more_than_90 = Tool.degreeMoreThan90(x0, y0, x2, y2, x1, y1);
				if (is_angleA_more_than_90 || is_angleB_more_than_90)
				{
					double d1 = DistPointToPoint(x0,y0,x1,y1);
					double d2 = DistPointToPoint(x0,y0,x2,y2);
					d = Math.min(d1, d2);
				}
				else
					d = Math.abs((x2-x1)*(y1-y0)-(x1-x0)*(y2-y1))/
						Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			}
			return d;
		}
		
		//The distance of two points (x1,y1) and (x2,y2)
		private double DistPointToPoint(double x1,double y1,double x2,double y2)
		{
			return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		}
	}
	
	class Performance
	{
		public int transition; //number of transitions it takes
		public long millisecond; //time used. (millisecond)
	}

//------------------------------------------------------------------------------	
	
}
