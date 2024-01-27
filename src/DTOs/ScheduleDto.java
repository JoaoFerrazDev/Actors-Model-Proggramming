package DTOs;

import models.Meeting;

import java.util.Date;

public class ScheduleDto {
    public Meeting Meeting;
    public Date ScheduledDate;

    public ScheduleDto(Meeting meeting, Date scheduledDate) {
        this.Meeting = meeting;
        this.ScheduledDate = scheduledDate;
    }
}
