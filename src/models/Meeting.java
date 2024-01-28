package models;

import java.util.ArrayList;
import java.util.Date;

public class Meeting {
    private int Id;
    private String Description;
    private String Localization;
    private Date Duration;
    private String Email;
    private ArrayList<Participant> Participants;
    private static int IdCounter;

    public Meeting(String description,
                   String localization,
                   Date duration,
                   String email) {
        Id = IdCounter;
        IdCounter++;
        this.Description = description;
        this.Localization = localization;
        this.Duration = duration;
        this.Email = email;
    }
    public int getId() { return Id; }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocalization() {
        return Localization;
    }

    public void setLocalization(String localization) {
        Localization = localization;
    }

    public Date getDuration() {
        return Duration;
    }

    public void setDuration(Date duration) {
        Duration = duration;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public ArrayList<Participant> getParticipants() {
        return Participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        Participants = participants;
    }

    public void addParticipant(Participant participant) {
        this.Participants.add(participant);
    }
}
