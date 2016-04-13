package edu.cmu.lpsoca.model;

import java.sql.Timestamp;

/**
 * Created by urbano on 4/5/16.
 */
public class Board {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String name = "";
    private String applicationId = "";
    private Timestamp lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (!name.equals(board.name)) return false;
        return applicationId.equals(board.applicationId);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + applicationId.hashCode();
        return result;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
