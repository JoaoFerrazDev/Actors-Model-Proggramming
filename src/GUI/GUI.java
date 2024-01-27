package GUI;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField textField1, textField2, textField3;
    private JTextField secondMenuTextField;
    private JTextArea dateTextArea;
    private ArrayList<String> informationList;
    private JTextField numberOfDatesField;
    private JPanel dateInputsPanel;
    private JPanel inputPanel;
    private JButton saveButton;
    private ArrayList<JDateChooser> mainDateChoosers = new ArrayList<>();
    private JLabel selectedDatesLabel;
    private ArrayList<String> textInputValues;


    public GUI() {
        informationList = new ArrayList<>();
        dateInputsPanel = new JPanel(new GridLayout(0, 1));
        inputPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        selectedDatesLabel = new JLabel();
        mainDateChoosers = new ArrayList<>();
        // Set up the frame
        setTitle("Multi-Menu GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initial Menu
        JPanel initialMenuPanel = createInitialMenu();
        cardPanel.add(initialMenuPanel, "initial");

        // Main Menu
        JPanel mainMenuPanel = createMainMenu();
        cardPanel.add(mainMenuPanel, "main");

        // Second Menu
        JPanel secondMenuPanel = createSecondMenu();
        cardPanel.add(secondMenuPanel, "second");

        // Third Menu
        JPanel thirdMenuPanel = createThirdMenu();
        cardPanel.add(thirdMenuPanel, "third");

        // Add the cardPanel to the frame
        add(cardPanel);

        // Make the frame visible
        setVisible(true);
    }

    private JPanel createInitialMenu() {
        // Create components for the initial menu
        JButton menu1Button = new JButton("Go to Menu 1");
        JButton menu2Button = new JButton("Go to Menu 2");
        JButton menu3Button = new JButton("Go to Menu 3");

        // Create a panel to hold the components
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to the panel
        panel.add(menu1Button);
        panel.add(menu2Button);
        panel.add(menu3Button);

        // Add action listeners to the buttons
        menu1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "main");
            }
        });

        menu2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "second");
            }
        });

        menu3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "third");
            }
        });

        return panel;
    }

    private JPanel createMainMenu() {
        // Create components for the main menu
        textField1 = new JTextField(20);
        textField2 = new JTextField(20);
        textField3 = new JTextField(20);

        // Use JDateChooser for date input
        JDateChooser dateChooser = new JDateChooser();

        JButton backToInitialButton = new JButton("Back to Initial Menu");
        JButton saveInfoButton = new JButton("Save Information");

        // Create a panel to hold the components
        JPanel panel = new JPanel(new GridLayout(9, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to the panel
        panel.add(new JLabel("Text Field 1:"));
        panel.add(textField1);
        panel.add(new JLabel("Text Field 2:"));
        panel.add(textField2);
        panel.add(new JLabel("Text Field 3:"));
        panel.add(textField3);
        panel.add(new JLabel("Date Input:"));
        panel.add(dateChooser);
        panel.add(backToInitialButton);
        panel.add(saveInfoButton);

        // Add action listeners to the buttons
        backToInitialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "initial");
            }
        });

        saveInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInformation(textField1, textField2, textField3, dateChooser);
            }
        });

        return panel;
    }

    private void saveInformation(JTextField textField1, JTextField textField2, JTextField textField3, JDateChooser dateChooser) {
        // Save the information to the variable
        String text1 = textField1.getText();
        String text2 = textField2.getText();
        String text3 = textField3.getText();

        // Get the chosen date from the JDateChooser
        Date date = dateChooser.getDate();

        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(date);

        String information = "Text Field 1: " + text1 + "\nText Field 2: " + text2
                + "\nText Field 3: " + text3 + "\nDate Input: " + formattedDate;

        informationList.add(information);

        // Clear the input fields
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        dateChooser.setDate(null);

        // Display the saved information in the console
        System.out.println("Information saved:\n" + information);
    }
    private JPanel createSecondMenu() {
        secondMenuTextField = new JTextField(20);
        numberOfDatesField = new JTextField(5);
        JButton generateDatesButton = new JButton("Generate Dates");
        JCalendar dateCalendar = new JCalendar();
        JDateChooser dateChooser = new JDateChooser();

        JButton backToInitialButton = new JButton("Back to Initial Menu");
        saveButton = new JButton("Save");

        // Create a panel to hold the components
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add the inputPanel to the CENTER of the BorderLayout
        panel.add(inputPanel, BorderLayout.CENTER);

        // Add the saveButton to the SOUTH of the BorderLayout
        panel.add(saveButton, BorderLayout.SOUTH);

        // Add components to the inputPanel
        inputPanel.add(new JLabel("Text Input:"));
        inputPanel.add(secondMenuTextField);
        inputPanel.add(new JLabel("Number of Dates:"));
        inputPanel.add(numberOfDatesField);
        inputPanel.add(generateDatesButton);
        inputPanel.add(new JLabel("Selected Dates:"));

        // Create a label to display selected dates
        selectedDatesLabel = new JLabel();
        inputPanel.add(selectedDatesLabel);

        // Add action listeners to the buttons
        backToInitialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "initial");
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInformation(secondMenuTextField, mainDateChoosers, textInputValues);
            }
        });

        generateDatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateDateInputs();
            }
        });

        return panel;
    }

    private void generateDateInputs() {
        try {
            int numberOfDates = Integer.parseInt(numberOfDatesField.getText());

            // Create a new frame for the second window
            JFrame dateInputsFrame = new JFrame("Generated Date Inputs");
            dateInputsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Create a panel for date inputs and text inputs in the new frame
            JPanel dateInputsPanel = new JPanel(new GridLayout(numberOfDates, 2, 5, 5)); // Adjusted layout and spacing
            JPanel textInputsPanel = new JPanel(new GridLayout(numberOfDates, 1, 5, 5)); // Adjusted layout and spacing

            // Create a list for JDateChooser components for the second window
            ArrayList<JDateChooser> secondDateChoosers = new ArrayList<>();
            ArrayList<String> textInputs = new ArrayList<>();

            // Generate and add new date inputs (2 for each line) and text inputs to the panels
            for (int i = 0; i < numberOfDates; i++) {
                JDateChooser dateChooser1 = new JDateChooser();
                JTextField textInput = new JTextField();

                dateInputsPanel.add(new JLabel("Date " + (i + 1) + ":"));
                dateInputsPanel.add(dateChooser1);

                textInputsPanel.add(new JLabel("Text Input " + (i + 1) + ":"));
                textInputsPanel.add(textInput);

                // Add JDateChooser components and text inputs to the lists for the second window
                secondDateChoosers.add(dateChooser1);
                textInputs.add(textInput.getText());  // Store the initial text input value
            }

            // Add a button to submit selected dates and text inputs back to the main window
            JButton submitDatesButton = new JButton("Submit Dates and Text Inputs");
            submitDatesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Retrieve selected dates from dateInputsPanel and store them in the list for the main window
                    mainDateChoosers.addAll(secondDateChoosers);

                    // Display selected dates in the label of the second menu
                    updateSelectedDatesLabel(mainDateChoosers, textInputs);

                    // Save information with selected dates and text inputs
                    saveInformation(secondMenuTextField, mainDateChoosers, textInputs);

                    // Close the second window
                    dateInputsFrame.dispose();
                }
            });

            // Add the dateInputsPanel and textInputsPanel to the frame
            dateInputsFrame.getContentPane().setLayout(new GridLayout(2, 1, 10, 10)); // Adjusted layout and spacing
            dateInputsFrame.getContentPane().add(dateInputsPanel);
            dateInputsFrame.getContentPane().add(textInputsPanel);

            // Create a new panel for the button with FlowLayout to place it at the bottom
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(submitDatesButton);

            dateInputsFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            // Set the size and make the frame visible
            dateInputsFrame.setSize(400, 200); // Adjusted size
            dateInputsFrame.setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedDatesLabel(ArrayList<JDateChooser> dateChoosers, ArrayList<String> textInputs) {
        // Display selected dates and text inputs in the label of the second menu
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        StringBuilder datesAndTextsText = new StringBuilder("<html><b>Selected Dates and Text Inputs:</b><br>");

        for (int i = 0; i < dateChoosers.size(); i++) {
            Date selectedDate = dateChoosers.get(i).getDate();
            String textInputValue = (i < textInputs.size()) ? textInputs.get(i) : "";

            if (selectedDate != null) {
                datesAndTextsText.append(dateFormat.format(selectedDate))
                        .append(" - Text Input: ").append(textInputValue).append("<br>");
            }
        }

        datesAndTextsText.append("</html>");

        // Assume selectedDatesLabel is the label where you want to display the information
        selectedDatesLabel.setText(datesAndTextsText.toString());
    }

    private void saveInformation(JTextField textField, ArrayList<JDateChooser> selectedDateChoosers, ArrayList<String> textInputs) {
        // Save the information to the variable
        String textInput = textField.getText();

        // Format the dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        StringBuilder formattedDates = new StringBuilder("Dates Input: ");
        for (JDateChooser dateChooser : selectedDateChoosers) {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate != null) {
                formattedDates.append(dateFormat.format(selectedDate)).append(", ");
            }
        }

        // Remove the trailing comma and space if there are dates
        if (selectedDateChoosers.size() > 0) {
            formattedDates.setLength(formattedDates.length() - 2);
        }

        // Display text inputs
        StringBuilder textInputsText = new StringBuilder("Text Inputs: ");
        for (String textInputValue : textInputs) {
            textInputsText.append(textInputValue).append(", ");
        }

        // Remove the trailing comma and space if there are text inputs
        if (textInputs.size() > 0) {
            textInputsText.setLength(textInputsText.length() - 2);
        }

        String information = "Text Input: " + textInput + "\n" +
                formattedDates.toString() + "\n" +
                textInputsText.toString();

        informationList.add(information);

        // Clear the input fields and selected dates
        textField.setText("");
        selectedDateChoosers.clear();
        updateSelectedDatesLabel(mainDateChoosers, textInputValues);  // Update the label to display dates and text inputs

        // Display the saved information in the console
        System.out.println("Information saved:\n" + information);
    }

    private JPanel createThirdMenu() {
        // Create components for the third menu
        JTextArea infoTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(infoTextArea);

        JButton backToInitialButton = new JButton("Back to Initial Menu");

        // Create a panel to hold the components
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backToInitialButton, BorderLayout.SOUTH);

        // Add action listener to the button
        backToInitialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "initial");
            }
        });

        return panel;
    }
}
