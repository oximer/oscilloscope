package edu.cmu.lpsoca.mqqt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by urbano on 4/12/16.
 */
public class MyMqttClient {

    private static MyMqttClient mInstance;
    private MqttClient mClient;
    String broker = "tcp://172.29.93.223:1883";
    String clientId = "Oscilloscope Server";

    public static MyMqttClient getInstance() throws MqttException {
        if (mInstance == null) {
            mInstance = new MyMqttClient();
        }
        return mInstance;
    }

    private MyMqttClient() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        mClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(System.getProperty("MQTT_USERNAME"));
        connOpts.setPassword(System.getProperty("MQTT_PASSWORD").toCharArray());
        mClient.connect(connOpts);
    }

    public void sendMessage(String applicationId, String boardId, String message) throws MqttException {
        sendMessage(applicationId, boardId, message.getBytes());
    }

    public void sendMessage(String applicationId, String boardId, byte[] bytes) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(bytes);
        String topic = String.format("/down/%s/%s/%s/", applicationId, MqttPublishSample.OSCILLOSCOPE_PORT, boardId);
        System.out.println(topic);
        mClient.publish(topic, mqttMessage);
    }

}
