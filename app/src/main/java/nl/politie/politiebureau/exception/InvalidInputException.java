package nl.politie.politiebureau.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InvalidInputException extends WebApplicationException {
    public InvalidInputException(String message, String details) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        message,
                        details
                ))
                .build());
    }
}


