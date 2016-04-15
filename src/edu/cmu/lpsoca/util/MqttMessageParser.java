package edu.cmu.lpsoca.util;

import edu.cmu.lpsoca.model.Message;

/**
 * Created by urbano on 4/14/16.
 */
public class MqttMessageParser {

    public static Message parseMessage(int boardId, String message) {
        //TODO ADJUST TO THE REAL MESSAGE
        int taskId = Integer.valueOf(message.charAt(0));
        int channel1 = Integer.valueOf(message.charAt(1));
        int channel2 = Integer.valueOf(message.charAt(2));
        int channel3 = Integer.valueOf(message.charAt(3));
        int channel4 = Integer.valueOf(message.charAt(4));

        return new Message(channel1, channel2, channel3, channel4, taskId, boardId);
    }
}
