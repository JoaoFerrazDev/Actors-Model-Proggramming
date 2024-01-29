package actor;

import DTOs.ScheduleDto;
import akka.actor.AbstractActor;
import models.Participant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class ReportActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ScheduleDto.class, this::handleMeetingFile)
                .match(Integer.class, this::handleMeetingInfo)
                .build();
    }

    private void handleMeetingFile(ScheduleDto scheduleDto){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("files/" + scheduleDto.Meeting.getId() + ".txt"))) {
            // Writing Meeting information
            writer.write("Meeting ID: " + scheduleDto.Meeting.getId() + "\n");
            writer.write("Description: " + scheduleDto.Meeting.getDescription() + "\n");
            writer.write("Localization: " + scheduleDto.Meeting.getLocalization() + "\n");
            writer.write("Duration: " + scheduleDto.Meeting.getDuration() + " minutes\n");
            writer.write("Email: " + scheduleDto.Meeting.getEmail() + "\n");

            // Writing Participants information
            for (Participant participant : scheduleDto.Meeting.getParticipants()) {
                writer.write("Participant: " + participant.getEmail() + "\n");
                writer.write("Participant Available Dates: \n");
                for (Date date : participant.getAvailableDates()) {
                    writer.write(date + "\n");
                }
            }
            writer.write("Scheduled Date: " + scheduleDto.ScheduledDate + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleMeetingInfo(int id){

    }
}
