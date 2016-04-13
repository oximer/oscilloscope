package edu.cmu.lpsoca.model.command;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.mqqt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.nio.ByteBuffer;

/**
 * Created by urbano on 4/9/16.
 */
public class Sample implements Command {

    private Board board;
    private short frequencyKhz;
    private short second;
    private byte[] mMsg;

    private final static String MESSAGE = "*SMP";

    public Sample(Board board, short frequencyKhz, short second) {
        this.board = board;
        this.frequencyKhz = frequencyKhz;
        this.second = second;
        byte[] args1 = ByteBuffer.allocate(2).putShort(frequencyKhz).array();
        byte[] args2 = ByteBuffer.allocate(2).putShort(second).array();
        mMsg = new byte[MESSAGE.getBytes().length + args1.length + args2.length];
        System.arraycopy(MESSAGE.getBytes(), 0, mMsg, 0, MESSAGE.getBytes().length);
        System.arraycopy(args1, 0, mMsg, MESSAGE.getBytes().length, args1.length);
        System.arraycopy(args2, 0, mMsg, MESSAGE.getBytes().length + args1.length, args2.length);
    }

    public String getName() {
        return Sample.class.getSimpleName();
    }

    public boolean execute() {
        try {
            MyMqttClient.getInstance().sendMessage(board.getApplicationId(), String.valueOf(board.getId()), mMsg);
        } catch (MqttException e) {
            return false;
        }
        return true;
    }

}
