package DTOs;

import models.Participant;

import java.util.ArrayList;
import java.util.Date;

public class MeetingDto {
    public static int Id;
    public String Description;
    public String Localization;
    public int Duration;
    public String Email;
    public ArrayList<Participant> Participants;

    public MeetingDto(String description, String localization, int duration, String email) {
        Description = description;
        Localization = localization;
        Duration = duration;
        Email = email;
    }
}
