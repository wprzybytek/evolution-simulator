package simulator.gui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

@SuppressWarnings("unchecked")
public class Chart {
    private final LineChart chart;
    private final XYChart.Series dataSeries;

    //constructor
    public Chart(String yAxisName) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisName);
        this.chart = new LineChart(xAxis, yAxis);
        this.chart.setLegendVisible(false);
        dataSeries = new XYChart.Series();
        this.chart.getData().add(this.dataSeries);
    }

    //getters
    public LineChart getChart() {
        return this.chart;
    }

    //methods
    public void updateChart(int day, int value) {
        this.dataSeries.getData().add(new XYChart.Data(day, value));
    }
}
