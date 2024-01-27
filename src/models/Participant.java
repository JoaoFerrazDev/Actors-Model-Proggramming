package models;

import java.util.ArrayList;
import java.util.Date;

public class Participant {
    private String Email;
    private ArrayList<Date> AvailableDates;


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public ArrayList<Date> getAvailableDates() {
        return AvailableDates;
    }

    public void setAvailableDates(ArrayList<Date> availableDates) {
        AvailableDates = availableDates;
    }
}
