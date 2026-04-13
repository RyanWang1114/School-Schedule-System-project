import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.swing.*;

public class ChooseWeekViewPage extends JFrame implements ActionListener {
    // Declare class variables
    private User user;
    private JButton currentWeekButton;
    private JButton nextWeekButton;
    private JButton goBackButton;
    private JPanel buttonpanel;

    // Constructor: Set up the frame and UI components
    public ChooseWeekViewPage(User user) {
        this.user = user;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit the application when closing
        this.setLayout(new BorderLayout()); // Set layout as BorderLayout
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        this.setTitle("Choose Week"); // Set frame title

        // Title label setup
        JLabel titleLabel = new JLabel("Choose a Week to View", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));

        // Go Back button setup
        goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Arial", Font.PLAIN, 16));
        goBackButton.addActionListener(this);

        // Top panel for title and "Go Back" button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(goBackButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Current Week button setup
        currentWeekButton = new JButton("Current Week");
        currentWeekButton.setFont(new Font("Arial", Font.BOLD, 18));
        currentWeekButton.setPreferredSize(new Dimension(200, 80));
        currentWeekButton.addActionListener(this);

        // Next Week button setup
        nextWeekButton = new JButton("Next Week");
        nextWeekButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextWeekButton.setPreferredSize(new Dimension(200, 80));
        nextWeekButton.addActionListener(this);

        // Button panel for week selection buttons
        buttonpanel = new JPanel();
        buttonpanel.add(currentWeekButton);
        buttonpanel.add(nextWeekButton);

        // Add components to the frame
        this.add(topPanel, BorderLayout.NORTH); // Add top panel to the frame
        this.add(buttonpanel, BorderLayout.CENTER); // Add button panel to the center

        this.setVisible(true); // Make the frame visible
    }

    // Handle button actions
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goBackButton) {
            this.dispose(); // Close the current frame
        }
        if (e.getSource() == currentWeekButton) {
            // Use java.time to calculate the start of the current week
            LocalDate today = LocalDate.now();
            DayOfWeek dayinweek = today.getDayOfWeek();
            int daynumber = dayinweek.getValue();
            LocalDate startofthisweek = today.minusDays(daynumber - 1);
            String Startofcurrentweekdate = startofthisweek.toString();
            new UserCurrentweekpage(Startofcurrentweekdate, user);
        }
        if (e.getSource() == nextWeekButton) {
            // Use java.time to calculate the start of the next week
            LocalDate today = LocalDate.now();
            DayOfWeek dayinweek = today.getDayOfWeek();
            int daynumber = dayinweek.getValue();
            LocalDate startofnextweek = today.plusDays(7);
            startofnextweek = startofnextweek.minusDays(daynumber - 1);
            String Startofcurrentweekdate = startofnextweek.toString();
            new UserCurrentweekpage(Startofcurrentweekdate, user);
        }
    }
}
