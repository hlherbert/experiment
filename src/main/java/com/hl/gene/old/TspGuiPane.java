package com.hl.gene.old;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

/**
 * The canvas to draw Tsp data and Tsp Solutions
 */
class TspGuiPane extends JPanel
{
	private static final long serialVersionUID = 2055434379411801673L;
	private final double CityPointRadius=5;
	private final double Margin = 15; //margin of canvas
	private TspData tspData;
	private TspSolution tspSolution;
	
	public void SetTsp(TspData data, TspSolution solution)
	{
		tspData = data;
		tspSolution = solution;
		Graphics g = this.getGraphics();
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		this.repaint();
	}
	
	public void paintComponent(Graphics comp)
	{
		Graphics2D comp2D=(Graphics2D)comp;
		//comp2D.drawString("sbcd",200,200);
		//Line2D.Float line = new Line2D.Float(1.0f,2.0f,200.0f,200.0f);
		//comp2D.draw(line);
		DrawTspInputData(comp2D,tspData);
		DrawTspSolution(comp2D,tspData,tspSolution);
	}
	
	// get boundary of all cities
	private Rectangle2D.Double GetCitiesBound(TspData data)
	{
		double min_city_x =Tool.findMin(data.x);
		double max_city_x =Tool.findMax(data.x);
		double min_city_y =Tool.findMin(data.y);
		double max_city_y =Tool.findMax(data.y);
		double city_bound_w = max_city_x-min_city_x;
		double city_bound_h = max_city_y-min_city_y;
		return new Rectangle2D.Double(min_city_x,min_city_y,city_bound_w,city_bound_h);
	}
	
	private AffineTransform GetCityBoundToCanvasTransform()
	{
		Rectangle2D.Double cityBound = GetCitiesBound(tspData);
		// Size of canvas
		Dimension canvasSize = this.getSize();
		double canvas_w = canvasSize.width-2*Margin;
		double canvas_h = canvasSize.height-2*Margin;
		if (canvas_w<0) canvas_w=0;
		if (canvas_h<0) canvas_h=0;
		AffineTransform newTx = new AffineTransform();
		newTx.translate(Margin, canvas_h+Margin);
		// we need to flip the graph, because in Screen coordinate, y-axis is 
		//  direct from top to down. But city's y-axis directs from bottom to top.
		newTx.scale(canvas_w/cityBound.width, -canvas_h/cityBound.height);
		newTx.translate(-cityBound.x,-cityBound.y);
		return newTx;
	}
	
	private void DrawTspInputData(Graphics2D graph, TspData data)
	{
		//draw all the cities
		if (data==null)
			return;
		
		// record original color
		Color color = graph.getColor();
				
		// transform the city position(x,y), which make the cities full of the canvas
		AffineTransform newTx = GetCityBoundToCanvasTransform();
		Point2D pt = new Point2D.Double();
		Point2D ptDst = new Point2D.Double();
		for (int i=0;i<data.n;i++)
		{
			double x = data.x[i];
			double y = data.y[i];
			
			pt.setLocation(x, y);
			newTx.transform(pt, ptDst);
			x= ptDst.getX();
			y=ptDst.getY();
			Ellipse2D cityCircle = 
				new Ellipse2D.Double(x-CityPointRadius, y-CityPointRadius, 
						2*CityPointRadius, 2*CityPointRadius);
			
			graph.setColor(Color.blue);
			graph.fill(cityCircle);
			graph.setColor(Color.red);
			graph.drawString( Integer.toString(i+1), (float)x, (float)y);
		}
		//restore color
		graph.setColor(color);
	}
	
	private void DrawTspSolution(Graphics2D graph, TspData data, TspSolution solution)
	{
		// draw tsp path line
		if (solution == null)
			return;
		if (solution.path==null)
			return;
		int n = solution.path.length;//size of cities in solution
		if (n<2)
			return;
		
		Path2D.Double path = new Path2D.Double();
		Line2D.Double line = new Line2D.Double();
		
		// transform the city position(x,y), which make the cities full of the canvas
		AffineTransform newTx = GetCityBoundToCanvasTransform();
		Point2D pt = new Point2D.Double();
		Point2D ptDst = new Point2D.Double();
		
		int city = solution.path[0];//city index in path
		double x = data.x[city-1];
		double y = data.y[city-1];
		pt.setLocation(x, y);
		newTx.transform(pt, ptDst);
		
		line.x1 = ptDst.getX();
		line.y1 = ptDst.getY();
		
		for (int i=1;i<n;i++)
		{
			city = solution.path[i];//city index in path
			x = data.x[city-1];
			y = data.y[city-1];
			pt.setLocation(x, y);
			newTx.transform(pt, ptDst);
			line.x2 = ptDst.getX();
			line.y2 = ptDst.getY();
			path.append(line, false);
			line.x1 = line.x2;
			line.y1 = line.y2;
		}
		
		graph.draw(path);
	}
}