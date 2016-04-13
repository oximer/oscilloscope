package edu.cmu.lpsoca.persistance;

import edu.cmu.lpsoca.model.Board;

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
    private static Connection connection;

    private DatabasePersistenceLayer() {
    }

    /**
     * Single Design Pattern. It return a instance to access that base.
     *
     * @return
     */
    public static DatabasePersistenceLayer getInstance() throws SQLException {
        if (mInstance == null) {
            Properties p = new Properties();
            p.put("user", System.getProperty("DATABASE_USERNAME"));
            p.put("password", System.getProperty("DATABASE_PASSWORD"));


            connection = DriverManager.getConnection(CONNECTION, p);
            connection.setAutoCommit(false);


            mInstance = new DatabasePersistenceLayer();
        }

        return mInstance;
    }

    public boolean insertMessage(Board board, String msg) {
        try {

            int id = getBoardId(board.getName());
            if (id == -1) {
                id = registerBoard(board);
            }

            Calendar calendar = Calendar.getInstance();
            Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
            String sql = " insert into Messages (message, boardId, dateCreated)"
                    + " values (?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString(1, msg);
            preparedStmt.setInt(2, id);
            preparedStmt.setTimestamp(3, timestamp);
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

    private int registerBoard(Board board) {
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

    private int getBoardId(String name) {
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

    public int getNumberOfBoards() {
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

    public List<String> getMessages(Board board) {
        List<String> result = new ArrayList<String>();
        try {
            int boardId = getBoardId(board.getName());
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT message FROM oscilloscope.Messages WHERE boardId = %s", boardId);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
            return result;
        } catch (Exception e) {
            System.out.print("getBoardId" + e.getCause() + e.getMessage());
        }
        return result;
    }

    public List<Board> getBoards() {
        List<Board> result = new ArrayList<Board>();
        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT * FROM oscilloscope.Devices");
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String applicationId = rs.getString(3);
                Timestamp timestamp = (rs.getTimestamp(4));
                Board board = new Board();
                board.setId(id);
                board.setName(name);
                board.setApplicationId(applicationId);
                board.setLastUpdated(timestamp);
                result.add(board);
            }
            rs.close();
            stmt.close();
            return result;
        } catch (Exception e) {
            System.out.print("getBoardId" + e.getCause() + e.getMessage());
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

    public Board getBoard(int id) {
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
}
