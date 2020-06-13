package service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ReservationDao;
import dao.ShowingDao;
import dao.UserDao;
import db.model.Reservation;
import db.model.Showing;
import db.model.User;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/reservations")
public class ReservationService {

    ReservationDao reservationDao = new ReservationDao();
    ShowingDao showingDao = new ShowingDao();
    UserDao userDao = new UserDao();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservation> getReservations(){
        return reservationDao.getAll();
    }
    @GET
    @Path("/{reservationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Reservation getReservationById(@PathParam(value = "reservationId") long id){
        return reservationDao.getById(id);
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addNewReservation(ReservationParam reservationParam) {
        User user = userDao.getById(reservationParam.getUserId());
        Showing showing = showingDao.getById(reservationParam.getShowingId());
        showing.setOccupiedPlaces(addReservedPlaces(reservationParam.getPlaces(), showing.getOccupiedPlaces()));
        showing.setFreePlaces(removeFreePlaces(reservationParam.getPlaces(), showing.getFreePlaces()));
        showingDao.update(showing);
        Reservation reservation = new Reservation(reservationParam.getPlaces(), reservationParam.isPaid(), user, showing);
        reservationDao.save(reservation);
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{reservationId}")
    public void editReservation(ReservationParam reservation, @PathParam("reservationId") long id) {
        Reservation newReservation = reservationDao.getById(id);
        Showing showing = showingDao.getById(reservation.getShowingId());
        updateShowingPlaces(reservation.getPlaces(), showing);
        showing.setOccupiedPlaces(addReservedPlaces(reservation.getPlaces(), showing.getOccupiedPlaces()));
        showing.setFreePlaces(removeFreePlaces(reservation.getPlaces(), showing.getFreePlaces()));
        showingDao.update(showing);
        newReservation.setIsPaid(reservation.isPaid());
        newReservation.setPlaces(reservation.getPlaces());
        reservationDao.update(newReservation);
    }
    @DELETE
    @Path("/{reservationId}")
    public void deleteReservation(@PathParam(value = "reservationId") long id) {
        Reservation reservation = reservationDao.getById(id);
        Showing showing = reservation.getShowing();
        updateShowingPlaces(reservation.getPlaces(), showing);
        reservationDao.delete(reservationDao.getById(id));
    }

    @GET
    @Path("/pdf/{reservationId}")
    @Produces()
    public byte[] getPDFofReservation(@PathParam(value = "reservationId") long id) throws IOException, DocumentException {
        Reservation result = reservationDao.getById(id);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("resources/" + id + "_reservation.pdf"));
        document.open();
        Chunk chunk = new Chunk("Person name and surname: " + result.getUser().getFirstName() + " " + result.getUser().getSecondName());
        document.add(chunk);
        document.add(new Paragraph());
        chunk = new Chunk("Movie title: " + result.getShowing().getMovie().getTitle());
        document.add(chunk);
        document.add(new Paragraph());
        chunk = new Chunk("Date of screening: " + result.getShowing().getDate());
        document.add(chunk);
        document.add(new Paragraph());
        chunk = new Chunk("Places reserved: " + result.getPlaces());
        document.add(chunk);
        document.add(new Paragraph());
        document.close();
        File f = new File("/" + id + "_reservation.pdf");
        return Files.readAllBytes(f.toPath());
    }
    private String addReservedPlaces(String reservedPlaces, String occupiedPlaces) {
        List<String> placesAsList = new ArrayList<>(Arrays.asList(occupiedPlaces.split(";")));
        placesAsList.addAll(Arrays.asList(reservedPlaces.split(";")));
        return String.join(";", placesAsList);
    }

    private void updateShowingPlaces(String reservedPlaces, Showing showing) {
        List<String> placesAsList = new ArrayList<>(Arrays.asList(showing.getFreePlaces().split(";")));
        placesAsList.addAll(Arrays.asList(reservedPlaces.split(";")));
        showing.setFreePlaces(String.join(";", placesAsList));

        List<String> occupiedPlacesAsList = new ArrayList<>(Arrays.asList(showing.getOccupiedPlaces().split(";")));
        occupiedPlacesAsList.removeAll(Arrays.asList(reservedPlaces.split(";")));
        showing.setOccupiedPlaces(String.join(";", occupiedPlacesAsList));
        showingDao.update(showing);
    }

    private String removeFreePlaces(String reservedPlaces, String freePlaces) {
        List<String> freePlacesAsList = new ArrayList<>(Arrays.asList(freePlaces.split(";")));
        String[] reservedPlacesAsArray = reservedPlaces.split(";");
        for (String place : reservedPlacesAsArray) {
            freePlacesAsList.remove(place);
        }
        return String.join(";", freePlacesAsList);
    }
}
