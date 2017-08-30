package com.github.daibhin;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

// http://www.jfree.org/jfreechart/:
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Grapher {
	
	private XYSeriesCollection dataset;
	private JFreeChart chart;

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
	public void addDecibleSeries(String seriesTitle, double[] yValues, String xLabel, String yLabel) {
		XYSeries series = new XYSeries(seriesTitle);
		for(int i=0; i < yValues.length; i++) {
			series.add(i+1, yValues[i]);
		}
		dataset.addSeries(series);


		NumberAxis xAxis = new NumberAxis(xLabel);
		xAxis.setAutoTickUnitSelection(true);
		LogAxis yAxis = new LogAxis(yLabel);
		yAxis.setBase(10);
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		XYPlot plot = new XYPlot(new XYSeriesCollection(series), xAxis, yAxis, new XYLineAndShapeRenderer(true, false));
		chart = new JFreeChart(seriesTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
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

	public void createChart(String chartTitle, String xLabel, String yLabel) {
		chart = ChartFactory.createXYLineChart(chartTitle, xLabel, yLabel, dataset);
	}

	public void plotChart(String windowTitle) {
		ChartPanel chartPanel = new ChartPanel(chart);
		
		JFrame frame = new JFrame(windowTitle);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	public void clearSeries() {
		this.dataset.removeAllSeries();
	}

	public void saveChart(String directoryName, String fileName) {
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(directory, fileName);
		try {
			ChartUtilities.saveChartAsPNG(file, chart, 900, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}