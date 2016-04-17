package edu.cmu.lpsoca.util;

import edu.cmu.lpsoca.model.Message;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by urbano on 4/14/16.
 */
public class MqttMessageParser {

    //ARsOBxUo/nPKATWzIfo1amufAVDzWENvf0TnAXq0O+g2h3RNAQvcB6dI7UpvAQ2GCgBaRQr3AWMtdNBJpzu+AQdSRbIATjHhAVkYUX0JWT6KAVYrE8xftH5OAA==
    public static List<Message> parseMessage(int boardId, String message) {

        List<Message> messageArrayList = new ArrayList<Message>();
        //10 samples of
        //byte 1 -  Task ID
        //byte 2 - Channel 1
        //byte 2 - Channel 2
        //byte 2 - channel 3
        //byte 2 - channel 4
        byte[] decoded = Base64.getDecoder().decode(message);

        if (decoded.length != 91) {
            throw new IllegalArgumentException("Invalid message format");
        }

        for (int i = 0; i < 9; i++) {
            int taskId = Integer.valueOf(decoded[0 + (9 * i)]);
            int channel1 = Integer.valueOf(((decoded[1 + (9 * i)] << 8) + decoded[2 + (9 * i)]) & 0b0000111111111111);
            int channel2 = Integer.valueOf(((decoded[3 + (9 * i)] << 8) + decoded[4 + (9 * i)]) & 0b0000111111111111);
            int channel3 = Integer.valueOf(((decoded[5 + (9 * i)] << 8) + decoded[6 + (9 * i)]) & 0b0000111111111111);
            int channel4 = Integer.valueOf(((decoded[7 + (9 * i)] << 8) + decoded[8 + (9 * i)]) & 0b0000111111111111);
            messageArrayList.add(new Message(channel1, channel2, channel3, channel4, taskId, boardId));
        }

        return messageArrayList;
    }
}
