package edu.cmu.lpsoca.model;

/**
 * Created by urbano on 4/9/16.
 */
public interface Command {
    String getName();
    boolean execute();
}
