package edu.cmu.lpsoca.oscilloscope.services;

import edu.cmu.lpsoca.model.Command;
import edu.cmu.lpsoca.model.command.Sample;
import edu.cmu.lpsoca.model.command.Start;
import edu.cmu.lpsoca.model.command.Stop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by urbano on 4/6/16.
 */
@Path("/")
@Api(value = "/commandList", description = "List all command available")
public class CommandService {

    @GET
    @Path("/commandList")
    @Produces("application/json")
    @ApiOperation(
            value = "List all command available",
            response = Command.class,
            responseContainer = "List"
    )
    public List<String> board() {
        List<String> commandList = new ArrayList<String>();
        commandList.add(Start.class.getSimpleName());
        commandList.add(Stop.class.getSimpleName());
        commandList.add(Sample.class.getSimpleName());
        return commandList;
    }
}
