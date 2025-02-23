package nl.politie.politiebureau.exception.provider;

import nl.politie.politiebureau.exception.ErrorResponse;

import javax.annotation.Priority;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;

@Provider
@Priority(Integer.MAX_VALUE)
public class GeneralExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Er heeft een onverwachte fout opgetreden.",
                exception.getMessage() != null ? exception.getMessage() : "Geen details beschikbaar"
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}

