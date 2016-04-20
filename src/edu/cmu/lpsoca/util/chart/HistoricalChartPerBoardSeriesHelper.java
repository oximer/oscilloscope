package edu.cmu.lpsoca.util.chart;

import java.util.*;

/**
 * Created by urbano on 4/14/16.
 */
public final class HistoricalChartPerBoardSeriesHelper {
    public String name;
    public List<String> categoryList;
    public TreeMap<String, Map<Integer, List<Double>>> chartData;

    public HistoricalChartPerBoardSeriesHelper(String name, List<String> categoryList, List<Integer> taskList) {
        this.name = name;
        this.categoryList = categoryList;
        chartData = new TreeMap<>();
        for (String category : categoryList) {
            HashMap taskToReading = new HashMap<Integer, List<Double>>();
            for (Integer taskId : taskList) {
                taskToReading.put(taskId, new ArrayList<>());
            }
            chartData.put(category, taskToReading);
        }
    }

    public void insertData(String category, Integer task, Double data) {
        HashMap taskToReading = (HashMap) chartData.get(category);
        List<Double> list = (List<Double>) taskToReading.get(task);
        list.add(data);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TreeMap<String, Map<Integer, List<Double>>> getChartData() {
        return chartData;
    }

    public double[] getRawData() {
        double[] data = new double[chartData.size()];
        int counter = 0;
        for (Map.Entry<String, Map<Integer, List<Double>>> entry : chartData.entrySet()) {
            Map<Integer, List<Double>> listOfValuesInsideSameCategory = entry.getValue();
            double taskSum = 0;
            for (Map.Entry<Integer, List<Double>> entry2 : listOfValuesInsideSameCategory.entrySet()) {
                if (entry2.getValue().size() > 0) {
                    taskSum += entry2.getValue().stream().mapToDouble(a -> a).average().getAsDouble();
                }
            }
            data[counter] = taskSum;
            counter++;
        }
        return data;
    }
}