package edu.cmu.lpsoca.model;

/**
 * Created by urbano on 4/14/16.
 */
public class Message {
    private float channel1;
    private float channel2;
    private float channel3;
    private float channel4;
    private int taskId;
    private int boardId;

    public Message(float channel1, float channel2, float channel3, float channel4, int taskId, int boardId) {
        this.channel1 = channel1;
        this.channel2 = channel2;
        this.channel3 = channel3;
        this.channel4 = channel4;
        this.taskId = taskId;
        this.boardId = boardId;
    }

    public float getChannel1() {
        return channel1;
    }

    public float getChannel2() {
        return channel2;
    }

    public float getChannel3() {
        return channel3;
    }

    public float getChannel4() {
        return channel4;
    }

    public float getSumAllChannels() {
        return channel1 + channel2 + channel3 + channel4;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getBoardId() {
        return boardId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "channel1=" + channel1 +
                ", channel2=" + channel2 +
                ", channel3=" + channel3 +
                ", channel4=" + channel4 +
                ", taskId=" + taskId +
                ", boardId=" + boardId +
                '}';
    }
}
