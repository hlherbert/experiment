package com.hl.gene.neo.tsp;

/**
 * 旅行商问题
 */
public class TspProblem
{
    public int nCity; //number of cities
    public double[] x;//x coords of each city, x[i-1] is x coord of city i
    public double[] y;//y coords of each city, y[i-1] is y coord of city i
    public double[][] cost; //cost matrix. from city A to city B.
    //Estimated max possible length for a travel cycle = n*MAX(dist[i,j])
    public double estimateMaxLen;// this is not used
    //Estimated average possible length for a travel cycle = n*Average(dist[i,j])
    public double estimateAvgLen;

    //load problem data
    public void ReadProblem(TspData data)
    {
        nCity = data.n;
        x = data.x.clone();
        y =	data.y.clone();
        cost = data.cost.clone();

        double sumCost=0;
        double maxCost=0;
        int p=0;
        for (int i=0;i<nCity-1;i++)
            for (int j=i+1;j<nCity;j++)
            {
                p++;
                sumCost+=cost[i][j];
                if (cost[i][j]>maxCost)
                {
                    maxCost=cost[i][j];
                }
            }
        estimateMaxLen = nCity*maxCost;
        estimateAvgLen = sumCost/p*nCity;
    }
}