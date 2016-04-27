package edu.cmu.lpsoca.model.command;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;

import javax.servlet.ServletException;
import java.sql.SQLException;

/**
 * Created by urbano on 4/9/16.
 */
public class ClearData implements Command {

    private Board board;

    public ClearData(Board board) {
        this.board = board;
    }

    public String getName() {
        return ClearData.class.getSimpleName();
    }

    public boolean execute() throws ServletException {

        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = new DatabasePersistenceLayer();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        databasePersistenceLayer.deleteAllMessageFromBoard(board.getId());
        databasePersistenceLayer.terminate();
        return true;
    }

}
