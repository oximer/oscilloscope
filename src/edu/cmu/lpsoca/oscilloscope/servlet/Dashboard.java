package edu.cmu.lpsoca.oscilloscope.servlet;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by urbano on 4/6/16.
 */
@WebServlet(name = "Dashboard")
public class Dashboard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }
        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }
        List<Board> boards = databasePersistenceLayer.getBoards();
        Map<Board, List<String>> boardMessageMap = new HashMap<Board, List<String>>();
        for (Board item : boards) {
            List<String> messageList = databasePersistenceLayer.getMessages(item);
            boardMessageMap.put(item, messageList);
        }
        request.setAttribute("numberBoards", boards.size());
        request.setAttribute("boardsMessagesMap", boardMessageMap);
        databasePersistenceLayer.terminate();
        request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);
    }
}