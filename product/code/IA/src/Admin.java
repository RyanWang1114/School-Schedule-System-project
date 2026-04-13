import java.util.ArrayList;
import java.util.List;

public class Admin extends User{//extends User(inheritance)
	
	//Admin Arraylist to store all admin
	public static List<Admin> adminlist = new ArrayList<>();

	public Admin(String Email, String password) {
		//inherit User detail 
		super(Email, password);
		//add new admin to admin Arraylist
		adminlist.add(this);
		
	}
	//return the whole adminlist
	public static List<Admin> getAdminlist(){
		return adminlist;
	}
	
	//get admin from admin list
	public static Admin getadmin(String useremail){
		Admin returnadmin = null;
		for(int i =0;i<adminlist.size();i++) {
			if(adminlist.get(i).getEmail().equals(useremail)) {
				returnadmin = adminlist.get(i);
			}
		}
		return returnadmin;
	}
	
}
