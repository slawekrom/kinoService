package service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReservationParam {

    String places;
    boolean paid;
    long userId;
    long showingId;

    public ReservationParam() {
    }

    public ReservationParam(String places, boolean isPaid, long userId, long showingId) {
        this.places = places;
        this.paid = isPaid;
        this.userId = userId;
        this.showingId = showingId;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public boolean getPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getShowingId() {
        return showingId;
    }

    public void setShowingId(long showingId) {
        this.showingId = showingId;
    }
}
