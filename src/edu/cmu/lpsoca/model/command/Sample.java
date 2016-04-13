package edu.cmu.lpsoca.model.command;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.mqqt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by urbano on 4/9/16.
 */
public class Sample implements Command {

    private Board board;
    private int frequencyKhz;
    private int second;

    private static String MESSAGE = "*SMP";

    public Sample(Board board, int frequencyKhz, int second) {
        this.board = board;
        this.frequencyKhz = frequencyKhz;
        this.second = second;
    }

    public String getName() {
        return Sample.class.getSimpleName();
    }

    public boolean execute() {
        try {
            MyMqttClient.getInstance().sendMessage(board.getApplicationId(), String.valueOf(board.getId()), MESSAGE);
        } catch (MqttException e) {
            return false;
        }
        return true;
    }

}
