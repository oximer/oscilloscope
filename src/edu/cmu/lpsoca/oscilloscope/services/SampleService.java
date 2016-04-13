package edu.cmu.lpsoca.oscilloscope.services;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.model.command.Sample;
import edu.cmu.lpsoca.oscilloscope.exceptions.BoardNotFoundException;
import edu.cmu.lpsoca.oscilloscope.exceptions.DatabaseConnectionError;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import io.swagger.annotations.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.sql.SQLException;

/**
 * Created by urbano on 4/9/16.
 */
@Path("/board")
@Api(value = "/board")
public class SampleService {

    @POST
    @Path("{id}/sample")
    @ApiOperation(
            value = "Start the data sampling into the board"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful starting the board"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 409, message = "Could not find the specified board ID"),
            @ApiResponse(code = 400, message = "Invalid arguments")})
    public boolean sample(@PathParam("id") int id, @ApiParam(value = "Frequency sampling between 1:100") @QueryParam("frequency") byte frequency, @ApiParam(value = "Sample time in seconds") @QueryParam("seconds") byte seconds) throws BoardNotFoundException, DatabaseConnectionError {
        try {
            DatabasePersistenceLayer databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
            Board board = databasePersistenceLayer.getBoard(id);
            if (board == null) {
                throw new BoardNotFoundException(String.valueOf(id));
            }
            Command sample = new Sample(board, frequency, seconds);
            return sample.execute();
        } catch (SQLException e) {
            throw new DatabaseConnectionError(e);
        }
    }

}
