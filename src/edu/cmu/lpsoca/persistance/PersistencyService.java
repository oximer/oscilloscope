package edu.cmu.lpsoca.persistance;

import edu.cmu.lpsoca.model.Board;

import java.util.List;

/**
 * Created by urbano on 4/6/16.
 */

interface PersistencyService {
    boolean insertMessage(Board board, String msg);
    int getNumberOfBoards();
    List<String> getMessages(Board board);
    List<Board> getBoards();
}
