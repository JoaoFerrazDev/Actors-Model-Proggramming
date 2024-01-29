package actor;

import DTOs.MeetingDto;
import DTOs.ScheduleDto;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import models.MeetingParticipant;
import akka.actor.AbstractActor;
import akka.actor.Status;
import models.Meeting;
import models.Participant;
import scala.Array;
import scala.Option;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class MeetingActor extends AbstractActor {

    public static ArrayList<Meeting> meetings;

    public MeetingActor() {
        meetings = new ArrayList<>();
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MeetingDto.class, this::handleCreatingMeeting)
                .match(Meeting.class, this::handleScheduleMeeting)
                .match(MeetingParticipant.class, this::handleParticipantInMeeting)
                .match(Integer.class, this::handleMeetingInformation)
                .build();
    }

    private void handleCreatingMeeting(MeetingDto dto){
        Meeting meeting = new Meeting(dto.Description, dto.Localization, dto.Duration, dto.Email);
        meetings.add(meeting);
        getSender().tell(meeting.getId(), self());
    }
    private void handleParticipantInMeeting(MeetingParticipant meetingParticipant) {
        System.out.println("meeting participant" + meetingParticipant.Meeting.getId());
        if(meetingParticipant.Meeting.getParticipants().stream().anyMatch(participant -> Objects.equals(participant.getEmail(), meetingParticipant.Email))) {
            throw new RuntimeException("Duplicate participant");
        }
        Participant participant = new Participant(meetingParticipant.Email,meetingParticipant.AvailableDates);
        meetingParticipant.Meeting.addParticipant(participant);
        getSender().tell(true, self());
    }

    private void handleScheduleMeeting(Meeting meeting) throws ExecutionException, InterruptedException {
        ActorRef scheduleActor = getOrCreateActor(ScheduleActor.class, "scheduleActor");
        for (Participant p : meeting.getParticipants()) {
            for (Date d : p.getAvailableDates()) {
                System.out.println("Date: " + d);
            }
        }
        java.time.Duration timeout = java.time.Duration.ofSeconds(10);
        CompletionStage<Object> result = Patterns.ask(scheduleActor, meeting, timeout);
        Date resultValue = (Date) result.toCompletableFuture().get();

        ActorRef reportActor = getOrCreateActor(ReportActor.class, "reportActor");
        ScheduleDto scheduleDto = new ScheduleDto(meeting,resultValue);
        reportActor.tell(scheduleDto, self());
    }
    private void handleMeetingInformation(int id) {
        System.out.println("Here : " + id);
        ActorRef scheduleActor = getOrCreateActor(ScheduleActor.class, "scheduleActor");
        scheduleActor.forward(id, this.context());
    }
    private ActorRef getOrCreateActor(Class<?> clazz, String name)
    {
        Option<ActorRef> actor = this.context().child(name);
        if (actor.isDefined())
            return actor.get();
        return this.context().actorOf(Props.create(clazz), name);
    }


}
