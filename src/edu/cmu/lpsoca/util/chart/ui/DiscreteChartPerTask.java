package edu.cmu.lpsoca.util.chart.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.ChartSeries;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.chart.PreparePowerChart;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by urbano on 4/19/16.
 */
public class DiscreteChartPerTask {

    public static List<ChartSeries> getPowerPerTask(List<Board> boards, long startTimeStamp, long endTimeStamp) throws ServletException, JsonProcessingException {
        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = new DatabasePersistenceLayer();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        List<Message> listMessage = databasePersistenceLayer.getAllMessages(startTimeStamp, endTimeStamp);
        databasePersistenceLayer.terminate();

        //Task, Board, Energy
        Map<Integer, Map<Integer, List<Float>>> taskToBoard = new TreeMap<Integer, Map<Integer, List<Float>>>();
        ArrayList<Integer> boardsIdList = new ArrayList<Integer>();
        for (Board board : boards) {
            boardsIdList.add(board.getId());
        }
        Collections.sort(boardsIdList);

        for (Message item : listMessage) {
            if (taskToBoard.containsKey(item.getTaskId())) {
                Map<Integer, List<Float>> boardToEnergy = taskToBoard.get(item.getTaskId());
                if (boardToEnergy.containsKey(item.getBoardId())) {
                    List<Float> energyList = boardToEnergy.get(item.getBoardId());
                    energyList.add(PreparePowerChart.getPower(item));
                    boardToEnergy.put(item.getBoardId(), energyList);
                } else {
                    ArrayList<Float> energyReadings = new ArrayList<>();
                    energyReadings.add(PreparePowerChart.getPower(item));
                    boardToEnergy.put(item.getBoardId(), energyReadings);
                }
            } else {
                Map<Integer, List<Float>> boardToEnergy = new HashMap<Integer, List<Float>>();
                ArrayList<Float> energyReadings = new ArrayList<>();
                energyReadings.add(PreparePowerChart.getPower(item));
                boardToEnergy.put(item.getBoardId(), energyReadings);
                taskToBoard.put(item.getTaskId(), boardToEnergy);
            }
        }

        List<ChartSeries> chartSeriesList = new ArrayList<ChartSeries>();
        for (Map.Entry<Integer, Map<Integer, List<Float>>> entry : taskToBoard.entrySet()) {
            int task = entry.getKey();
            double energy[] = new double[boards.size()];
            Map<Integer, List<Float>> boardToEnergy = entry.getValue();
            for (Map.Entry<Integer, List<Float>> item : boardToEnergy.entrySet()) {
                energy[boardsIdList.indexOf(item.getKey())] += item.getValue().stream().mapToDouble(a -> a).average().getAsDouble();
            }
            ChartSeries chartSeries = new ChartSeries(String.format("Task %d", task), energy);
            chartSeriesList.add(chartSeries);
        }

        return chartSeriesList;
    }
}
