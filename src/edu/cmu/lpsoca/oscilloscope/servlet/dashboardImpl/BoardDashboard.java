package edu.cmu.lpsoca.oscilloscope.servlet.dashboardImpl;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.chart.PreparePowerChart;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by urbano on 4/20/16.
 */
public class BoardDashboard {
    public static final String BOARDID = "BoardId";

    public static void prepareBoardPage(int boardId, HttpServletRequest request, HttpServletResponse response, DatabasePersistenceLayer databasePersistenceLayer) throws ServletException, IOException {
        List<Board> boards = databasePersistenceLayer.getBoards(null);
        request.setAttribute(BOARDID, boardId);
        request.setAttribute(PreparePowerChart.CHANNEL_CATEGORIES, PreparePowerChart.channelCategories());

        request.getRequestDispatcher("/web/board.jsp").forward(request, response);
    }
}
