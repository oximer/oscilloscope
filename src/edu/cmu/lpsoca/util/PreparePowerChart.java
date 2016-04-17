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
    public static final String CHANNEL_ENERGY_SERIES = "channelEnergySeries";
    public static final String BOARD_CATEGORIES = "boardCategories";
    public static final String CHANNEL_CATEGORIES = "channelCategories";

    public static String boardCategories(List<Board> boardList) {
        ArrayList<String> boardIdList = new ArrayList<String>();
        for (Board item : boardList) {
            boardIdList.add(String.format("\"Board %d\"", item.getId()));
        }
        return Arrays.toString(boardIdList.toArray());
    }

    public static String channelCategories() {
        ArrayList<String> channelList = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) {
            channelList.add(String.format("\"Channel %d\"", i));
        }
        return Arrays.toString(channelList.toArray());
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
                    energyList.add(getEnergy(item));
                    boardToEnergy.put(item.getBoardId(), energyList);
                } else {
                    ArrayList<Float> energyReadings = new ArrayList<>();
                    energyReadings.add(getEnergy(item));
                    boardToEnergy.put(item.getBoardId(), energyReadings);
                }
            } else {
                Map<Integer, List<Float>> boardToEnergy = new HashMap<Integer, List<Float>>();
                ArrayList<Float> energyReadings = new ArrayList<>();
                energyReadings.add(getEnergy(item));
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

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(chartSeriesList);
        return jsonInString;
    }

    public static String getEnergyPerChannel(int boardId, long startTimeStamp, long endTimeStamp) throws ServletException, JsonProcessingException {
        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        List<Message> listMessage = databasePersistenceLayer.getAllMessages(boardId, startTimeStamp, endTimeStamp);

        HashMap<Integer, List<Float[]>> taskToChannel = new HashMap<>();
        for (Message message : listMessage) {
            if (!taskToChannel.containsKey(message.getTaskId())) {
                List<Float[]> channelToEnergy = new ArrayList<>();
                Float energy[] = new Float[4];
                energy[0] = message.getChannel1();
                energy[1] = message.getChannel2();
                energy[2] = message.getChannel3();
                energy[3] = message.getChannel4();
                channelToEnergy.add(energy);
                taskToChannel.put(message.getTaskId(), channelToEnergy);
            } else {
                List<Float[]> channelToEnergy = taskToChannel.get(message.getTaskId());
                Float energy[] = new Float[4];
                energy[0] = message.getChannel1();
                energy[1] = message.getChannel2();
                energy[2] = message.getChannel3();
                energy[3] = message.getChannel4();
                channelToEnergy.add(energy);
                taskToChannel.put(message.getTaskId(), channelToEnergy);
            }
        }

        List<ChartSeries> chartSeriesList = new ArrayList<ChartSeries>();
        for (Map.Entry<Integer, List<Float[]>> entry : taskToChannel.entrySet()) {
            List<Float[]> energyPerChannelList = entry.getValue();
            double energy[] = new double[4];
            for (Float[] energyPerChannel : energyPerChannelList) {
                energy[0] += AnalogToEnergyReader.convertToEnergy(energyPerChannel[0], 1);
                energy[1] += AnalogToEnergyReader.convertToEnergy(energyPerChannel[1], 2);
                energy[2] += AnalogToEnergyReader.convertToEnergy(energyPerChannel[2], 3);
                energy[3] += AnalogToEnergyReader.convertToEnergy(energyPerChannel[3], 4);
            }
            energy[0] /= energyPerChannelList.size();
            energy[1] /= energyPerChannelList.size();
            energy[2] /= energyPerChannelList.size();
            energy[3] /= energyPerChannelList.size();

            ChartSeries chartSeries = new ChartSeries(String.format("Task %d", entry.getKey()), energy);
            chartSeriesList.add(chartSeries);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(chartSeriesList);
        return jsonInString;
    }

    private static float getEnergy(Message message) {
        float powerChannel1 = AnalogToEnergyReader.convertToEnergy(message.getChannel1(), 1);
        float powerChannel2 = AnalogToEnergyReader.convertToEnergy(message.getChannel2(), 2);
        float powerChannel3 = AnalogToEnergyReader.convertToEnergy(message.getChannel3(), 3);
        float powerChannel4 = AnalogToEnergyReader.convertToEnergy(message.getChannel4(), 4);

        float totalEnergy = powerChannel1 + powerChannel2 + powerChannel3 + powerChannel4;
        return totalEnergy;
    }
}


