package edu.cmu.lpsoca.oscilloscope.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DatabaseConnectionError extends Exception implements
        ExceptionMapper<DatabaseConnectionError> {
    private static final long serialVersionUID = 1L;

    public DatabaseConnectionError() {
        super(DatabaseConnectionError.class.getSimpleName());
    }

    public DatabaseConnectionError(Exception e) {
        super(e.getMessage());
    }

    public Response toResponse(DatabaseConnectionError exception) {
        ObjectMapper mapper = new ObjectMapper();
        String msg = " ";
        try {
            msg = mapper.writeValueAsString(getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.status(500).entity(exception.getMessage())
                .type("application/json").build();
    }
}