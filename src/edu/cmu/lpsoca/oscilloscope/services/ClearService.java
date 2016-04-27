package edu.cmu.lpsoca.oscilloscope.services;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.model.command.ClearData;
import edu.cmu.lpsoca.oscilloscope.exceptions.BoardNotFoundException;
import edu.cmu.lpsoca.oscilloscope.exceptions.DatabaseConnectionError;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.ServletException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.sql.SQLException;

/**
 * Created by urbano on 4/9/16.
 */
@Path("/board")
@Api(value = "/Board")
public class ClearService {

    @POST
    @Path("{id}/clear")
    @Produces("application/json")
    @ApiOperation(
            value = "Clear the whole data sampling available for a board",
            response = boolean.class
    )
    public boolean getById(@PathParam("id") int id) throws BoardNotFoundException, DatabaseConnectionError {
        try {
            DatabasePersistenceLayer databasePersistenceLayer = new DatabasePersistenceLayer();
            Board board = databasePersistenceLayer.getBoard(id);
            if (board == null) {
                throw new BoardNotFoundException(String.valueOf(id));
            }
            Command clearData = new ClearData(board);
            boolean result = clearData.execute();
            databasePersistenceLayer.terminate();
            return result;
        } catch (SQLException e) {
            throw new DatabaseConnectionError(e);
        } catch (ServletException e) {
            throw new DatabaseConnectionError(e);
        }
    }

}
