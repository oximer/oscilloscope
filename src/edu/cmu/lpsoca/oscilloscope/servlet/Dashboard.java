package edu.cmu.lpsoca.oscilloscope.servlet;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.PreparePowerChart;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by urbano on 4/6/16.
 */
@WebServlet(name = "Dashboard")
public class Dashboard extends HttpServlet {
    public static final String BOARD_LIST = "boardList";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Logger logger = Logger.getLogger(Dashboard.class);
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

        List<Board> boards = databasePersistenceLayer.getBoards(null);
        Map<Board, List<Message>> boardMessageMap = new HashMap<Board, List<Message>>();
        Map<String, List<Board>> applicationList = new HashMap<String, List<Board>>();
        for (Board item : boards) {
            List<Message> messageList = databasePersistenceLayer.getMessages(item);
            boardMessageMap.put(item, messageList);
            if (applicationList.containsKey(item.getApplicationId())) {
                List<Board> boardslist = applicationList.get(item.getApplicationId());
                boardslist.add(item);
            } else {
                List<Board> boardslist = new ArrayList<Board>();
                boardslist.add(item);
                applicationList.put(item.getApplicationId(), boardslist);
            }

        }
        long startTimeStamp = 0;
        long stopTimeStamp = new Date().getTime();

        request.setAttribute("numberBoards", boards.size());
        request.setAttribute(BOARD_LIST, boards);
        request.setAttribute("boardsMessagesMap", boardMessageMap);
        request.setAttribute("ApplicationList", applicationList);
        String boardCategoriesJson = PreparePowerChart.boardCategories(boards);
        String energyTaskSeries = PreparePowerChart.getEnergyPerTask(boards, startTimeStamp, stopTimeStamp);
        logger.info(boardCategoriesJson);
        logger.info(energyTaskSeries);
        request.setAttribute(PreparePowerChart.BOARD_CATEGORIES, boardCategoriesJson);
        request.setAttribute(PreparePowerChart.TASK_ENERGY_SERIES, PreparePowerChart.getEnergyPerTask(boards, startTimeStamp, stopTimeStamp));
        databasePersistenceLayer.terminate();
        request.getRequestDispatcher("/web/index.jsp").forward(request, response);
    }
}
