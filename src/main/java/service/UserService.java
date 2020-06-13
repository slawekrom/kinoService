package service;

import dao.ReservationDao;
import dao.UserDao;
import db.model.Reservation;
import db.model.User;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Base64;
import java.util.List;

@Path("/user")
public class UserService {

    UserDao userDao = new UserDao();
    ReservationDao reservationDao = new ReservationDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return userDao.getAll();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("userId") long id) {
        return userDao.getById(id);
    }

    @GET
    @Path("/{userId}/reservations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservation> getUserReservations(@PathParam(value = "userId") long userId) {
        return reservationDao.getUserReservation(userId);
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(User user) {
        User newUser = new User(user.getFirstName(), user.getSecondName(), user.getPesel(), encode(user.getPassword()));
        userDao.save(newUser);
    }
    @GET
    @Path("/pesel/{pesel}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean checkIfUserExist(@PathParam(value = "pesel") String pesel) {
        return userDao.getByPesel(pesel) != null;
    }
    @GET
    @Path("/pesel")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByPesel(@QueryParam(value = "pesel") String pesel) {
        return userDao.getByPesel(pesel);
    }
    @GET
    @Path("/reservation/{pesel}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservation> getUserReservationsByPesel(@PathParam(value = "pesel") String pesel) {
        return reservationDao.getReservationByPesel(pesel);
    }
    @GET
    @Path("/reservation")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservation> getUserReservationsByName(@QueryParam(value = "firstName")String firstName,
                                                       @QueryParam(value = "secondName")String secondName) {
        return reservationDao.getReservationByName(firstName, secondName);
    }
    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeUser(@PathParam("userId") long id) {
        userDao.delete(userDao.getById(id));
    }

    @GET
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean authorize(@QueryParam(value = "pesel")String pesel, @QueryParam(value = "password")String password) {
        User user = userDao.getByPesel(pesel);
        if (user == null)
            return false;
        else return password.equals(decode(user.getPassword()));
    }

    private String encode(String password){
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
    private String decode(String password){
        byte[] decodedBytes = Base64.getDecoder().decode(password);
        String decodedPassword = new String(decodedBytes);
        return decodedPassword;
    }
}
