package service;


import dao.ImageDao;
import dao.MovieDao;
import db.model.Movie;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    @Produces({"image/png"})
    public Response getImage(@PathParam(value = "movieId") long id) {
        ImageDao imageDao = new ImageDao();
        byte[] image = imageDao.getByMovieId(id).getImage();
        return Response.ok().entity(new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                outputStream.write(image);
                outputStream.flush();
            }
        }).build();
    }
   /* @GET
    @Path("/showing")
    @Produces(MediaType.APPLICATION_JSON)
    public Showing getShowingInfo(@QueryParam("showingId")long id) {
        return showingDao.getById(id);
    }*/
}
