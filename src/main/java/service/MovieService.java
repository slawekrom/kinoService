package service;


import dao.MovieDao;
import db.model.Movie;

import javax.jws.WebParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/movies")
public class MovieService {

    MovieDao movieDao = new MovieDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMovieInfo() {
        return "Hello ";
    }
}
