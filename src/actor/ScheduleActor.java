package actor;

import akka.actor.AbstractActor;
import akka.actor.Status;
import models.Meeting;
import models.Participant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleActor extends AbstractActor {
    public ArrayList<Meeting> Meetings;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Meeting.class, this::handleScheduleMeeting)
                .build();
    }

    private void handleScheduleMeeting(Meeting meeting) {
        ArrayList<Participant> participants = meeting.getParticipants();
        ArrayList<Date> commonDates =  new ArrayList<>(participants.get(0).getAvailableDates());
        for (int i = 1; i < participants.size(); i++) {
            commonDates.retainAll(participants.get(i).getAvailableDates());
        }

        boolean isAvailable = !commonDates.isEmpty();
        if(isAvailable) {
            Date commonDate = commonDates.get(0);

            for(Participant p : meeting.getParticipants()){
                p.getAvailableDates().remove(commonDate);
                p.setAvailableDates(p.getAvailableDates());
            }
            System.out.println("Available date for everyone : " + commonDate);
            getSender().tell(commonDate, self());
        }
        else{
            System.out.println("no available date");
            throw new RuntimeException("no available date");
        }

    }

}
