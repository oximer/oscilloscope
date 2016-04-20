package edu.cmu.lpsoca.util.chart;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.util.AnalogToEnergyReader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by urbano on 4/14/16.
 */
public class PreparePowerChart {

    public static final String TASK_ENERGY_SERIES = "taskEnergySeries";
    public static final String CHANNEL_ENERGY_SERIES = "channelEnergySeries";
    public static final String BOARD_CATEGORIES = "boardCategories";
    public static final String CHANNEL_CATEGORIES = "channelCategories";
    public static final String TIME_INTERVAL_CATEGORIES = "timeIntervalCategories";
    public static final String TIME_INTERVAL_SERIES = "timeIntervalSeries";

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

    public static String getTimeIntervalCategories(long startTimeStamp, long endTimeStamp, int numberSteps) {
        ArrayList<String> timeInterval = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long step = (endTimeStamp - startTimeStamp) / numberSteps;
        for (int i = 0; i <= numberSteps; i++) {
            timeInterval.add(String.format("\'%s\'", sdf.format(new Date(startTimeStamp + (step * i)))));
        }
        return Arrays.toString(timeInterval.toArray());
    }

    public static List<String> getTimeIntervalCategoriesAsList(long startTimeStamp, long endTimeStamp, int numberSteps) {
        ArrayList<String> timeInterval = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long step = (endTimeStamp - startTimeStamp) / numberSteps;
        for (int i = 0; i <= numberSteps; i++) {
            timeInterval.add(String.format("\'%s\'", sdf.format(new Date(startTimeStamp + (step * i)))));
        }
        return timeInterval;
    }

    public static float getPower(Message message) {
        float powerChannel1 = AnalogToEnergyReader.convertToPower(message.getChannel1(), 1);
        float powerChannel2 = AnalogToEnergyReader.convertToPower(message.getChannel2(), 2);
        float powerChannel3 = AnalogToEnergyReader.convertToPower(message.getChannel3(), 3);
        float powerChannel4 = AnalogToEnergyReader.convertToPower(message.getChannel4(), 4);

        float totalEnergy = powerChannel1 + powerChannel2 + powerChannel3 + powerChannel4;
        return totalEnergy;
    }
}


