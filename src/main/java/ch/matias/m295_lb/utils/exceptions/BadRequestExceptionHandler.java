package ch.matias.m295_lb.utils.exceptions;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {
    private final Logger logger = LogManager.getLogger(BadRequestExceptionHandler.class);

    @Override
    public Response toResponse(BadRequestException exception) {
        logger.error(exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
