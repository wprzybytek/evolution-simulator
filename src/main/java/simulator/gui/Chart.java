package simulator.gui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Chart {
    private LineChart chart;

    public Chart(String yAxisName) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisName);
        this.chart = new LineChart(xAxis, yAxis);
        this.chart.setLegendVisible(false);
    }

    public LineChart getChart() {
        return this.chart;
    }

    public void updateChart(int day, int value) {
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.getData().add(new XYChart.Data(day, value));
        this.chart.getData().add(dataSeries);
    }
}
