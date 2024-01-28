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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private ActionListener createButtonActionListener(String message, int buttonNumber) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Message: " + message + ", Button " + buttonNumber + " clicked");
            }
        };
    }

    private void openAddParticipantWindow() {
        JFrame participantFrame = new JFrame("Add Participant");
        participantFrame.setSize(300, 250);
        participantFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2,1, 10, 10));

        JTextField nameField = new JTextField();
        JDateChooser dateChooser = new JDateChooser();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Date:"));
        panel.add(dateChooser);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        buttonPanel.add(addButton);
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String selectedDate = dateChooser.getDate().toString();
            //meetingActor.tell(new AddParticipantMessage(name, selectedDate), ActorRef.noSender());
            participantFrame.dispose();
        });
        participantFrame.getContentPane().setLayout(new BorderLayout());
        participantFrame.getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);
        participantFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        participantFrame.setVisible(true);
    }

    private void openCreateMeetingWindow() {
        JFrame createMeetingFrame = new JFrame("Add Meeting");
        createMeetingFrame.setSize(600, 400);
        createMeetingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 4));

        JTextField descriptionField = new JTextField();
        JTextField localizationField = new JTextField();
        JTextField emailField = new JTextField();
        JDateChooser dateChooser = new JDateChooser();

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Localization:"));
        panel.add(localizationField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Duration:"));
        panel.add(dateChooser);

        JButton addButton = new JButton("Create Meeting");
        addButton.addActionListener(e -> {
            String description = descriptionField.getText();
            String localization = localizationField.getText();
            String email = emailField.getText();
            Date duration = dateChooser.getDate();
            java.time.Duration timeout = java.time.Duration.ofSeconds(5);
            CompletionStage<Object> result = Patterns.ask(meetingActor, new MeetingDto(description,localization,duration,email), timeout);
            try {
                int id = (int) result.toCompletableFuture().get();
                System.out.println("O Id da reunião criada é : " + id);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
            updateMainPanelMeetings();
            createMeetingFrame.dispose();
        });
        panel.add(addButton, BorderLayout.SOUTH);
        createMeetingFrame.getContentPane().add(panel);
        createMeetingFrame.setVisible(true);
    }

    private void updateMainPanelMeetings() {
        System.out.println(MeetingActor.meetings.size());
        mainPanel.removeAll();
        for (Meeting meeting : MeetingActor.meetings) {
            JLabel messageLabel = new JLabel("Reunião número : " + meeting.getId());
            mainPanel.add(messageLabel);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton addParticipantButton = new JButton("Add Participant");
            addParticipantButton.addActionListener(e -> openAddParticipantWindow());
            buttonPanel.add(addParticipantButton);
            JButton scheduleMeetingButton = new JButton("Schedule Meeting");
            scheduleMeetingButton.addActionListener(e -> openAddParticipantWindow());
            buttonPanel.add(scheduleMeetingButton);
            JButton printMeetingInfoButton = new JButton("Print Meeting Info");
            printMeetingInfoButton.addActionListener(e -> openAddParticipantWindow());
            buttonPanel.add(printMeetingInfoButton);

            mainPanel.add(buttonPanel);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
