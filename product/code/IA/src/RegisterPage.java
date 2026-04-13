import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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

//register page for user
public class RegisterPage extends JFrame implements ActionListener {
	//extends JFrame to display frame
	//extends ActionListener for implementation of buttons
	
	//define document path
    private static final String Emaildocumentpath = "UserEmail.txt";
    private static final String Passworddocumentpath = "password.txt";

    //initialize frame components
    JLabel Register;
    JLabel Emaillabel;
    JLabel codelabel;
    JButton Registerbutton;
    JTextField Email;
    JPasswordField PasswordEnter;
    JPanel textfieldpanel;
    JPanel SouthPanel;

    RegisterPage() {
    	//initialize default options for frame
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Register");

        //panel for register button
        SouthPanel = new JPanel();
        SouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 150));

        //Big register label
        Register = new JLabel("Register", JLabel.CENTER);
        Register.setFont(new Font("Serif", Font.BOLD, 50));
        Register.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));

        //user input panel 
        textfieldpanel = new JPanel();
        textfieldpanel.setLayout(new BoxLayout(textfieldpanel, BoxLayout.Y_AXIS));
        textfieldpanel.setBorder(new EmptyBorder(100, 200, 20, 100));

        //label to give user instructions
        Emaillabel = new JLabel("Email: ");
        Emaillabel.setPreferredSize(new Dimension(100, 40));
        codelabel = new JLabel("Password: ");
        codelabel.setPreferredSize(new Dimension(100, 40));

        //register button configuration
        Registerbutton = new JButton("Register");
        Registerbutton.setLayout(new FlowLayout(FlowLayout.CENTER));
        Registerbutton.setPreferredSize(new Dimension(400, 80));
        Registerbutton.addActionListener(this);

        //textfield for email/username enter
        Email = new JTextField();
        Email.setPreferredSize(new Dimension(2000, 50));
        Email.setMaximumSize(new Dimension(2000, 50));
        
        //password field configuration
        PasswordEnter = new JPasswordField();
        PasswordEnter.setEchoChar('*');
        PasswordEnter.setPreferredSize(new Dimension(2000, 50));
        PasswordEnter.setMaximumSize(new Dimension(2000, 50));

        //add labels and textfield/passwordfield to textfieldpanel
        textfieldpanel.add(Emaillabel);
        textfieldpanel.add(Box.createVerticalStrut(5));
        textfieldpanel.add(Email);
        textfieldpanel.add(Box.createVerticalStrut(20));
        textfieldpanel.add(codelabel);
        textfieldpanel.add(Box.createVerticalStrut(5));
        textfieldpanel.add(PasswordEnter);

        //register button added 
        SouthPanel.add(Registerbutton);

        //add all panel into frame
        this.add(textfieldpanel, BorderLayout.CENTER);
        this.add(Register, BorderLayout.NORTH);
        this.add(SouthPanel, BorderLayout.SOUTH);
        //set visible
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Registerbutton) {
            String registerEmail = Email.getText().trim();
            char[] passwordchararray = PasswordEnter.getPassword();
            String password = new String(passwordchararray);
            //check if email is already registered in the data
            if (isEmailRegistered(registerEmail)) {
            	//If registered display frame of error message
                JOptionPane.showMessageDialog(this, 
                		"This email is already registered!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
            	//use Buffered Writer to write the register email and password
                try (BufferedWriter emailWriter = new BufferedWriter(new FileWriter(Emaildocumentpath, true))) {
                    emailWriter.write(registerEmail + "|");
                    emailWriter.newLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try (BufferedWriter passwordWriter = new BufferedWriter(new FileWriter(Passworddocumentpath, true))) {
                    passwordWriter.write(password + "|");
                    passwordWriter.newLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //close Register frame
                this.dispose();
                //back to Welcome frame
                new User(registerEmail, password);
                new Welcome();
            }
        }
    }

    //check if email is registered
    private boolean isEmailRegistered(String email) {
    	//user buffered Reader to loop through the email to check repetition
    	//user try and catch to handle exceptions
        try (BufferedReader reader = new BufferedReader(new FileReader(Emaildocumentpath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(email + "|")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
