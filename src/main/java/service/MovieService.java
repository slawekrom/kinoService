package service;


import dao.ImageDao;
import dao.MovieDao;
import db.model.Movie;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Path("/movies")
public class MovieService {

    MovieDao movieDao = new MovieDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getMovies() {
        return movieDao.getAll();
    }

    @GET
    @Path("/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Movie getMovieById(@PathParam("movieId") long id) {
        return movieDao.getById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/title/{title}")
    public Movie getMovieByTitle(@PathParam("title") String title) {
        return movieDao.getByTitle(title);
    }

    @GET
    @Path("/image/{movieId}")
    @Produces("image/png")
    public Image getImage(@PathParam(value = "movieId") long id) {
        Image image = null;
        ImageDao imageDao = new ImageDao();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageDao.getByMovieId(id).getImage());
        try {
            image = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
        /*ImageDao imageDao = new ImageDao();
        return imageDao.getByMovieId(id).getImage();*/
    }
   /* @GET
    @Path("/showing")
    @Produces(MediaType.APPLICATION_JSON)
    public Showing getShowingInfo(@QueryParam("showingId")long id) {
        return showingDao.getById(id);
    }*/
}
