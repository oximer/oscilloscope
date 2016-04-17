package edu.cmu.lpsoca.mqqt;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.MqttMessageParser;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import static java.lang.Thread.sleep;

public class MqttPublishSample {

    public static int OSCILLOSCOPE_PORT = 1;

    public static void main(String[] args) throws SQLException {

        String broker = "tcp://localhost:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        final DatabasePersistenceLayer databasePersistanceLayer = DatabasePersistenceLayer.getInstance();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            sampleClient.subscribe("/up/+/" + OSCILLOSCOPE_PORT + "/+");
            sampleClient.setCallback(new MqttCallback() {

                public void connectionLost(Throwable throwable) {

                }

                //Message example: /up/appId/port/board
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("messageArrived() called with " + "s = [" + topic + "], mqttMessage = [" + mqttMessage + "]");
                    StringTokenizer str = new StringTokenizer(topic, "/");
                    if (str.countTokens() != 4)
                        throw new IllegalArgumentException("Invalid message" + String.valueOf(mqttMessage));

                    Board board = new Board();
                    str.nextToken(); //Ignore
                    board.setApplicationId(str.nextToken());
                    str.nextToken(); //Ignore port
                    board.setName(str.nextToken());
                    board.setLastUpdated(new Timestamp(new Date().getTime()));
                    List<Message> messageList = MqttMessageParser.parseMessage(board.getId(), String.valueOf(mqttMessage));
                    for (Message message : messageList) {
                        databasePersistanceLayer.insertMessage(board, message);
                    }
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("deliveryComplete() called with " + "iMqttDeliveryToken = [" + iMqttDeliveryToken + "]");
                }
            });

            try {
                while (true) {
                    sleep(10000000);
                }
            } catch (InterruptedException e) {
                System.out.println(e.getCause());
                e.printStackTrace();
            }

            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}