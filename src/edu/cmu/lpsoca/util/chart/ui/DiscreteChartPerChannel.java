package edu.cmu.lpsoca.util.chart.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.AnalogToEnergyReader;
import edu.cmu.lpsoca.util.chart.ChartSeries;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by urbano on 4/19/16.
 */
public class DiscreteChartPerChannel {

    public static String getPowerPerChannel(int boardId, long startTimeStamp, long endTimeStamp) throws ServletException, JsonProcessingException {
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
                Float power[] = new Float[4];
                power[0] = message.getChannel1();
                power[1] = message.getChannel2();
                power[2] = message.getChannel3();
                power[3] = message.getChannel4();
                channelToEnergy.add(power);
                taskToChannel.put(message.getTaskId(), channelToEnergy);
            } else {
                List<Float[]> channelToEnergy = taskToChannel.get(message.getTaskId());
                Float power[] = new Float[4];
                power[0] = message.getChannel1();
                power[1] = message.getChannel2();
                power[2] = message.getChannel3();
                power[3] = message.getChannel4();
                channelToEnergy.add(power);
                taskToChannel.put(message.getTaskId(), channelToEnergy);
            }
        }

        List<ChartSeries> chartSeriesList = new ArrayList<ChartSeries>();
        for (Map.Entry<Integer, List<Float[]>> entry : taskToChannel.entrySet()) {
            List<Float[]> powerPerChannelList = entry.getValue();
            double energy[] = new double[4];
            for (Float[] powerPerChannel : powerPerChannelList) {
                energy[0] += AnalogToEnergyReader.convertToPower(powerPerChannel[0], 1);
                energy[1] += AnalogToEnergyReader.convertToPower(powerPerChannel[1], 2);
                energy[2] += AnalogToEnergyReader.convertToPower(powerPerChannel[2], 3);
                energy[3] += AnalogToEnergyReader.convertToPower(powerPerChannel[3], 4);
            }
            energy[0] /= powerPerChannelList.size();
            energy[1] /= powerPerChannelList.size();
            energy[2] /= powerPerChannelList.size();
            energy[3] /= powerPerChannelList.size();

            ChartSeries chartSeries = new ChartSeries(String.format("Task %d", entry.getKey()), energy);
            chartSeriesList.add(chartSeries);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(chartSeriesList);
        return jsonInString;
    }
}
