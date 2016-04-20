package edu.cmu.lpsoca.util.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by urbano on 4/14/16.
 */
public final class HistoricalChartPerTaskSeriesHelper {
    public String name;
    public List<String> categoryList;
    public TreeMap<String, List<Double>> chartData;

    public HistoricalChartPerTaskSeriesHelper(String name, List<String> categoryList) {
        this.name = name;
        this.categoryList = categoryList;
        chartData = new TreeMap<>();
        for (String category : categoryList) {
            chartData.put(category, new ArrayList<>());
        }
    }

    public void insertData(String category, Double data) {
        List<Double> list = chartData.get(category);
        list.add(data);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TreeMap<String, List<Double>> getChartData() {
        return chartData;
    }

    public double[] getRawData() {
        double[] data = new double[chartData.size()];
        int counter = 0;
        for (Map.Entry<String, List<Double>> entry : chartData.entrySet()) {
            List<Double> listOfValuesInsideSameCategory = entry.getValue();
            if (listOfValuesInsideSameCategory.size() > 0) {
                data[counter] = listOfValuesInsideSameCategory.stream().mapToDouble(a -> a).average().getAsDouble();
            } else {
                data[counter] = 0;
            }
            counter++;
        }
        return data;
    }
}