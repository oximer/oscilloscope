package edu.cmu.lpsoca.persistance;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/**
 * Created by urbano on 4/6/16.
 */
public class DatabasePersistenceLayer implements PersistencyService {

    private static DatabasePersistenceLayer mInstance;
    private static final String CONNECTION = "jdbc:mysql://aa1kkwxqq0sjjq4.chj4zgaq2k1z.us-west-2.rds.amazonaws.com:3306/oscilloscope";
    private Connection connection;

    public DatabasePersistenceLayer() throws SQLException {
        Properties p = new Properties();
        p.put("user", System.getProperty("DATABASE_USERNAME"));
        p.put("password", System.getProperty("DATABASE_PASSWORD"));


        connection = DriverManager.getConnection(CONNECTION, p);
        connection.setAutoCommit(false);
    }

    public synchronized boolean insertMessage(Board board, Message msg, int sampleId) {
        try {

            int id = getBoardId(board.getName());
            if (id == -1) {
                id = registerBoard(board);
            }

            Calendar calendar = Calendar.getInstance();
            Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
            String sql = " insert into Messages (boardId, taskId, sampleId, channel1, channel2, channel3, channel4, dateCreated)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setInt(1, id);
            preparedStmt.setInt(2, msg.getTaskId());
            preparedStmt.setInt(3, sampleId);
            preparedStmt.setFloat(4, msg.getChannel1());
            preparedStmt.setFloat(5, msg.getChannel2());
            preparedStmt.setFloat(6, msg.getChannel3());
            preparedStmt.setFloat(7, msg.getChannel4());
            preparedStmt.setTimestamp(8, timestamp);
            System.out.println(preparedStmt.toString());
            preparedStmt.execute();
            preparedStmt.close();
            connection.commit();
            return true;
        } catch (Exception e) {
            System.out.print("insertMessage" + e.getCause() + e.getMessage());
        }
        return false;
    }

    public synchronized int getNumberOfMessageSamples() {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT MAX(SampleId) As max FROM oscilloscope.Messages;");
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int number = rs.getInt("max");
            rs.close();
            stmt.close();
            return number;
        } catch (Exception e) {
            System.out.print("getNumberOfMessageSamples" + e.getCause() + e.getMessage());
        }
        return -1;
    }

    private synchronized int registerBoard(Board board) {
        try {
            String sql = "insert into Devices (name, applicationId)"
                    + " values (?, ?);";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, board.getName());
            preparedStmt.setString(2, board.getApplicationId());
            preparedStmt.executeUpdate();
            ResultSet tableKeys = preparedStmt.getGeneratedKeys();
            tableKeys.next();
            int id = tableKeys.getInt(1);
            tableKeys.close();
            preparedStmt.close();
            connection.commit();
            return id;
        } catch (Exception e) {
            System.out.print("registerBoard - " + e.getCause() + e.getMessage());
        }
        return -1;
    }

    private synchronized int getBoardId(String name) {
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT id FROM oscilloscope.Devices WHERE name = \"%s\"", name);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int number = rs.getInt("id");
            rs.close();
            stmt.close();
            return number;
        } catch (Exception e) {
            System.out.print("getBoardId" + e.getCause() + e.getMessage());
        }
        return -1;
    }

    public synchronized int getNumberOfBoards() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT COUNT(*) AS total FROM Devices";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int number = rs.getInt("total");
            rs.close();
            stmt.close();
            return number;
        } catch (Exception e) {
            System.out.print("getNumberOfBoards" + e.getCause() + e.getMessage());
        }
        return -1;
    }

    public synchronized List<Message> getMessages(Board board) {
        List<Message> result = new ArrayList<Message>();
        try {
            int boardId = getBoardId(board.getName());
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM oscilloscope.Messages WHERE boardId = %s", boardId);
            ResultSet rs = stmt.executeQuery(sql);
            result = getMessageFromSelectResultOnMessageTable(rs);
            rs.close();
            stmt.close();
            return result;
        } catch (Exception e) {
            System.out.print("getMessages" + e.getCause() + e.getMessage());
        }
        return result;
    }

    public synchronized List<Board> getBoards(String applicationId) {
        List<Board> result = new ArrayList<Board>();
        try {
            Statement stmt = connection.createStatement();
            String sql;
            if (applicationId == null || applicationId.isEmpty()) {
                sql = String.format("SELECT * FROM oscilloscope.Devices ORDER BY id");
            } else {
                sql = String.format("SELECT * FROM oscilloscope.Devices ORDER BY id where applicationId = \"%s\"", applicationId);
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String appId = rs.getString(3);
                Timestamp timestamp = (rs.getTimestamp(4));
                Board board = new Board();
                board.setId(id);
                board.setName(name);
                board.setApplicationId(appId);
                board.setLastUpdated(timestamp);
                result.add(board);
            }
            rs.close();
            stmt.close();
            return result;
        } catch (Exception e) {
            System.out.print("getBoards" + e.getCause() + e.getMessage());
        }
        return result;
    }

    public void terminate() {
        mInstance = null;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized Board getBoard(int id) {
        Board result = null;
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM oscilloscope.Devices where id = %d", id);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int boardId = rs.getInt(1);
                String name = rs.getString(2);
                String applicationId = rs.getString(3);
                Timestamp timestamp = (rs.getTimestamp(4));
                Board board = new Board();
                board.setId(boardId);
                board.setName(name);
                board.setApplicationId(applicationId);
                board.setLastUpdated(timestamp);
                result = board;
            }
            rs.close();
            stmt.close();
            return result;
        } catch (Exception e) {
            System.out.print("getBoardId" + e.getCause() + e.getMessage());
        }
        return result;
    }

    public synchronized List<Message> getAllMessages(long startTimeStamp, long endTimeStamp) {
        return getAllMessages(null, startTimeStamp, endTimeStamp);
    }

    public synchronized List<Message> getAllMessages(Integer boardId, long startTimeStamp, long endTimeStamp) {
        List<Message> result = new ArrayList<Message>();
        try {
            Statement stmt = connection.createStatement();
            String sql;
            if (boardId != null) {
                sql = String.format("SELECT * FROM oscilloscope.Messages where boardId = \"%s\" AND dateCreated > \"%s\" AND dateCreated < \"%s\"", boardId, new Timestamp(startTimeStamp), new Timestamp(endTimeStamp));
            } else {
                sql = String.format("SELECT * FROM oscilloscope.Messages where dateCreated > \"%s\" AND dateCreated < \"%s\"", new Timestamp(startTimeStamp), new Timestamp(endTimeStamp));
            }
            ResultSet rs = stmt.executeQuery(sql);
            result = getMessageFromSelectResultOnMessageTable(rs);
            rs.close();
            stmt.close();
            return result;
        } catch (Exception e) {
            System.out.print("getAllMessages" + e.getCause() + e.getMessage());
        }
        return result;
    }

    public synchronized int getNumberOfTasks(Integer boardId) {
        int numberOfTasks = -1;
        try {
            Statement stmt = connection.createStatement();
            String sql;
            if (boardId != null) {
                sql = String.format("SELECT COUNT(DISTINCT taskId) FROM oscilloscope.Messages where boardId = \"%s\"", boardId);
            } else {
                sql = String.format("SELECT COUNT(DISTINCT taskId) FROM oscilloscope.Messages");
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                numberOfTasks = rs.getInt(1);
            }
            rs.close();
            stmt.close();
            return numberOfTasks;
        } catch (Exception e) {
            System.out.print("getAllMessages" + e.getCause() + e.getMessage());
        }
        return numberOfTasks;
    }

    public synchronized List<Integer> getListOfTasks(Integer boardId) {
        List<Integer> numberOfTasks = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String sql;
            if (boardId != null) {
                sql = String.format("SELECT DISTINCT taskId FROM oscilloscope.Messages where boardId = \"%s\"", boardId);
            } else {
                sql = String.format("SELECT DISTINCT taskId FROM oscilloscope.Messages");
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                numberOfTasks.add(rs.getInt(1));
            }
            rs.close();
            stmt.close();
            return numberOfTasks;
        } catch (Exception e) {
            System.out.print("getAllMessages" + e.getCause() + e.getMessage());
        }
        return numberOfTasks;
    }

    private synchronized List<Message> getMessageFromSelectResultOnMessageTable(ResultSet rs) throws SQLException {
        List<Message> result = new ArrayList<Message>();
        while (rs.next()) {
            int messageBoardId = rs.getInt(2);
            int taskId = rs.getInt(3);
            float channel1 = rs.getFloat(5);
            float channel2 = rs.getFloat(6);
            float channel3 = rs.getFloat(7);
            float channel4 = rs.getFloat(8);
            Message message = new Message(channel1, channel2, channel3, channel4, taskId, messageBoardId);
            result.add(message);
        }
        return result;
    }

    public int updateBoardLastUpdateTime(Board board) {
        Calendar calendar = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
        int result = -1;
        int boardId = getBoardId(board.getName());
        if (boardId == -1) return -1;
        try {
            String query = "UPDATE oscilloscope.Devices SET lastUpdated = ? where id = ?;";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setTimestamp(1, timestamp);
            preparedStmt.setInt(2, boardId);
            result = preparedStmt.executeUpdate();
            preparedStmt.close();
            connection.commit();
        } catch (Exception e) {
            return result;
        }
        return result;
    }


    public int deleteAllMessageFromBoard(int boardId) {
        int result = -1;
        try {
            String query = "DELETE FROM oscilloscope.Messages WHERE boardId = ? LIMIT 1000;";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, boardId);
            result = preparedStmt.executeUpdate();
            preparedStmt.close();
            connection.commit();
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public int deleteBoard(int boardId) {
        int result = -1;
        try {
            String query = "DELETE FROM oscilloscope.Devices WHERE id = ?;";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, boardId);
            result = preparedStmt.executeUpdate();
            preparedStmt.close();
            connection.commit();
        } catch (Exception e) {
            return result;
        }
        return result;
    }
}
