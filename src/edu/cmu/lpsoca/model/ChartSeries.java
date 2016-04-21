package edu.cmu.lpsoca.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by urbano on 4/14/16.
 */
public final class ChartSeries {
    public final String name;
    public final double[] data;

    @JsonCreator
    public ChartSeries(@JsonProperty("name") String name, @JsonProperty("data") double[] data) {
        this.name = name;
        this.data = data;
    }
}