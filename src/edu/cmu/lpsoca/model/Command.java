package edu.cmu.lpsoca.model;

import javax.servlet.ServletException;

/**
 * Created by urbano on 4/9/16.
 */
public interface Command {
    String getName();
    boolean execute() throws ServletException;
}
