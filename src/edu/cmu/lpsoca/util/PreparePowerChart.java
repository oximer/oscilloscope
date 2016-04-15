package edu.cmu.lpsoca.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.chart.ChartSeries;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by urbano on 4/14/16.
 */
public class PreparePowerChart {

    public static final String TASK_ENERGY_SERIES = "taskEnergySeries";
    public static final String BOARD_CATEGORIES = "boardCategories";

    public static String boardCategories(List<Board> boardList) {
        ArrayList<String> boardIdList = new ArrayList<String>();
        for (Board item : boardList) {
            boardIdList.add(String.format("\"Board %d\"", item.getId()));
        }
        return Arrays.toString(boardIdList.toArray());
    }

    public static String getEnergyPerTask(List<Board> boards, long startTimeStamp, long endTimeStamp) throws ServletException, JsonProcessingException {
        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        //TODO THINK ABOUT A MORE EFFICIENT WAY OF DOING IT
        List<Message> listMessage = databasePersistenceLayer.getAllMessages(startTimeStamp, endTimeStamp);

        //Task, Board, Energy
        Map<Integer, Map<Integer, Float>> taskToBoard = new TreeMap<Integer, Map<Integer, Float>>();
        ArrayList<Integer> boardsIdList = new ArrayList<Integer>();
        for (Board board : boards) {
            boardsIdList.add(board.getId());
        }
        Collections.sort(boardsIdList);

        for (Message item : listMessage) {
            if (taskToBoard.containsKey(item.getTaskId())) {
                Map<Integer, Float> boardToEnergy = taskToBoard.get(item.getTaskId());
                if (boardToEnergy.containsKey(item.getBoardId())) {
                    Float energy = boardToEnergy.get(item.getBoardId());
                    energy = (energy + item.getSumAllChannels()) / 2;
                    boardToEnergy.put(item.getBoardId(), energy);
                } else {
                    boardToEnergy.put(item.getBoardId(), item.getSumAllChannels());
                }
            } else {
                Map<Integer, Float> boardToEnergy = new HashMap<Integer, Float>();
                boardToEnergy.put(item.getBoardId(), item.getSumAllChannels());
                taskToBoard.put(item.getTaskId(), boardToEnergy);
            }
        }

        List<ChartSeries> chartSeriesList = new ArrayList<ChartSeries>();
        for (Map.Entry<Integer, Map<Integer, Float>> entry : taskToBoard.entrySet()) {
            int task = entry.getKey();
            double energy[] = new double[boards.size()];
            Map<Integer, Float> boardToEnergy = entry.getValue();
            for (Map.Entry<Integer, Float> item : boardToEnergy.entrySet()) {
                if (energy[boardsIdList.indexOf(item.getKey())] == 0) {
                    energy[boardsIdList.indexOf(item.getKey())] += item.getValue();
                } else {
                    energy[boardsIdList.indexOf(item.getKey())] += item.getValue();
                    energy[boardsIdList.indexOf(item.getKey())] /= 2;
                }
            }
            ChartSeries chartSeries = new ChartSeries(String.format("Task %d", task), energy);
            chartSeriesList.add(chartSeries);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(chartSeriesList);
        return jsonInString;
    }
}


