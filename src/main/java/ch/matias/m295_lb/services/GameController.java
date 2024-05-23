package ch.matias.m295_lb.services;

import ch.matias.m295_lb.models.Game;
import ch.matias.m295_lb.repositories.IGameRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Path("/game")
public class GameController {
    private final Logger logger = LogManager.getLogger(GameController.class);

    private final IGameRepository gameRepository;

    @Autowired
    public GameController(IGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Response ping() {
        logger.info("Endpoint /game/ping is running...");
        return Response.ok("Endpoint /game/ping is running...").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response get() {
        List<Game> games;

        try {
            games = gameRepository.findAll();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        if (games.isEmpty()) {
            logger.info("No games found.");
            return Response.status(Response.Status.NO_CONTENT).entity("No games saved.").build();
        }

        logger.info("Returning all games...");
        return Response.status(Response.Status.OK).entity(games).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getById(@PathParam("id") @Valid Integer id) {
        Optional<Game> game;

        try {
            game = gameRepository.findById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        if (game.isPresent()) {
            logger.info(STR."Returning game with id \{id}.");
            return Response.status(Response.Status.OK).entity(game).build();
        }

        throw new NotFoundException(STR."No game with id \{id} found.");
    }

    private Response saveOrUpdate(Game game) {
        /*if (game.getStartTime().isBefore(game.getEndTime())) {
            throw new BadRequestException("Start date must be before end date.");
        }*/

        try {
            logger.info(STR."Inserting / updating game with id \{game.getId()}.");
            return Response.status(Response.Status.OK)
                    .entity(gameRepository.save(game)).build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @POST
    @Path("/game")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response insert(@Valid Game game) {
        if (gameRepository.findById(game.getId()).isPresent()) {
            logger.warn(STR."Game with id \{game.getId()} already exists.");
            return Response.status(Response.Status.CONFLICT).entity(game).build();
        }
        return saveOrUpdate(game);
    }

    @PUT
    @Path("/game")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response update(@Valid Game game) {
        if (gameRepository.findById(game.getId()).isPresent()) {
            return saveOrUpdate(game);
        }
        throw new NotFoundException(STR."Game with id \{game.getId()} doesn't exist.");
    }

    @DELETE
    @Path("/game/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"ADMIN"})
    public Response delete(@PathParam("id") @Valid Integer id) {
        try {
            if (gameRepository.findById(id).isPresent()) {
                gameRepository.deleteById(id);

                logger.info(STR."Deleting game with id \{id}.");
                return Response.status(Response.Status.OK)
                        .entity(STR."Game with id \{id} deleted.").build();
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        throw new NotFoundException(STR."No game with id \{id} found.");
    }
}
