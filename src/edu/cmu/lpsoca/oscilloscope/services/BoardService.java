package edu.cmu.lpsoca.oscilloscope.services;

import edu.cmu.lpsoca.model.Board;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by urbano on 4/6/16.
 */
@Path("/board")
@Api(value = "/board", description = "Endpoint for Employee listing")
public class BoardService {


    @GET
    @Path("/{id}")
    @Produces("application/json")
    @ApiOperation(
            value = "Get a specific board",
            response = Board.class
    )
    public Board board(@ApiParam(value = "Use applicationId as filter") @PathParam("id") int boardId) throws SQLException {
        DatabasePersistenceLayer databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
        Board board = databasePersistenceLayer.getBoard(boardId);
        databasePersistenceLayer.terminate();
        return board;
    }

    @GET
    @Path("/")
    @Produces("application/json")
    @ApiOperation(
            value = "List all boards filter by applicationId",
            response = Board.class,
            responseContainer = "List",
            notes = "Leave blank the applicationId to not use filter"
    )
    public List<Board> board(@ApiParam(value = "Use applicationId as filter") @QueryParam("applicationId") String applicationId) throws SQLException {
        DatabasePersistenceLayer databasePersistenceLayer = DatabasePersistenceLayer.getInstance();
        String appId = (applicationId == null || applicationId.isEmpty()) ? null : applicationId;
        List<Board> list = databasePersistenceLayer.getBoards(appId);
        databasePersistenceLayer.terminate();
        return list;
    }
}
