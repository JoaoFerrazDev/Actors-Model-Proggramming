package actor;

import DTOs.MeetingDto;
import DTOs.ScheduleDto;
import akka.actor.AbstractActor;
import akka.actor.Status;
import models.Meeting;
import models.Participant;

import java.util.ArrayList;
import java.util.Date;

public class ScheduleActor extends AbstractActor {
    public ArrayList<Meeting> Meetings;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Meeting.class, this::handleScheduleMeeting)
                .match(String.class, s -> s.equals("alive?"), s -> {
                    getSender().tell(this.getClass().getName() + " is alive!", self());
                })
                .match(String.class, s -> !s.equals("alive?"), s -> {
                    getSender().tell(new Status.Failure(new Exception("Invalid command.")), self());
                })
                .build();
    }

    public void handleScheduleMeeting(Meeting meeting) {
        ArrayList<Participant> participants = meeting.getParticipants();
        ArrayList<Date> commonDates =  new ArrayList<Date>(participants.get(0).getAvailableDates());
        for (int i = 1; i < participants.size(); i++) {
            commonDates.retainAll(participants.get(i).getAvailableDates());
        }

        boolean isAvailable = commonDates.isEmpty();
        if(!isAvailable) {
            Date commonDate = commonDates.get(0);
            System.out.println("Available date for everyone : " + commonDate);
            ScheduleDto scheduledMeeting = new ScheduleDto(meeting, commonDate);
            getSelf().tell(scheduledMeeting, );
        }
        else
            throw new RuntimeException();
    }

}
