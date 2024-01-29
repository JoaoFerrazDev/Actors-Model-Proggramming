package GUI;

import DTOs.MeetingDto;
import actor.MeetingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import models.Meeting;
import models.MeetingParticipant;
import models.Participant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class GUI extends JFrame {
    private final ActorRef meetingActor;
    private ArrayList<JLabel> idLabels;
    private JPanel mainPanel;

    public GUI(ActorSystem actorSystem) {
        this.meetingActor = actorSystem.actorOf(Props.create(MeetingActor.class), "meetingActor");
    }
    public void createAndShowGUI(ActorSystem actorSystem) {
        JFrame frame = new JFrame("Meeting Scheduler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Create a JPanel to hold the components
        mainPanel = new JPanel(new GridLayout(MeetingActor.meetings.size(), 2, 10, 10));
        updateMainPanelMeetings();

        JButton createMeetingButton = new JButton("Create Meeting");
        createMeetingButton.addActionListener(e -> openCreateMeetingWindow());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createMeetingButton);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }



    private void openAddParticipantWindow(Meeting meeting) {
        JFrame participantFrame = new JFrame("Add Participant");
        participantFrame.setSize(600, 400);
        participantFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel buttonPanel = new JPanel();
        JPanel panel = new JPanel(new GridLayout(buttonPanel.getComponentCount(),1, 10, 10));

        JTextField emailField = new JTextField();
        JTextField hourField = new JTextField();
        ArrayList<JDateChooser> listDates = new ArrayList<JDateChooser>();
        ArrayList<JTextField> listHours = new ArrayList<JTextField>();
        JDateChooser dateChooser = new JDateChooser();
        listDates.add(dateChooser);
        listHours.add(hourField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Date:"));
        panel.add(dateChooser);
        panel.add(new JLabel("Hour:"));
        panel.add(hourField);


        JButton addButton = new JButton("Add");
        JButton addMoreButton = new JButton("+");
        JButton removeLastButton = new JButton("-");

        buttonPanel.add(addButton);
        buttonPanel.add(addMoreButton);
        buttonPanel.add(removeLastButton);

        //Adicionar participante
        addButton.addActionListener(e -> {
            String email = emailField.getText();
            ArrayList<Date> dates = new ArrayList<>();
            for (int i = 0; i < listDates.size(); i++) {
                Date selectedDate = listDates.get(i).getDate();
                String selectedHour = listHours.get(i).getText() + ":00";
                if (selectedDate != null) {
                    dates.add(combineDateAndTime(selectedDate, selectedHour));
                }
            }
            boolean isCreated;
            java.time.Duration timeout = java.time.Duration.ofSeconds(50);
            CompletionStage<Object> result = Patterns.ask(meetingActor, new MeetingParticipant(meeting,email, dates), timeout);
            try {
                isCreated = (boolean) result.toCompletableFuture().get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
            if(isCreated) updateMainPanelMeetings();
            participantFrame.dispose();
        });
        //Adicionar e remover inputs das datas disponiveis
        addMoreButton.addActionListener(e -> {
            JDateChooser newDateChooser = new JDateChooser();
            JTextField newHourField = new JTextField();
            panel.add(new JLabel("Date:"));
            panel.add(newDateChooser);
            panel.add(new JLabel("Hour:"));
            panel.add(newHourField);
            listDates.add(newDateChooser);
            listHours.add(newHourField);
            participantFrame.revalidate();
            participantFrame.repaint();
        });
        removeLastButton.addActionListener(e -> {
           panel.remove(panel.getComponentCount() - 1);
           panel.remove(panel.getComponentCount() - 1);
           panel.remove(panel.getComponentCount() - 1);
           panel.remove(panel.getComponentCount() - 1);
           listDates.removeLast();
           listHours.removeLast();
            participantFrame.revalidate();
            participantFrame.repaint();
        });

        updateMainPanelMeetings();
        participantFrame.getContentPane().setLayout(new BorderLayout());
        participantFrame.getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);
        participantFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        participantFrame.setLocationRelativeTo(null);
        participantFrame.setVisible(true);
    }


    private void openCreateMeetingWindow() {
        JFrame createMeetingFrame = new JFrame("Add Meeting");
        createMeetingFrame.setSize(600, 400);
        createMeetingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding

        Dimension textFieldSize = new Dimension(150, 20);
        Dimension labelFieldSize = new Dimension(50, 20);

        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setPreferredSize(labelFieldSize);
        JTextField descriptionField = new JTextField();
        descriptionField.setPreferredSize(textFieldSize);

        // Localization
        JLabel localizationLabel = new JLabel("Localization:");
        localizationLabel.setPreferredSize(labelFieldSize);
        JTextField localizationField = new JTextField();
        localizationField.setPreferredSize(textFieldSize);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setPreferredSize(labelFieldSize);
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(textFieldSize);

        // Duration
        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setPreferredSize(labelFieldSize);
        JTextField dateChooser = new JTextField();
        dateChooser.setPreferredSize(textFieldSize);

        // Add components to the panel
        panel.add(descriptionLabel);
        panel.add(descriptionField);
        panel.add(localizationLabel);
        panel.add(localizationField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(durationLabel);
        panel.add(dateChooser);

        //Criar meeting
        JButton addButton = new JButton("Create Meeting");
        addButton.addActionListener(e -> {
            String description = descriptionField.getText();
            String localization = localizationField.getText();
            String email = emailField.getText();
            int duration = Integer.parseInt(dateChooser.getText());
            java.time.Duration timeout = java.time.Duration.ofSeconds(5);
            CompletionStage<Object> result = Patterns.ask(meetingActor, new MeetingDto(description,localization,duration,email), timeout);
            try {
                int id = (int) result.toCompletableFuture().get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
            updateMainPanelMeetings();
            createMeetingFrame.dispose();
        });
        panel.add(addButton, BorderLayout.SOUTH);
        createMeetingFrame.getContentPane().add(panel);
        createMeetingFrame.setLocationRelativeTo(null);
        createMeetingFrame.setVisible(true);
    }

    private void updateMainPanelMeetings() {
        mainPanel.removeAll();
        for (Meeting meeting : MeetingActor.meetings) {
            StringBuilder message = new StringBuilder("Reunião número: " + meeting.getId() + " / Participantes: ");
            for(Participant p : meeting.getParticipants()) {
                message.append(p.getEmail()).append(", ");
            }
            JLabel messageLabel = new JLabel(message.toString());
            mainPanel.add(messageLabel);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            //Add Participant in Meeting
            JButton addParticipantButton = new JButton("Add Participant");
            addParticipantButton.addActionListener(e -> openAddParticipantWindow(meeting));
            buttonPanel.add(addParticipantButton);
            //Schedule Meeting
            JButton scheduleMeetingButton = new JButton("Schedule Meeting");
            scheduleMeetingButton.addActionListener(e -> meetingActor.tell(meeting, ActorRef.noSender()));
            buttonPanel.add(scheduleMeetingButton);
            //Print Meeting on Console
            JButton printMeetingInfoButton = new JButton("Print Meeting Info");
            printMeetingInfoButton.addActionListener(e -> meetingActor.tell(meeting.getId(), ActorRef.noSender()));
            buttonPanel.add(printMeetingInfoButton);

            mainPanel.add(buttonPanel);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static Date combineDateAndTime(Date date, String timeString) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date timeDate = timeFormat.parse(timeString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.HOUR_OF_DAY, timeDate.getHours());
            calendar.set(Calendar.MINUTE, timeDate.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
