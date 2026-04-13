import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

// Frame for changing password
public class ChangePasswordPage extends JFrame implements ActionListener {
    // File paths for user email and password storage
    private static final String Emaildocumentpath = "UserEmail.txt";
    private static final String Passworddocumentpath = "password.txt";
    
    // Components for the JFrame
    JLabel ForgetPassword;
    JLabel Emaillabel;
    JLabel oldPasswordLabel;
    JLabel newPasswordLabel;
    JButton resetpasswordbutton;
    JTextField Email;
    JPasswordField oldPasswordField;
    JPasswordField newPasswordField;
    JPanel textfieldpanel;
    JPanel SouthPanel;
    
    // Constructor to initialize the frame
    ChangePasswordPage() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Set close operation
        this.setLayout(new BorderLayout()); // Set layout manager
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize frame
        this.setTitle("Forget Password"); // Set frame title
        
        // Panel for buttons at the bottom
        SouthPanel = new JPanel();
        SouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 150));
        
        // Label for "Change Password" title
        ForgetPassword = new JLabel("Change Password", JLabel.CENTER);
        ForgetPassword.setFont(new Font("Serif", Font.BOLD, 50));
        ForgetPassword.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        
        // Panel for text fields
        textfieldpanel = new JPanel();
        textfieldpanel.setLayout(new BoxLayout(textfieldpanel, BoxLayout.Y_AXIS));
        textfieldpanel.setBorder(new EmptyBorder(100, 200, 20, 100));
        
        // Labels and fields for email, old password, and new password
        Emaillabel = new JLabel("Email: ");
        Emaillabel.setPreferredSize(new Dimension(100, 40));
        oldPasswordLabel = new JLabel("Old Password: ");
        oldPasswordLabel.setPreferredSize(new Dimension(100, 40));
        newPasswordLabel = new JLabel("New Password: ");
        newPasswordLabel.setPreferredSize(new Dimension(100, 40));
        
        // Button to reset password
        resetpasswordbutton = new JButton("Reset Password");
        resetpasswordbutton.setLayout(new FlowLayout(FlowLayout.CENTER));
        resetpasswordbutton.setPreferredSize(new Dimension(400, 80));
        resetpasswordbutton.addActionListener(this); // Action listener for button
        
        // Text fields for user input
        Email = new JTextField();
        Email.setPreferredSize(new Dimension(2000, 50)); 
        Email.setMaximumSize(new Dimension(2000, 50)); 
        
        oldPasswordField = new JPasswordField();
        oldPasswordField.setEchoChar('*');
        oldPasswordField.setPreferredSize(new Dimension(2000, 50)); 
        oldPasswordField.setMaximumSize(new Dimension(2000, 50)); 
        
        newPasswordField = new JPasswordField();
        newPasswordField.setEchoChar('*');
        newPasswordField.setPreferredSize(new Dimension(2000, 50)); 
        newPasswordField.setMaximumSize(new Dimension(2000, 50)); 
        
        // Add components to the text field panel
        textfieldpanel.add(Emaillabel);
        textfieldpanel.add(Box.createVerticalStrut(5));
        textfieldpanel.add(Email);
        textfieldpanel.add(Box.createVerticalStrut(20));
        textfieldpanel.add(oldPasswordLabel);
        textfieldpanel.add(Box.createVerticalStrut(5));
        textfieldpanel.add(oldPasswordField);
        textfieldpanel.add(Box.createVerticalStrut(20));
        textfieldpanel.add(newPasswordLabel);
        textfieldpanel.add(Box.createVerticalStrut(5));
        textfieldpanel.add(newPasswordField);
        
        // Add components to the frame
        SouthPanel.add(resetpasswordbutton);
        this.add(textfieldpanel, BorderLayout.CENTER);
        this.add(ForgetPassword, BorderLayout.NORTH);
        this.add(SouthPanel, BorderLayout.SOUTH);
        this.setVisible(true); // Make frame visible
    }
    
    // Action performed method for button click
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetpasswordbutton) { // Check which button was clicked
            String resetEmail = Email.getText(); // Get email input
            String oldPassword = new String(oldPasswordField.getPassword()); // Get old password input
            String newPassword = new String(newPasswordField.getPassword()); // Get new password input
            
            // Retrieve stored emails and passwords from files
            List<String> emails = getList(Emaildocumentpath);
            List<String> passwords = getList(Passworddocumentpath);
            
            // Find index of email in list
            int index = emails.indexOf(resetEmail);
            if (index == -1) { // If email not found
                JOptionPane.showMessageDialog(this, "Email not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!passwords.get(index).equals(oldPassword)) { // If old password doesn't match stored password
                JOptionPane.showMessageDialog(this, "Incorrect old password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update password for the user
            List<User> userlist = User.getuserlist();
            boolean found = false;
            for (User user : userlist) {
                if (user.getEmail().equals(resetEmail)) {
                    found = true;
                    user.changepassword(newPassword); // Change user's password
                    break;
                }
            }
            
            // Show success or error message based on password change
            if (found) {
                JOptionPane.showMessageDialog(this, "Your password has been changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close current frame
                new Welcome(); // Open new welcome frame
            } else {
                JOptionPane.showMessageDialog(this, "An error occurred while changing your password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Method to read lines from file and return as list
    private List<String> getList(String path) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] items = line.split("\\|");
                for (String item : items) {
                    if (!item.trim().isEmpty()) {
                        list.add(item.trim());
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + path, "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }
}
