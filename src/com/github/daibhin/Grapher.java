package com.github.daibhin;

import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.github.daibhin.Functions.Func;

public class Grapher {
	
	private XYSeries series;

	public Grapher(double[] yValues) {
		double[] xValues = new double[yValues.length];
		for(int i=1; i <= xValues.length; i++) {
			xValues[i] = i;
		}
		setupGrapher(xValues, yValues);
	}
	
	private void setupGrapher(double[] xValues, double[] yValues) {
		this.series = new XYSeries("Convergence");
		if (xValues.length != yValues.length) {
			System.out.println("Cannot plot series of different lengths");
		}
		for(int i=0; i<xValues.length; i++) {
			this.series.add(xValues[i], yValues[i]);
		}
	}

	public Grapher(double[] xValues, double[] yValues) {
		setupGrapher(xValues, yValues);
	}

	private void plotConvergence(XYSeries series) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Convergence", "Iteration", "Fitness", dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		JFrame frame = new JFrame("Convergence Chart");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}