package GUI;

import DTOs.MeetingDto;
import actor.MeetingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
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

public class GUI extends JFrame {
    private final ActorRef meetingActor;

    public GUI(ActorSystem actorSystem) {
        this.meetingActor = actorSystem.actorOf(Props.create(MeetingActor.class), "meetingActor");
    }
    public void createAndShowGUI(ActorSystem actorSystem) {
        JFrame frame = new JFrame("Meeting Scheduler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Create a JPanel to hold the components
        JPanel mainPanel = new JPanel(new GridLayout(MeetingActor.meetings.size(), 2, 10, 10));

        for (Meeting meeting : MeetingActor.meetings) {
            JLabel messageLabel = new JLabel(String.valueOf(meeting.getId()));
            mainPanel.add(messageLabel);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            for (int i = 1; i <= 3; i++) {
                JButton button = new JButton("Button " + i);
                button.addActionListener(createButtonActionListener("message", i));
                buttonPanel.add(button);
            }

            mainPanel.add(buttonPanel);
        }

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
        participantFrame.setSize(300, 150);
        participantFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JTextField nameField = new JTextField();
        JDateChooser dateChooser = new JDateChooser();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Date:"));
        panel.add(dateChooser);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String selectedDate = dateChooser.getDate().toString();
            //meetingActor.tell(new AddParticipantMessage(name, selectedDate), ActorRef.noSender());
            participantFrame.dispose();
        });

        panel.add(addButton);

        participantFrame.getContentPane().add(panel);
        participantFrame.setVisible(true);
    }

    private void openCreateMeetingWindow() {
        JFrame createMeetingFrame = new JFrame("Add Meeting");
        createMeetingFrame.setSize(600, 400);
        createMeetingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

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
            meetingActor.tell(new MeetingDto(description,localization,duration,email), ActorRef.noSender());
            createMeetingFrame.dispose();
        });

        panel.add(addButton);

        createMeetingFrame.getContentPane().add(panel);
        createMeetingFrame.setVisible(true);
    }
}
