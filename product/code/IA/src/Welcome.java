import java.awt.BorderLayout;
import java.awt.Color;
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

public class Welcome extends JFrame implements ActionListener{
	//extends JFrame to display frame
	//extends ActionListener for implementation of buttons
	
	//define document path
	private static final String Emaildocumentpath = "UserEmail.txt";
	private static final String Passworddocumentpath = "password.txt";
	private static final String AdminEmaildocumentpath = "AdminEmail.txt";
	private static final String AdminPassworddocumentpath = "Adminpassword.txt";
	
	//define key components for frame
	JPanel SouthPanel;
	JPanel Loginbutton;
	JPanel textfield;
	JPanel buttons;
	JLabel Welcome;
	JLabel Emaillabel;
	JLabel Passwordlabel;
	JTextField Email;
	JPasswordField PasswordEnter;
	JButton ForgotPassword;
	JButton Register;
	JButton Login;
	//user input 
	String INPUT;
	String Password;
	
	public Welcome() {
		//store user and admin into Array list
		List<String> useremail = getList(Emaildocumentpath);
		List<String> password = getList(Passworddocumentpath);
		
		
		for(int i = 0;i<useremail.size();i++) {
			new User(useremail.get(i),password.get(i));
		}
		
		List<String> adminemail = getList(AdminEmaildocumentpath);
		List<String> adminpassword = getList(AdminPassworddocumentpath);
		for(int i = 0;i<adminemail.size();i++) {
			new Admin(adminemail.get(i), adminpassword.get(i));
		}
		//Frame default options
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Welcome!");
        
        //welcome label on top of the frame
        Welcome = new JLabel("Welcome!", JLabel.CENTER);
        Welcome.setFont(new Font("Serif", Font.BOLD, 50));
        Welcome.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        
        //a Panel for user input(named textfield)
        textfield = new JPanel();
        textfield.setLayout(new BoxLayout(textfield, BoxLayout.Y_AXIS));
        textfield.setBorder(new EmptyBorder(100, 200, 20, 100));
        
        //Panel for login button
        Loginbutton = new JPanel();
        Loginbutton.setLayout(new FlowLayout(FlowLayout.CENTER,10,40));
        Loginbutton.setPreferredSize(new Dimension(200,300));
        
        //panel for buttons
        buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER,10,50));
        buttons.setPreferredSize(new Dimension(200,150));
        
        //Label of email/username and password to give user instructions
        Emaillabel = new JLabel("Email: ");
        Emaillabel.setPreferredSize(new Dimension(100,40));
        Passwordlabel = new JLabel("Password: ");
        Passwordlabel.setPreferredSize(new Dimension(100,40));
        
        //Register button and configuration
        Register = new JButton("Register");	
        Register.setFont(new Font("Serif", Font.BOLD, 20));
        Register.setForeground(Color.BLACK);
        //invisible border
        Register.setBorderPainted(false);
        Register.setContentAreaFilled(false); 
        //enable instruction after button is clicked
        Register.addActionListener(this);
        
        //Change Password button and configuration
        ForgotPassword = new JButton("Change password");
        ForgotPassword.setFont(new Font("Serif", Font.BOLD, 20));
        ForgotPassword.setForeground(Color.BLACK);
        ForgotPassword.setBorderPainted(false);
        ForgotPassword.setContentAreaFilled(false); 
        //enable instruction after button is clicked
        ForgotPassword.addActionListener(this);
        
        //button for login
        Login = new JButton("Login");
        Login.setLayout(new FlowLayout(FlowLayout.CENTER));
        Login.setPreferredSize(new Dimension(400,80));
        Login.addActionListener(this);
        
        //JPasswordField for password enter
        PasswordEnter = new JPasswordField();
        //keep privacy when user is entering password
        PasswordEnter.setEchoChar('*');
        PasswordEnter.setPreferredSize(new Dimension(2000, 50));
        PasswordEnter.setMaximumSize(new Dimension(2000, 50)); 
        
        //textfield for Email enter
        Email = new JTextField();
        Email.setPreferredSize(new Dimension(2000, 50)); 
        Email.setMaximumSize(new Dimension(2000, 50)); 
        
        //south panel for buttons 
        SouthPanel = new JPanel();
        SouthPanel.setLayout(new BoxLayout(SouthPanel, BoxLayout.Y_AXIS));
        
        textfield.add(Emaillabel);
        //distance between label and textfield
        textfield.add(Box.createVerticalStrut(5)); 
        textfield.add(Email);
        textfield.add(Box.createVerticalStrut(10)); 
        textfield.add(Passwordlabel);
        textfield.add(Box.createVerticalStrut(5)); 
        textfield.add(PasswordEnter);
        //add buttons to panels
        buttons.add(Register);
        buttons.add(ForgotPassword);
        Loginbutton.add(Login);
        SouthPanel.add(Loginbutton);
        SouthPanel.add(buttons);
        //set location for panels
        this.add(Welcome, BorderLayout.NORTH);
        this.add(textfield, BorderLayout.CENTER);
        this.add(SouthPanel, BorderLayout.SOUTH);
        this.setVisible(true);
	}

	@Override //action performed after button is clicked
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ForgotPassword) {
			//when a change password button is clicked by user
			this.dispose();
			//close current page and open the change password page
			new ChangePasswordPage();
		}
		else if(e.getSource() == Register) {
			//when register button is clicked
			//close current page and open a Register page
			this.dispose();
			new RegisterPage();
		}
		else if(e.getSource() == Login) {
			//get user input (use character array to keep the password textfield private)
			char[] passwordchararray = PasswordEnter.getPassword();
			String password = new String(passwordchararray);
			INPUT = Email.getText();
			//check if login detail accepted (refer to figure 1.1 check() method)
			boolean valid = check(AdminEmaildocumentpath, AdminPassworddocumentpath, password, INPUT) 
					|| check(Emaildocumentpath, Passworddocumentpath, password, INPUT);
			if(valid) {
				//differentiate between User and Admin
				User currentuser = User.getuser(INPUT);
				if(currentuser.isAdmin()) {
					//open an Admin Main Frame
					Admin currentadmin = Admin.getadmin(INPUT);
					new AdminMainFrame(currentadmin);
				}
				else {
					//open an User Main Frame
					new MainFrame(currentuser);
				}
				//close Welcome Page
				this.dispose();
			}
			else {
				//display input error
				JOptionPane.showOptionDialog(
			            null,  
			            "incorrect email or password",  
			            "error",  
			            JOptionPane.DEFAULT_OPTION, 
			            JOptionPane.ERROR_MESSAGE, 
			            null, 
			            new Object[] { "OK" },
			            "OK" 
			        );
			}
		}
		
	}
	
	private List<String> getList(String path) {
		//User dynamic data structure Array List to append data
	    List<String> list = new ArrayList<>();
	    //user buffered reader from java.io to extract data from txt file
	    //user try and catch to handle exception
	    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
	        String line;
	        /* write user or admin Password into Array split with the character
	         "|" and handle exception*/
	        while ((line = reader.readLine()) != null) {
	            String[] items = line.split("\\|");
	            for (String item : items) {
	                if (!item.trim().isEmpty()) {
	                    list.add(item.trim());
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	public boolean check(String emailPath, String passwordPath, String passwordEntered, String emailEntered) {
	    List<String> emailList = getList(emailPath);
	    List<String> passwordList = getList(passwordPath);
	    //check if the entered email and password match with any of the registered email and password
	    for (int i = 0; i < emailList.size(); i++) {
	        String storedEmail = emailList.get(i);
	        String storedPassword = passwordList.get(i);
	        if (emailEntered.equals(storedEmail) && passwordEntered.equals(storedPassword)) {
	            return true;
	        }
	    }
	    return false;
	}
}
