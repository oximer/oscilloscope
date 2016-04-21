package edu.cmu.lpsoca.util.chart.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.model.NewChartSeries;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.AnalogToEnergyReader;
import edu.cmu.lpsoca.util.chart.HistoricalChartPerBoardSeriesHelper;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by urbano on 4/19/16.
 */
public class HistoricalChartPerBoard {

    private static Object demoBoardSerieData;

    public static List<NewChartSeries> getHistoricalPowerPerBoard(long startTimeStamp, long endTimeStamp, List<String> listOfXAxisIntervals) throws ServletException, JsonProcessingException {
        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = new DatabasePersistenceLayer();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        List<Board> listBoard = databasePersistenceLayer.getBoards(null);
        List<Integer> listTask = databasePersistenceLayer.getListOfTasks(null);
        long stepSize = ((endTimeStamp - startTimeStamp) / listOfXAxisIntervals.size());

        HashMap<Integer, HistoricalChartPerBoardSeriesHelper> taskToSerie = new HashMap<>();
        for (Board board : listBoard) {
            taskToSerie.put(board.getId(), new HistoricalChartPerBoardSeriesHelper(String.format("Board %s", board.getId()), listOfXAxisIntervals, listTask));
        }

        for (int i = 0; i < listOfXAxisIntervals.size(); i++) {
            List<Message> listMessage = databasePersistenceLayer.getAllMessages(null, startTimeStamp + (i * stepSize), startTimeStamp + ((i + 1) * stepSize));
            for (Message message : listMessage) {
                HistoricalChartPerBoardSeriesHelper chartSerie = taskToSerie.get(message.getBoardId());
                chartSerie.insertData(listOfXAxisIntervals.get(i), message.getTaskId(), (double) (AnalogToEnergyReader.convertToPower(message.getChannel1(), 1) + AnalogToEnergyReader.convertToPower(message.getChannel2(), 2) +
                        AnalogToEnergyReader.convertToPower(message.getChannel3(), 3) + AnalogToEnergyReader.convertToPower(message.getChannel4(), 4)));
            }
        }

        databasePersistenceLayer.terminate();

        List<NewChartSeries> newChartSerieList = new ArrayList<>();
        String[] xAxisArray = listOfXAxisIntervals.toArray(new String[0]);
        for (Map.Entry<Integer, HistoricalChartPerBoardSeriesHelper> entry : taskToSerie.entrySet()) {
            NewChartSeries chartSeries = new NewChartSeries(entry.getValue().getName(), entry.getValue().getRawData(), xAxisArray);
            newChartSerieList.add(chartSeries);
        }

        return newChartSerieList;
    }
}
