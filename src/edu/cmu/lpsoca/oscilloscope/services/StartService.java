package edu.cmu.lpsoca.oscilloscope.services;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.model.command.Start;
import edu.cmu.lpsoca.oscilloscope.exceptions.BoardNotFoundException;
import edu.cmu.lpsoca.oscilloscope.exceptions.DatabaseConnectionError;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Created by urbano on 4/9/16.
 */
@Path("/board")
@Api(value = "/board")
public class StartService {

    @POST
    @Path("{id}/start")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Start the data sampling into the board"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful starting the board"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 409, message = "Could not find the specified board ID"),
            @ApiResponse(code = 400, message = "Invalid arguments")})
    public boolean start(@PathParam("id") int id) throws BoardNotFoundException, DatabaseConnectionError {
        try {
            DatabasePersistenceLayer databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
            Board board = databasePersistenceLayer.getBoard(id);
            if (board == null) {
                throw new BoardNotFoundException(String.valueOf(id));
            }
            Command start = new Start(board);
            return start.execute();
        } catch (SQLException e) {
            throw new DatabaseConnectionError(e);
        }
    }

}
