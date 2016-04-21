package edu.cmu.lpsoca.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by urbano on 4/19/16.
 */
public class ChartPoint {

    @JsonCreator
    public ChartPoint(@JsonProperty("y") double y, @JsonProperty("x") long x, @JsonProperty("serie") String serie) {
        this.y = y;
        this.x = x;
        this.serie = serie;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public double y;
    public long x;
    public String serie;
}