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
	
	XYSeriesCollection dataset;

	public Grapher() {
		dataset = new XYSeriesCollection();
	}
	
	public void addSeries(String seriesTitle, double[] yValues) {
		XYSeries series = new XYSeries(seriesTitle);
		for(int i=0; i < yValues.length; i++) {
			series.add(i+1, yValues[i]);
		}
		dataset.addSeries(series);
	}
	public void addSeries(String seriesTitle, double[] xValues, double[] yValues) {
		XYSeries series = new XYSeries(seriesTitle);
		if (xValues.length != yValues.length) {
			System.out.println("Cannot plot series of different lengths");
		}
		for(int i=0; i < xValues.length; i++) {
			series.add(xValues[i], yValues[i]);
		}
		dataset.addSeries(series);
	}

	public void plotGraph(String seriesTitle, String xLabel, String yLabel) {
		JFreeChart chart = ChartFactory.createXYLineChart(seriesTitle, xLabel, yLabel, dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		JFrame frame = new JFrame("Convergence Chart");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}