package edu.cmu.lpsoca.oscilloscope.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.lpsoca.model.ChartPoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Date;

/**
 * Created by urbano on 4/19/16.
 */
@Path("/power")
@Api(value = "/PowerService", description = "Offer some service reading")
public class PowerService {

    @GET
    @Path("{id}")
    @Produces("application/json")
    @ApiOperation(
            value = "Get a power reading for a board",
            response = ChartPoint.class
    )
    public String board(@PathParam("id") @ApiParam(value = "Use boardId as filter") int id) throws JsonProcessingException {
        ChartPoint point = new ChartPoint(0.5, new Date().getTime());

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(point);
        return jsonInString;
    }
}
