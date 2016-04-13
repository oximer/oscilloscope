package edu.cmu.lpsoca.model.command;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.mqqt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by urbano on 4/9/16.
 */
public class Stop implements Command {

    private Board board;
    private static String MESSAGE = "*STP";

    public Stop(Board board) {
        this.board = board;
    }

    public String getName() {
        return Stop.class.getSimpleName();
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
