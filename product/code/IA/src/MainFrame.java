import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener {
    
    // Declare class variables and components
    User user;
    JButton Schedule_new_week;
    JButton View_current_week_schedule;
    JButton Personal_detail;
    JLabel label;
    JPanel buttonpanel;
    JPanel southPanel;

    // MainFrame constructor: Set up the frame
    MainFrame(User user) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit on close
        this.setLayout(new BorderLayout()); // Set BorderLayout
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize frame
        this.setTitle("Welcome"); // Frame title
        
        this.user = user; // Initialize user
        String text = user.getEmail(); // Get user's email
        
        // Create a welcome label
        label = new JLabel("Welcome! " + text, JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 50));
        label.setBorder(BorderFactory.createEmptyBorder(100, 0, 50, 0)); // Add padding
        
        // Set up southPanel with GridBagLayout
        southPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
        
        // Create button panel with GridLayout
        buttonpanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonpanel.setPreferredSize(new Dimension(900, 100)); // Set button panel size
        
        Dimension buttonsize = new Dimension(250, 80); // Standard button size
        
        // Create "Schedule new week" button
        Schedule_new_week = new JButton("Schedule new week");
        Schedule_new_week.addActionListener(this);
        Schedule_new_week.setPreferredSize(buttonsize);
        
        // Create "View current week schedule" button
        View_current_week_schedule = new JButton("View current week schedule");
        View_current_week_schedule.addActionListener(this);
        View_current_week_schedule.setPreferredSize(buttonsize);
        
        // Add buttons to the button panel
        buttonpanel.add(Schedule_new_week);
        buttonpanel.add(View_current_week_schedule);
        southPanel.add(buttonpanel, gbc); // Add button panel to south panel
        
        // Add components to the frame
        this.add(label, BorderLayout.NORTH); // Add welcome label to the top
        this.add(southPanel, BorderLayout.CENTER); // Add button panel to the center
        this.setVisible(true); // Make the frame visible
    }

    // Handle button actions
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Schedule_new_week) {
            new Chooseweekpage(user); // Open "Choose week" page
            this.dispose(); // Close current frame
        }
        if (e.getSource() == View_current_week_schedule) {
            new ChooseWeekViewPage(user); // Open "View current week schedule" page
        }
    }
}
