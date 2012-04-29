package org.botbeans.blocks.util;

import org.jfree.chart.JFreeChart;


public abstract class ChartData {
	public final String title;
	
	public abstract JFreeChart makeChart();
	public ChartData(String title) {
		this.title = title;
	}
}
