package edu.cmu.lpsoca.oscilloscope.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BoardNotFoundException extends Exception implements
        ExceptionMapper<BoardNotFoundException> {
    private static final long serialVersionUID = 1L;

    public BoardNotFoundException() {
        super(BoardNotFoundException.class.getSimpleName());
    }

    public BoardNotFoundException(String msg) {
        super(msg);
    }

    public Response toResponse(BoardNotFoundException exception) {
        ObjectMapper mapper = new ObjectMapper();
        String msg = " ";
        try {
            msg = mapper.writeValueAsString(getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.status(409).entity(exception.getMessage())
                .type("application/json").build();
    }
}