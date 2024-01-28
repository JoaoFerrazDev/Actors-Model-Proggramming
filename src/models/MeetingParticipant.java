package models;

import java.util.ArrayList;
import java.util.Date;

public class MeetingParticipant {
    public Meeting Meeting;
    public String Email;
    public ArrayList<Date> AvailableDates;
    public MeetingParticipant(Meeting meeting, String email, ArrayList<Date> availableDates) {
        Meeting = meeting;
        Email = email;
        AvailableDates = availableDates;
    }
}
