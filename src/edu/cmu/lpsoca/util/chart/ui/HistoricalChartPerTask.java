package edu.cmu.lpsoca.util.chart.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.model.NewChartSeries;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.AnalogToEnergyReader;
import edu.cmu.lpsoca.util.chart.HistoricalChartPerTaskSeriesHelper;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by urbano on 4/19/16.
 */
public class HistoricalChartPerTask {

    public static List<NewChartSeries> getHistoricalPowerPerTask(int boardId, long startTimeStamp, long endTimeStamp, List<String> listOfXAxisIntervals) throws ServletException, JsonProcessingException {
        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = new DatabasePersistenceLayer();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        List<Integer> listTaskId = databasePersistenceLayer.getListOfTasks(boardId);
        long stepSize = ((endTimeStamp - startTimeStamp) / listOfXAxisIntervals.size());

        HashMap<Integer, HistoricalChartPerTaskSeriesHelper> taskToSerie = new HashMap<>();
        for (Integer id : listTaskId) {
            taskToSerie.put(id, new HistoricalChartPerTaskSeriesHelper(String.format("Task %s", id), listOfXAxisIntervals));
        }

        for (int i = 0; i < listOfXAxisIntervals.size(); i++) {
            List<Message> listMessage = databasePersistenceLayer.getAllMessages(boardId, startTimeStamp + (i * stepSize), startTimeStamp + ((i + 1) * stepSize));
            for (Message message : listMessage) {
                HistoricalChartPerTaskSeriesHelper chartSerie = taskToSerie.get(message.getTaskId());
                chartSerie.insertData(listOfXAxisIntervals.get(i), (double) (AnalogToEnergyReader.convertToPower(message.getChannel1(), 1) + AnalogToEnergyReader.convertToPower(message.getChannel2(), 2) +
                        AnalogToEnergyReader.convertToPower(message.getChannel3(), 3) + AnalogToEnergyReader.convertToPower(message.getChannel4(), 4)));
            }
        }

        databasePersistenceLayer.terminate();

        List<NewChartSeries> newChartSerieList = new ArrayList<>();
        String[] xAxisArray = listOfXAxisIntervals.toArray(new String[0]);
        for (Map.Entry<Integer, HistoricalChartPerTaskSeriesHelper> entry : taskToSerie.entrySet()) {
            NewChartSeries chartSeries = new NewChartSeries(entry.getValue().getName(), entry.getValue().getRawData(), xAxisArray);
            newChartSerieList.add(chartSeries);
        }

        return newChartSerieList;
    }
}
