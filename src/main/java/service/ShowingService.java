package service;

import dao.ReservationDao;
import dao.ShowingDao;
import dao.UserDao;
import db.model.Showing;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    @Path("/showings")
public class ShowingService {

    ShowingDao showingDao = new ShowingDao();
    UserDao userDao = new UserDao();
    ReservationDao reservationDao = new ReservationDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Showing> getShowings() {
        return showingDao.getAll();
    }
    @GET
    @Path("/{showingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Showing getShowingById(@PathParam("showingId") long id) {
        return showingDao.getById(id);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/showing/{year}/{month}/{day}")
    public List<Showing> getShowingsByDate(@PathParam(value = "year") int year, @PathParam(value = "month")int month,
                                           @PathParam(value = "day")int day) {
        return showingDao.getByDate(year, month, day);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/showing")
    public boolean ifPlacesFree(@QueryParam(value = "places") String places, @QueryParam(value = "showingId") long showingId) {
        Showing showing = showingDao.getById(showingId);
        List<String> freePlaces = new ArrayList<>(Arrays.asList(showing.getFreePlaces().split(";")));
        String[] reservedPlaces = places.split(";");
        for (String place : reservedPlaces) {
            if (!freePlaces.contains(place))
                return false;
        }
        return true;
    }
}
