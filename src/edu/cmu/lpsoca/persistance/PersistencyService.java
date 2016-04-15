package edu.cmu.lpsoca.persistance;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;

import java.util.List;

/**
 * Created by urbano on 4/6/16.
 */

interface PersistencyService {
    boolean insertMessage(Board board, Message msg);

    int getNumberOfBoards();

    List<Message> getMessages(Board board);

    List<Board> getBoards(String applicationId);

    Board getBoard(int id);
}
