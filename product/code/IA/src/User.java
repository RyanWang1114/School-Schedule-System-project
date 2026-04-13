import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
public class User {//class User
	
	//User List to store user at the start of program
	//static own by class not individual user
	public static List<User> userlist = new ArrayList<>();
	//all user details
	private String UserID;
	private String Email;
	private String International_sign;
	private int Phone_number;
	private String Full_Name;
	private Image icon;
	private String password;
	
	//register with email/username and password
	public User(String Email, String password) {
		
		this.Email = Email;
		this.password = password;
		//add to user list
		userlist.add(this);
	}

	//assessors
	public String getUserID() {
		return this.UserID;
	}
	
	public String getEmail() {
		return this.Email;
	}
	
	public String getInternational_sign() {
		return this.International_sign;
	}
	
	public int Phone_number() {
		return this.Phone_number;
	}
	
	public String getName() {
		return this.Full_Name;
	}
	
	public Image getImage() {
		return this.icon;
	}
	
	public String getpassword() {
		return this.password;
	}
	
	//mutators
	public void changepassword(String password) {
		this.password = password;
	}
	
	//get user from email by scanning the arraylist
	public static User getuser(String useremail){
		User returnuser = null;
		for(int i =0;i<userlist.size();i++) {
			if(userlist.get(i).getEmail().equals(useremail)) {
				returnuser = userlist.get(i);
			}
		}
		return returnuser;
	}
	//assessor for user arraylist
	public static List<User> getuserlist(){
		return userlist;
	}
	
	//check if that user is an admin
	public Boolean isAdmin() {
		Boolean result = false;
		//get Admin array list
		List<Admin> adminlist = Admin.getAdminlist();
		for(Admin i : adminlist) {
			if(this.equals(i)) {
				result = true;
			}
		}
		return result;
		
	}

}
