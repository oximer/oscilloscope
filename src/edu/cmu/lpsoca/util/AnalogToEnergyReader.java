package edu.cmu.lpsoca.util;

/**
 * Created by urbano on 4/16/16.
 */
public class AnalogToEnergyReader {

    public static float convertToPower(float rawData, int channel) {
        final float vLoad = 3.3f;
        float vRead = ((rawData / 4096f) * 3.3f);
        final float amplifier = 25;
        final float rSense = getRSense(channel);
        float powerDevice = (vLoad * vRead) / (amplifier * rSense);
        return powerDevice;
    }

    public static float getRSense(int channel) {
        switch (channel) {
            case 1:
                return 1.33f;
            case 2:
            case 3:
            case 4:
            default:
                return 0.4f;
        }
    }
}
