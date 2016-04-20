package edu.cmu.lpsoca.oscilloscope.servlet;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.chart.PreparePowerChart;
import edu.cmu.lpsoca.util.chart.ui.DiscreteChartPerChannel;
import edu.cmu.lpsoca.util.chart.ui.DiscreteChartPerTask;
import edu.cmu.lpsoca.util.chart.ui.HistoricalChartPerBoard;
import edu.cmu.lpsoca.util.chart.ui.HistoricalChartPerTask;
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
    private DatabasePersistenceLayer databasePersistenceLayer;
    final Logger logger = Logger.getLogger(Dashboard.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setupDatabase();

        if (request.getParameter("board") != null && !request.getParameter("board").isEmpty()) {
            prepareBoardPage(Integer.valueOf(request.getParameter("board")), request, response, databasePersistenceLayer);
        } else {
            prepareGenericDashboard(request, response, databasePersistenceLayer);
        }

        databasePersistenceLayer.terminate();
    }

    private static void prepareBoardPage(int boardId, HttpServletRequest request, HttpServletResponse response, DatabasePersistenceLayer databasePersistenceLayer) throws ServletException, IOException {
        long startTimeStamp = 0;
        long stopTimeStamp = new Date().getTime();
        List<Board> boards = databasePersistenceLayer.getBoards(null);
        request.setAttribute(BOARD_LIST, boards);
        request.setAttribute(PreparePowerChart.CHANNEL_CATEGORIES, PreparePowerChart.channelCategories());
        request.setAttribute(PreparePowerChart.CHANNEL_ENERGY_SERIES, DiscreteChartPerChannel.getPowerPerChannel(boardId, startTimeStamp, stopTimeStamp));

        long beginXAxis = 1460831775000l;
        long engXAxis = 1460831809000l;
        List<String> listOfXIntervals = PreparePowerChart.getTimeIntervalCategoriesAsList(beginXAxis, engXAxis, 4);
        request.setAttribute(PreparePowerChart.TIME_INTERVAL_CATEGORIES, Arrays.toString(listOfXIntervals.toArray()));
        request.setAttribute(PreparePowerChart.TIME_INTERVAL_SERIES, HistoricalChartPerTask.getHistoricalPowerPerTask(boardId, beginXAxis, engXAxis, listOfXIntervals));

        request.getRequestDispatcher("/web/board.jsp").forward(request, response);
    }

    private static void prepareGenericDashboard(HttpServletRequest request, HttpServletResponse response, DatabasePersistenceLayer databasePersistenceLayer) throws ServletException, IOException {
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
        request.setAttribute(PreparePowerChart.BOARD_CATEGORIES, boardCategoriesJson);
        request.setAttribute(PreparePowerChart.TASK_ENERGY_SERIES, DiscreteChartPerTask.getPowerPerTask(boards, startTimeStamp, stopTimeStamp));

        long beginXAxis = 1460831775000l;
        long engXAxis = 1460831809000l;
        List<String> listOfXIntervals = PreparePowerChart.getTimeIntervalCategoriesAsList(beginXAxis, engXAxis, 4);
        request.setAttribute(PreparePowerChart.TIME_INTERVAL_CATEGORIES, Arrays.toString(listOfXIntervals.toArray()));
        request.setAttribute(PreparePowerChart.TIME_INTERVAL_SERIES, HistoricalChartPerBoard.getHistoricalPowerPerBoard(beginXAxis, engXAxis, listOfXIntervals));

        request.getRequestDispatcher("/web/index.jsp").forward(request, response);
    }

    private void setupDatabase() throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }
        databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }
    }
}
