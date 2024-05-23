package ch.matias.m295_lb.services;

import ch.matias.m295_lb.models.Game;
import ch.matias.m295_lb.models.Publisher;
import ch.matias.m295_lb.repositories.IGameRepository;
import ch.matias.m295_lb.repositories.IPublisherRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Path("/games")
public class GameController {
    private final Logger logger = LogManager.getLogger(GameController.class);

    private final IGameRepository gameRepository;
    private final IPublisherRepository publisherRepository;

    @Autowired
    public GameController(IGameRepository gameRepository, IPublisherRepository publisherRepository) {
        this.gameRepository = gameRepository;
        this.publisherRepository = publisherRepository;
    }

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Response ping() {
        logger.info("Endpoint /games/ping is running...");
        return Response.ok("Endpoint /games/ping is running...").build();
    }

    private List<Game> getAllGames() {
        List<Game> games;

        try {
            games = gameRepository.findAll();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return games;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response get() {
        List<Game> games = getAllGames();

        if (games.isEmpty()) {
            logger.info("No games saved.");
            return Response.status(Response.Status.NO_CONTENT).entity("No games saved.").build();
        }

        logger.info("Returning all games...");
        return Response.status(Response.Status.OK).entity(games).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getCount() {
        List<Game> games = getAllGames();

        return Response.ok(games.size()).build();
    }

    private Optional<Game> getGameById(Integer id) {
        Optional<Game> game;

        try {
            game = gameRepository.findById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return game;
    }

    @GET
    @Path("/exists/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Response getExistsById(@PathParam("id") @Valid Integer id) {
        Optional<Game> game = getGameById(id);

        if (game.isPresent()) {
            logger.info(STR."Game with id \{id} is present.");
            return Response.status(Response.Status.OK).entity(true).build();
        }

        throw new NotFoundException(STR."No game with id \{id} found.");
    }

    @GET
    @Path("/byId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getById(@PathParam("id") @Valid Integer id) {
        Optional<Game> game = getGameById(id);

        if (game.isPresent()) {
            logger.info(STR."Returning game with id \{id}.");
            return Response.status(Response.Status.OK).entity(game).build();
        }

        throw new NotFoundException(STR."No game with id \{id} found.");
    }

    @GET
    @Path("byName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getByName(@PathParam("name") @Valid String name) {
        Optional<Game> game;
        try {
            game = gameRepository.findGameByName(name);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        if (game.isPresent()) {
            logger.info(STR."Found game with name \{name}");
            return Response.status(Response.Status.OK).entity(game).build();
        }

        throw new NotFoundException(STR."No game with name \{name} found.");
    }

    @GET
    @Path("byReleaseDate/{releaseDate}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getByReleaseDate(@PathParam("releaseDate") String releaseDateString) {
        LocalDate releaseDate;
        try {
            releaseDate = LocalDate.parse(releaseDateString);
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format, format should be yyyy-MM-dd");
        }

        Optional<List<Game>> game;
        try {
            game = gameRepository.findGamesByReleaseDate(releaseDate);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        if (game.isPresent()) {
            logger.info(STR."Found game(s) with date \{releaseDateString}");
            return Response.status(Response.Status.OK).entity(game).build();
        }

        throw new NotFoundException(STR."No game(s) with from date \{releaseDateString} found.");
    }

    private Response saveOrUpdate(Game game) {
        Publisher publisher = game.getPublisher();
        String publisherName = publisher.getName();

        Optional<Publisher> existingPublisher = publisherRepository.findPublisherByName(publisherName);
        if (existingPublisher.isEmpty()) {
            try {
                logger.info(STR."Creating publisher with name \{publisherName}");
                publisherRepository.save(publisher);
            } catch(Exception e) {
                throw new InternalServerErrorException(e.getMessage());
            }
        } else {
            game.setPublisher(existingPublisher.get());
        }

        try {
            logger.info(STR."Inserting / updating game with id \{game.getId()}.");
            return Response.status(Response.Status.OK)
                    .entity(gameRepository.save(game)).build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response insert(@Valid Game game) {
        if (gameRepository.findGameByName(game.getName()).isPresent()) {
            logger.warn(STR."Game with name \{game.getName()} already exists.");
            return Response.status(Response.Status.CONFLICT).entity(game).build();
        }
        return saveOrUpdate(game);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response update(@Valid Game game) {
        Optional<Game> existingGame = gameRepository.findGameByName(game.getName());
        if (existingGame.isPresent()) {
            game.setId(existingGame.get().getId());
            return saveOrUpdate(game);
        }
        throw new NotFoundException(STR."Game with name \{game.getName()} doesn't exist.");
    }

    @DELETE
    @Path("/{id}")
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

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response deleteAll() {
        try {
            gameRepository.deleteAll();

            logger.info("Deleted all games.");
            return Response.status(Response.Status.OK).entity("Deleted all games.").build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
