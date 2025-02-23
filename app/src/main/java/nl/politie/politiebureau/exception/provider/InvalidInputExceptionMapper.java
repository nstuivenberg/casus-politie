package nl.politie.politiebureau.exception.provider;

import nl.politie.politiebureau.exception.ErrorResponse;
import nl.politie.politiebureau.exception.InvalidInputException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {
    @Override
    public Response toResponse(InvalidInputException exception) {
        ErrorResponse errorResponse = (ErrorResponse) exception.getResponse().getEntity();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }
}

