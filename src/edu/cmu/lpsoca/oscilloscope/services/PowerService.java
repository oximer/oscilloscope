package edu.cmu.lpsoca.oscilloscope.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.cmu.lpsoca.model.*;
import edu.cmu.lpsoca.persistance.DatabasePersistenceLayer;
import edu.cmu.lpsoca.util.AnalogToEnergyReader;
import edu.cmu.lpsoca.util.chart.ui.DiscreteChartPerChannel;
import edu.cmu.lpsoca.util.chart.ui.DiscreteChartPerTask;
import edu.cmu.lpsoca.util.chart.ui.HistoricalChartPerBoard;
import edu.cmu.lpsoca.util.chart.ui.HistoricalChartPerTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by urbano on 4/19/16.
 */
@Path("/board")
@Api(value = "/PowerService", description = "Offer some service reading")
public class PowerService {

    public static final long SAMPLE_RATE = TimeUnit.SECONDS.toMillis(20);
    public static final long NUMBER_OF_SAMPLES = 10;

    @GET
    @Path("/power")
    @Produces("application/json")
    @ApiOperation(
            value = "Get the power reading for sorted list of all boards",
            response = ChartPoint.class,
            responseContainer = "List"
    )
    public List<ChartPoint> getInstantenousPowerForBoards() throws JsonProcessingException, ServletException {
        Date date = new Date();
        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = new DatabasePersistenceLayer();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        List<Board> boardList = databasePersistenceLayer.getBoards(null);
        HashMap<Integer, ChartPoint> chartPointHashMap = new HashMap<>();
        HashMap<Integer, List<Double>> boardToEnergy = new HashMap<>();

        long start = date.getTime();
        long delay = TimeUnit.SECONDS.toMillis(30);

        //Initialize
        for (Board board : boardList) {
            chartPointHashMap.put(board.getId(), new ChartPoint(0, start - delay, getBoardSeriesName(board.getId())));
            boardToEnergy.put(board.getId(), new ArrayList<>());
        }

        List<Message> listMessage = databasePersistenceLayer.getAllMessages(null, start - SAMPLE_RATE - delay, start);

        for (Message message : listMessage) {
            List<Double> energyReadings = boardToEnergy.get(message.getBoardId());
            energyReadings.add((double) (AnalogToEnergyReader.convertToPower(message.getChannel1(), 1) + AnalogToEnergyReader.convertToPower(message.getChannel2(), 2) +
                    AnalogToEnergyReader.convertToPower(message.getChannel3(), 3) + AnalogToEnergyReader.convertToPower(message.getChannel4(), 4)));
        }

        //Preparing Result
        List<ChartPoint> result = new ArrayList<>();
        for (Board board : boardList) {
            double boardAverage;
            if (boardToEnergy.get(board.getId()).size() != 0) {
                boardAverage = boardToEnergy.get(board.getId()).stream().mapToDouble(a -> a).average().getAsDouble();
            } else {
                boardAverage = 0;
            }
            result.add(new ChartPoint(boardAverage, date.getTime(), getBoardSeriesName(board.getId())));
        }

        databasePersistenceLayer.terminate();
        return result;
    }


    @GET
    @Path("/power/historic")
    @Produces("application/json")
    @ApiOperation(
            value = "Get the historic of the power reading for sorted list of all boards",
            response = NewChartSeries.class,
            responseContainer = "List"
    )
    public List<NewChartSeries> getPowerHistoric() throws JsonProcessingException, ServletException {
        Date date = new Date();
        long start = date.getTime() - (PowerService.SAMPLE_RATE * NUMBER_OF_SAMPLES);
        long end = date.getTime();


        long step = (end - start) / NUMBER_OF_SAMPLES;
        List<String> listOfXIntervals = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SAMPLES; i++) {
            listOfXIntervals.add(String.valueOf(start + (step * i)));
        }

        List<NewChartSeries> result = HistoricalChartPerBoard.getHistoricalPowerPerBoard(start, end, listOfXIntervals);
        return result;
    }

    @GET
    @Path("/power/task")
    @Produces("application/json")
    @ApiOperation(
            value = "Get the energy reading for tasks for all boards",
            response = ChartSeries.class,
            responseContainer = "List",
            notes = "Default is last 10 minutes"
    )
    public List<ChartSeries> getBoardPowerPerTask(@ApiParam(value = "Get a average for the lastMilliseconds") @QueryParam("lastMilliseconds") Long lastMilliseconds) throws JsonProcessingException, ServletException {

        long stopTimeStamp = new Date().getTime();

        if (lastMilliseconds == null || lastMilliseconds == 0) {
            lastMilliseconds = TimeUnit.MINUTES.toMillis(10);
        }
        if (lastMilliseconds == -1) {
            lastMilliseconds = stopTimeStamp;
        }


        long startTimeStamp = stopTimeStamp - lastMilliseconds;

        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = new DatabasePersistenceLayer();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        List<Board> boards = databasePersistenceLayer.getBoards(null);
        databasePersistenceLayer.terminate();

        return DiscreteChartPerTask.getPowerPerTask(boards, startTimeStamp, stopTimeStamp);
    }

    @GET
    @Path("/{boardId}/power/channel")
    @Produces("application/json")
    @ApiOperation(
            value = "Get a power reading for a specific board. It gives you the energy per task organized by channel",
            response = ChartSeries.class
    )
    public List<ChartSeries> getInstantenousChannelReadingPerBoard(@PathParam("boardId") @ApiParam(value = "Use boardId as filter") int boardId, @ApiParam(value = "Get a average for the lastMilliseconds") @QueryParam("lastMilliseconds") Long lastMilliseconds) throws JsonProcessingException, ServletException {
        long stopTimeStamp = new Date().getTime();

        if (lastMilliseconds == null || lastMilliseconds == 0) {
            lastMilliseconds = TimeUnit.MINUTES.toMillis(10);
        }
        if (lastMilliseconds == -1) {
            lastMilliseconds = stopTimeStamp;
        }


        long startTimeStamp = stopTimeStamp - lastMilliseconds;

        return DiscreteChartPerChannel.getPowerPerChannel(boardId, startTimeStamp, stopTimeStamp);
    }

    @GET
    @Path("/{boardId}/power/task")
    @Produces("application/json")
    @ApiOperation(
            value = "Get the current information for power reading for a board organized by task",
            response = ChartSeries.class
    )
    public List<ChartPoint> getInstantenousBoardReadingPerTask(@PathParam("boardId") @ApiParam(value = "Use boardId as filter") int boardId) throws JsonProcessingException, ServletException {
        Date date = new Date();
        DatabasePersistenceLayer databasePersistenceLayer = null;
        try {
            databasePersistenceLayer = new DatabasePersistenceLayer();
        } catch (SQLException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }

        List<Integer> taskList = databasePersistenceLayer.getListOfTasks(boardId);
        HashMap<Integer, ChartPoint> chartPointHashMap = new HashMap<>();
        HashMap<Integer, List<Double>> taskToEnergy = new HashMap<>();

        long start = date.getTime();
        long delay = TimeUnit.SECONDS.toMillis(30);

        //Initialize
        for (Integer task : taskList) {
            chartPointHashMap.put(task, new ChartPoint(0, start - delay, getTaskSeriesName(task)));
            taskToEnergy.put(task, new ArrayList<>());
        }

        List<Message> listMessage = databasePersistenceLayer.getAllMessages(boardId, start - SAMPLE_RATE - delay, start);

        for (Message message : listMessage) {
            List<Double> energyReadings = taskToEnergy.get(message.getTaskId());
            energyReadings.add((double) (AnalogToEnergyReader.convertToPower(message.getChannel1(), 1) + AnalogToEnergyReader.convertToPower(message.getChannel2(), 2) +
                    AnalogToEnergyReader.convertToPower(message.getChannel3(), 3) + AnalogToEnergyReader.convertToPower(message.getChannel4(), 4)));
        }

        //Preparing Result
        List<ChartPoint> result = new ArrayList<>();
        for (Integer task : taskList) {
            double boardAverage;
            if (taskToEnergy.get(task).size() != 0) {
                boardAverage = taskToEnergy.get(task).stream().mapToDouble(a -> a).average().getAsDouble();
            } else {
                boardAverage = 0;
            }
            result.add(new ChartPoint(boardAverage, date.getTime(), getTaskSeriesName(task)));
        }

        databasePersistenceLayer.terminate();
        return result;
    }

    @GET
    @Path("/{boardId}/power/task/historic")
    @Produces("application/json")
    @ApiOperation(
            value = "Get a historical information for power reading for a board organized by task",
            response = ChartSeries.class
    )
    public List<NewChartSeries> getHistoricBoardReadingPerTask(@PathParam("boardId") @ApiParam(value = "Use boardId as filter") int boardId) throws JsonProcessingException, ServletException {
        Date date = new Date();
        long start = date.getTime() - (PowerService.SAMPLE_RATE * NUMBER_OF_SAMPLES);
        long end = date.getTime();


        long step = (end - start) / NUMBER_OF_SAMPLES;
        List<String> listOfXIntervals = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SAMPLES; i++) {
            listOfXIntervals.add(String.valueOf(start + (step * i)));
        }

        return HistoricalChartPerTask.getHistoricalPowerPerTask(boardId, start, end, listOfXIntervals);
    }

    private String getBoardSeriesName(Integer boardId) {
        return String.format("Board %d", boardId);
    }

    private String getTaskSeriesName(Integer boardId) {
        return String.format("Task %d", boardId);
    }
}