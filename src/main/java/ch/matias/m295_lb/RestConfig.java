package ch.matias.m295_lb;

import ch.matias.m295_lb.services.DatabaseController;
import ch.matias.m295_lb.services.GameController;
import ch.matias.m295_lb.utils.exceptions.*;
import ch.matias.m295_lb.utils.security.AuthenticationFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/resources")
public class RestConfig extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<>(
                Arrays.asList(
                        AuthenticationFilter.class,
                        GameController.class,
                        DatabaseController.class,
                        NotFoundExceptionHandler.class,
                        ConstraintViolationExceptionHandler.class,
                        BadRequestExceptionHandler.class,
                        InternalServerErrorExceptionHandler.class
                )
        );
    }
}
