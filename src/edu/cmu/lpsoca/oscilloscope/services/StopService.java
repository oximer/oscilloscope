package edu.cmu.lpsoca.oscilloscope.services;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.model.command.Stop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by urbano on 4/9/16.
 */
@Path("/board")
@Api(value = "/board")
public class StopService {

    @POST
    @Path("{id}/stop")
    @Produces("application/json")
    @ApiOperation(
            value = "Stop the data sampling into the board",
            response = boolean.class
    )
    public boolean getById(@PathParam("id") int id) {
        Board board = new Board();
        board.setId(id);
        board.setApplicationId("1");
        Command start = new Stop(board);
        return start.execute();
    }

}
