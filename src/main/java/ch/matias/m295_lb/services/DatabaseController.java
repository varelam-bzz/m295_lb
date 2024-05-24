package ch.matias.m295_lb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
@Path("/database")
public class DatabaseController {
    private final Logger logger = LogManager.getLogger(GameController.class);

    private final DataSource dataSource;

    @Autowired
    public DatabaseController(JdbcTemplate jdbcTemplate) {
        this.dataSource = jdbcTemplate.getDataSource();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"ADMIN"})
    public Response createTables() {
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("steam.sql"));

            logger.info("Tables created successfully.");
            return Response.status(Response.Status.OK).entity("Tables created successfully!").build();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
