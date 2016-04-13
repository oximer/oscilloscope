package edu.cmu.lpsoca.oscilloscope.services;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by urbano on 4/6/16.
 */
@Path("/")
@Api(value = "/board", description = "Endpoint for Employee listing")
public class BoardService {

    @GET
    @Path("/board")
    @Produces("application/json")
    @ApiOperation(
            value = "List all boards",
            response = Board.class,
            responseContainer = "List"
    )
    public List<Board> board() throws SQLException {
        DatabasePersistenceLayer databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
        List<Board> list = databasePersistenceLayer.getBoards();
        databasePersistenceLayer.terminate();
        return list;
    }
}
