package edu.cmu.lpsoca.oscilloscope.servlet.dashboardImpl;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Message;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.chart.PreparePowerChart;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by urbano on 4/20/16.
 */
public class GeneralDashboard {
    public static final String APPLICATION_LIST = "ApplicationList";

    public static void prepareGenericDashboard(HttpServletRequest request, HttpServletResponse response, DatabasePersistenceLayer databasePersistenceLayer) throws ServletException, IOException {
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

        request.setAttribute(APPLICATION_LIST, applicationList);
        String boardCategoriesJson = PreparePowerChart.boardCategories(boards);
        request.setAttribute(PreparePowerChart.BOARD_CATEGORIES, boardCategoriesJson);
        request.getRequestDispatcher("/web/index.jsp").forward(request, response);
    }
}
