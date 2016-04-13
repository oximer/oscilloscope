package edu.cmu.lpsoca.persistance;

import edu.cmu.lpsoca.model.Board;

import java.util.*;

/**
 * Created by urbano on 4/5/16.
 */
public class TemporaryPersistanceLayer implements PersistencyService {

    private static TemporaryPersistanceLayer mInstance;
    private LinkedHashMap<Board, Queue<String>> mBoardLinkedList;


    private TemporaryPersistanceLayer() {
        mBoardLinkedList = new LinkedHashMap<Board, Queue<String>>();
    }

    public static TemporaryPersistanceLayer getInstance() {
        if (mInstance == null) {
            mInstance = new TemporaryPersistanceLayer();
        }
        return mInstance;
    }

    public synchronized boolean insertMessage(Board board, String msg) {
        if (!mBoardLinkedList.containsKey(board)) mBoardLinkedList.put(board, new LinkedList<String>());

        Queue<String> stringArrayList = mBoardLinkedList.get(board);
        if (stringArrayList.size() > 5) {
            stringArrayList.poll();
        }
        stringArrayList.add(msg);

        mBoardLinkedList.replace(board, stringArrayList);
        return true;
    }

    public int getNumberOfBoards() {
        return mBoardLinkedList.keySet().size();
    }

    public List<String> getMessages(Board board) {
        //TODO implement it.
        return new ArrayList<String>();
    }

    public List<Board> getBoards(String applicationId) {
        //TODO implement it.
        return new ArrayList<Board>();
    }

    public Board getBoard(int id) {
        return null;
    }

    @Override
    public String toString() {
        return "TemporaryPersistanceLayer{" +
                "mBoardLinkedList=" + mBoardLinkedList +
                '}';
    }
}
