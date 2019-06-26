package com.hl.gene.neo.tsp;

import java.util.Iterator;
import java.util.List;

/**
 * MODULE 3
 * Tool functions
 * Which is common and may be used in any problems.
 * @author hl
 *
 */
public class Tool {
	/**
	* calculate factorial
	* @param n
	* @return n!
	*/
	public static long Factorial(int n){
	   int result=1;
	   if(n<0){
	        return -1;//invalid n
	   }
	   if(n==0){//0!=1
	       return 1;
	   }
	   for(int i=1;i<=n;i++){
	       result*=i;
	   }
	   return result;
	}
	
	/**
	 * swap two elements, at i and j, of an array
	 * @param arr  An array
	 * @param i  index i
	 * @param j  index j
	 */
	public static void swap(int[] arr,int i,int j)
	{
		int t=arr[i];
		arr[i]=arr[j];
		arr[j]=t;
	}
	
	/**
	 * Copy the element of src into dst array.
	 * These two array should have same size
	 * @param src  Source array
	 * @param dst  Destination array
	 */
	public static void copy(int[] src, int[] dst)
	{
		int n = src.length;
		for (int i=0;i<n;i++)
		{
			dst[i]=src[i];
		}
	}
	
	/**
	 * Copy part of the data from src to dst array.
	 * @param src  Source array
	 * @param dst  Destination array
	 * @param startSrc  The index of data started to copy in src
	 * @param startDst  The index of data started to be copied to in dst
	 * @param n  Number of data to be copy
	 */
	public static void copyPart(int[] src, int[] dst, int startSrc,int startDst,int n)
	{
		for (int i=0;i<n;i++)
		{
			dst[startDst+i]=src[startSrc+i];
		}
	}
	
	/**
	 * print array
	 * @param arr
	 */
	public static void printArray(int[] arr)
	{
		if (arr==null)
			return;
		int n = arr.length;
		for (int i=0;i<n;i++)
			System.out.print(arr[i]+",");
	}
	
	public static void printlnArray(int[] arr)
	{
		if (arr==null)
			return;
		printArray(arr);
		System.out.println();
	}
	
	/**
	 * Is integer x an even number?
	 * @param x
	 * @return
	 */
	public static boolean IsEven(int x)
	{
		return (x & 0x1)==0x0;
	}
	/**
	 *  Is integer x an odd number?
	 * @param x
	 * @return
	 */
	public static boolean IsOdd(int x)
	{
		return (x & 0x1)==0x1;
	}
	
	// Calculate the total cost of path
	// path: city indices
	// cost: cost[i][j] store the cost from city i+1 to city j+1
	public static double PathCost(List<Integer> path, double[][] cost)
	{
		double pathcost=0;
		Iterator<Integer> it = path.iterator();
		Integer i=0,j=0;
		if (it.hasNext())
		{
			i = it.next();
		}
		while (it.hasNext())
		{
			j = it.next();
			pathcost += cost[i-1][j-1];
			i = j;
		}
		return pathcost;
	}
	
	// Calculate the total cost of path
		// path: city indices 1,2,..n
		// cost: cost[i][j] store the cost from city i+1 to city j+1
		public static double PathCost(int[] path, double[][] cost)
		{
			double pathcost=0;
			int n = path.length-1;
			
			for (int i=0;i<n;i++)
			{
				int x = path[i];
				int y = path[i+1];
				pathcost += cost[x-1][y-1];
			}
			return pathcost;
		}
		
	
	// Find min number in an array.
	public static double findMin(double[] arr)
	{
		double min = arr[0];
		for (int i=0;i<arr.length;i++)
		{
			if (arr[i]<min)
				min=arr[i];
		}
		return min;
	}
	
	// Find max number in an array.
	public static double findMax(double[] arr)
	{
		double max = arr[0];
		for (int i=0;i<arr.length;i++)
		{
			if (arr[i]>max)
				max=arr[i];
		}
		return max;
	}
	
	//return the vector multiply = (x1,y1).(x2,y2)
	public static double vectorMultiply(double x1,double y1, double x2, double y2)
	{
		return x1*x2+y1*y2;
	}
	
	//return if the angle ABC is more than 90
	public static boolean degreeMoreThan90(double xA,double yA,
			double xB,double yB,double xC,double yC)
	{
		//Judge the angle of two vectors a and b : 
		//When a.b>=0,the angle is less than or equal to 90 degree.
		//When a.b<0,the angle is more than 90 degree.
		double xBA = xA-xB; //vector BA
		double yBA = yA-yB;
		double xBC = xC-xB;//vector BC
		double yBC = yC-yB;
		double mul = vectorMultiply(xBA,yBA,xBC,yBC);
		if (mul<0)
			return true;
		else
			return false;
	}
}
