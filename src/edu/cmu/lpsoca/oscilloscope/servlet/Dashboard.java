package edu.cmu.lpsoca.oscilloscope.servlet;

import edu.cmu.lpsoca.oscilloscope.servlet.dashboardImpl.BoardDashboard;
import edu.cmu.lpsoca.oscilloscope.servlet.dashboardImpl.GeneralDashboard;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

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
            BoardDashboard.prepareBoardPage(Integer.valueOf(request.getParameter("board")), request, response, databasePersistenceLayer);
        } else {
            GeneralDashboard.prepareGenericDashboard(request, response, databasePersistenceLayer);
        }

        databasePersistenceLayer.terminate();
    }

    private void setupDatabase() throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }
        databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = new DatabasePersistenceLayer();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }
    }
}
