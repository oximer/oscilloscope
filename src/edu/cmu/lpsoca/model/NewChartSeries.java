package edu.cmu.lpsoca.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by urbano on 4/14/16.
 */
public final class NewChartSeries {
    public final String name;
    public final double[] y;
    public final String[] x;


    @JsonCreator
    public NewChartSeries(@JsonProperty("name") String name, @JsonProperty("y") double[] y, @JsonProperty("x") String[] x) {
        this.name = name;
        this.y = y;
        this.x = x;
    }
}