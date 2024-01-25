package actor;

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
        return null;
    }

    public void writeMeetingInfo(Meeting meeting){
        try {
            File meetingFile = new File(meeting.getId() + ".txt");
            FileWriter fileWriter = new FileWriter(meeting.getId() + ".txt");
            fileWriter.write(meeting.getDescription());
            fileWriter.write(meeting.getDuration().toString());
            fileWriter.write(meeting.getLocalization());
            fileWriter.write(meeting.getEmail());
            for (Participant p: meeting.getParticipants()){
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
}
