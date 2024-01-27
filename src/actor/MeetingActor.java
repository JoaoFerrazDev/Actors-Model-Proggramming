package actor;

import DTOs.MeetingDto;
import akka.actor.AbstractActor;
import akka.actor.Status;
import models.Meeting;

import java.util.Date;

public class MeetingActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MeetingDto.class, this::handleCreatingMeeting)
                .match(Meeting.class, this::handleScheduleMeeting)
                .match(String.class, s -> s.equals("alive?"), s -> {
                    getSender().tell(this.getClass().getName() + " is alive!", self());
                })
                .match(String.class, s -> !s.equals("alive?"), s -> {
                    getSender().tell(new Status.Failure(new Exception("Invalid command.")), self());
                })
                .build();
    }

    public void handleCreatingMeeting(MeetingDto dto){
        Meeting meeting = new Meeting(dto.Description, dto.Localization, dto.Duration, dto.Email);
        getSender().tell(meeting.getId(), self());
    }

    public void handleScheduleMeeting(Meeting meeting) {
        getSender().forward(meeting, this.context());
    }
}
