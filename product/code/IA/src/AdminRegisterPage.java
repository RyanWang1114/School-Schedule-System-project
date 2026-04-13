import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

public class AdminRegisterPage extends JFrame implements ActionListener {
    
    // File paths for storing admin email and password data
    private static final String Emaildocumentpath = "AdminEmail.txt";
    private static final String Passworddocumentpath = "Adminpassword.txt";
    
    // UI components
    JLabel Register; // Label for the registration title
    JLabel Emaillabel; // Label for the email field
    JLabel codelabel; // Label for the password field
    JButton Registerbutton; // Button to register a new admin
    JTextField Email; // Text field for entering the email
    JPasswordField PasswordEnter; // Password field for entering the password
    JPanel textfieldpanel; // Panel to hold the input fields
    JPanel SouthPanel; // Panel to hold the register button
    Admin admin; // Reference to the Admin object
    
    AdminRegisterPage(Admin admin) {
        this.admin = admin; // Store the passed Admin object
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window on exit
        this.setLayout(new BorderLayout()); // Use BorderLayout for the frame
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        this.setTitle("Register"); // Set the frame title
        
        // Panel to hold the Register button
        SouthPanel = new JPanel();
        SouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 150));
        
        // Title label for the registration page
        Register = new JLabel("Register", JLabel.CENTER);
        Register.setFont(new Font("Serif", Font.BOLD, 50));
        Register.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0)); // Add padding around the label
        
        // Panel to hold the email and password fields
        textfieldpanel = new JPanel();
        textfieldpanel.setLayout(new BoxLayout(textfieldpanel, BoxLayout.Y_AXIS));
        textfieldpanel.setBorder(new EmptyBorder(100, 200, 20, 100)); // Add padding around the panel
        
        // Email field and its label
        Emaillabel = new JLabel("Email: ");
        Emaillabel.setPreferredSize(new Dimension(100, 40));
        Email = new JTextField();
        Email.setPreferredSize(new Dimension(2000, 50)); 
        Email.setMaximumSize(new Dimension(2000, 50)); 
        
        // Password field and its label
        codelabel = new JLabel("Password: ");
        codelabel.setPreferredSize(new Dimension(100, 40));
        PasswordEnter = new JPasswordField();
        PasswordEnter.setEchoChar('*'); // Mask the entered characters
        PasswordEnter.setPreferredSize(new Dimension(2000, 50)); 
        PasswordEnter.setMaximumSize(new Dimension(2000, 50)); 
        
        // Register button configuration
        Registerbutton = new JButton("Register");
        Registerbutton.setLayout(new FlowLayout(FlowLayout.CENTER));
        Registerbutton.setPreferredSize(new Dimension(400, 80));
        Registerbutton.addActionListener(this); // Add action listener for the button
        
        // Add components to the input panel
        textfieldpanel.add(Emaillabel);
        textfieldpanel.add(Box.createVerticalStrut(5)); // Add spacing between components
        textfieldpanel.add(Email);
        textfieldpanel.add(Box.createVerticalStrut(20));
        textfieldpanel.add(codelabel);
        textfieldpanel.add(Box.createVerticalStrut(5));
        textfieldpanel.add(PasswordEnter);
        
        // Add Register button to the south panel
        SouthPanel.add(Registerbutton);
        
        // Add all panels to the frame
        this.add(textfieldpanel, BorderLayout.CENTER);
        this.add(Register, BorderLayout.NORTH);
        this.add(SouthPanel, BorderLayout.SOUTH);
        this.setVisible(true); // Make the frame visible
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Registerbutton) {
            // Get the email and password entered by the user
            String registerEmail = Email.getText();
            char[] passwordchararray = PasswordEnter.getPassword();
            String password = new String(passwordchararray); // Convert password to String
            
            // Write the email to the file
            try (BufferedWriter emailWriter = new BufferedWriter(new FileWriter(Emaildocumentpath, true))) {
                emailWriter.write(registerEmail + "|"); // Separate entries with a delimiter
                emailWriter.newLine(); 
            } catch (IOException e1) {
                e1.printStackTrace(); // Print error in case of an exception
            }
            
            // Write the password to the file
            try (BufferedWriter passwordWriter = new BufferedWriter(new FileWriter(Passworddocumentpath, true))) {
                passwordWriter.write(password + "|"); // Separate entries with a delimiter
                passwordWriter.newLine(); 
            } catch (IOException e1) {
                e1.printStackTrace(); // Print error in case of an exception
            }
            
            // Close the registration page and create a new Admin object
            this.dispose();    
            new Admin(registerEmail, password);
        }
    }
}
