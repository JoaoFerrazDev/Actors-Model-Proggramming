package actor;

import DTOs.MeetingDto;
import DTOs.ScheduleDto;
import akka.actor.AbstractActor;
import models.Meeting;
import models.Participant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
        try {
            File meetingFile = new File(scheduleDto.Meeting.getId() + ".txt");
            FileWriter fileWriter = new FileWriter(scheduleDto.Meeting.getId() + ".txt");
            fileWriter.write(scheduleDto.Meeting.getDescription());
            fileWriter.write(scheduleDto.Meeting.getDuration().toString());
            fileWriter.write(scheduleDto.Meeting.getLocalization());
            fileWriter.write(scheduleDto.Meeting.getEmail());
            fileWriter.write(scheduleDto.ScheduledDate.toString());
            for (Participant p: scheduleDto.Meeting.getParticipants()){
                fileWriter.write(p.getEmail());
                fileWriter.write("Datas disponíveis");
                for (Date d: p.getAvailableDates()) {
                    fileWriter.write(d.toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleMeetingInfo(int id){

    }
}
