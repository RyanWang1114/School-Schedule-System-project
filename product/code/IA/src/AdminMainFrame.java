import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AdminMainFrame extends JFrame implements ActionListener{
	
	//initialize variables and Jcomponents
	Admin admin;
	JButton create_new_Admin;
	JButton View_current_week_schedule;
	JButton Schedule_current_week;
	JLabel label;
	JPanel buttonpanel;
	JPanel southPanel;
	JButton Schedule_to_picture;
	
	//headers and
	
	private static final String[] COLUMN_HEADERS = {" ","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private static final String[] ROW_HEADERS = {"Breakfast", "1st Lunch", "2nd Lunch (Downstairs)", "2nd Lunch (Stairs)", "Supper"};

	AdminMainFrame(Admin admin){
		//initialize frame default options
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Welcome");
        
        this.admin = admin;
        String text = admin.getEmail();
        
        //header for frame
        label = new JLabel("Welcome! " + text, JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 50));
        label.setBorder(BorderFactory.createEmptyBorder(100, 0, 50, 0));
        
        //south panel
        southPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        
        //button panel for buttons
        buttonpanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonpanel.setPreferredSize(new Dimension(900,100));
        //button size 
        Dimension buttonsize = new Dimension(250,80);
        
        //define all buttons
       	create_new_Admin = new JButton("create new Admin");
        create_new_Admin.addActionListener(this);
        create_new_Admin.setPreferredSize(buttonsize);
        View_current_week_schedule = new JButton("view current week schedule");
        View_current_week_schedule.addActionListener(this);
        View_current_week_schedule.setPreferredSize(buttonsize);
        Schedule_current_week = new JButton("Schedule current week");
        Schedule_current_week.setPreferredSize(buttonsize);
        Schedule_current_week.addActionListener(this);
        
        Schedule_to_picture = new JButton("Output schedule");
        Schedule_to_picture.setPreferredSize(buttonsize);
        Schedule_to_picture.addActionListener(this);
        
        
        //add components to the frame
        buttonpanel.add(create_new_Admin);
        buttonpanel.add(View_current_week_schedule);
        buttonpanel.add(Schedule_current_week);
        buttonpanel.add(Schedule_to_picture);
        
        southPanel.add(buttonpanel,gbc);
        
        this.add(label, BorderLayout.NORTH);
        this.add(southPanel, BorderLayout.CENTER);
        this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//admin click on schedule 
		if(e.getSource() == Schedule_current_week) {
			//get current week (any date) and calculate start date
			LocalDate today = LocalDate.now();
			DayOfWeek dayinweek = today.getDayOfWeek();
			int daynumber = dayinweek.getValue();
			LocalDate startofweek = today.minusDays(daynumber - 1);
			String startofcurrentweek = startofweek.toString();
			//schedule (Figure 8.1)
			String[][] result = schedulefinalarray(startofcurrentweek);
			//display success schedulling
			JOptionPane.showMessageDialog(null, 
					"File scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
		if(e.getSource() == View_current_week_schedule) {
			new AdminChooseWeekViewPage(admin);
			
		}
		if(e.getSource() == create_new_Admin) {
			new AdminRegisterPage(this.admin);
		}
		if(e.getSource() == Schedule_to_picture) {
			//similar to the start of week date calculation mentioned in Figure 2.1
			LocalDate today = LocalDate.now();
			DayOfWeek dayinweek = today.getDayOfWeek();
			int daynumber = dayinweek.getValue();
	        LocalDate startofthisweek = today.minusDays(daynumber - 1);
			String date = startofthisweek.toString();
			String foldername = date + " week";
	        String filename = foldername + "/" + date + " week final";
	        File folder = new File(foldername);
	        File file = new File(filename);
	        //if final file exist(week scheduled)
	        if(folder.exists() && file.exists()) {
	        	String[][] timetable = processFile(new File(filename));
	        	//create a StringBuilder from java.lang
	        	StringBuilder html = new StringBuilder();
	        	//a JFileChooser for Admin to save the HTML file 
	        	JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setDialogTitle("Save");
	            fileChooser.setFileFilter(new FileNameExtensionFilter("HTML Files", "html"));
	            /*defines HTML document structure and styles the table with boardered cells,
	             * padding and centered text*/
	            html.append("<!DOCTYPE html>\n<html>\n<head>\n<title>Schedule</title>\n");
	            html.append("<style>table {border-collapse: collapse; width: 50%;}");
	            html.append("th, td {border: 1px solid black; padding: 8px; text-align: center;}</style>\n");
	            html.append("</head>\n<body>\n");
	            //Add title, row and column headers
	            html.append("<h2>Schedule week " + date +" </h2>\n<table>\n");
	            html.append("<tr>");
	            for (String colHeader : COLUMN_HEADERS) {
	                html.append("<th>").append(colHeader).append("</th>");
	            }
	            html.append("</tr>\n");
	            /*fill the table with data: this program iterate through each row. 
	             * The first column contains meal time labels and remaining column contain
	             * scheduled data*/
	            for (int i = 0; i < timetable.length; i++) {
	                html.append("<tr>");
	                html.append("<th>").append(ROW_HEADERS[i]).append("</th>");
	                for (String cell : timetable[i]) {
	                    html.append("<td>").append(cell).append("</td>");
	                }
	                html.append("</tr>\n");
	            }
	            //close the html document by ending 
	            html.append("</table>\n</body>\n</html>");
	            //open file dialogue
	            int userSelection = fileChooser.showSaveDialog(null);
	            if (userSelection == JFileChooser.APPROVE_OPTION) {
	            	//Retrieves the files path chosen by user and change the file name with .html ending
	                File fileToSave = fileChooser.getSelectedFile();
	                String filePath = fileToSave.getAbsolutePath();
	                if (!filePath.toLowerCase().endsWith(".html")) {
	                    filePath += ".html";
	                }
	                /*Uses Filewriter to write HTML content to the file 
	                 * and use try and catch to ensure file choose properly*/
	                try (FileWriter writer = new FileWriter(filePath)) {
	                    writer.write(html.toString());
	                } catch (IOException e1) {
	                    e1.printStackTrace();
	                }
	            } else {
	                System.out.println("Save operation was canceled.");
	            }

	        }
	        else {
	        	//display error when week is not scheduled yet
	        	JOptionPane.showOptionDialog(
			            null,  
			            "Week not scheduled",  
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
	
	
	private String[][] schedulefinalarray(String startOfWeekDate){
		//check if that week have already scheduled 
    	String filename = startOfWeekDate + " week final";
    	String foldername = startOfWeekDate + " week";
        File folder = new File(foldername);
    	File filefinal = new File(foldername + "/" + filename);
    	if(!filefinal.exists()) {
    		//if not scheduled
    		//get all the file exists in the folder
    		//and create a HashMap to store data for each email/Username
            File[] files = folder.listFiles();
            Map<String, String[][]> emailToDataMap = new HashMap<>();
            //loop through all files in the folder
            //if the file contains the start of week date then:
            //1. extract the email from name using extractEmailFromFileName() method
            //(in figure 8.2)
            //2. Read schedule data by user
            //3. Stores data into the HashMap
            for (File file : files) {
                if (file.getName().contains(startOfWeekDate)) { 
                    String email = extractEmailFromFileName(file.getName());
                    String[][] data = processFile(file); 
                    emailToDataMap.put(email, data); 
                }
            }
            //call organizeDataForLocations() method to combine into 1 final schedule
            //shown in figure 8.3
            String[][] finalArray = organizeDataForLocations(emailToDataMap);
            //write the final schedule 2D array into the final file and return it
            try(FileWriter writer = new FileWriter(filefinal)) {
				for(String[] row : finalArray) {
					for(String cell : row) {
						writer.write(cell + " ");
					}
					writer.write("\n");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            return finalArray;
            }
    	//if the file exist (already scheduled) then we don't have to reschedule
    	//return the original schedulled data.
    	else {
    		String[][] finalArray = processFile(filefinal);
    		return finalArray;
    	}
	}
	
	private static String extractEmailFromFileName(String fileName) {
		/*define a regular expression:
		 * ^ means start of string
		 * (.*?) means capture the shortest sequence of any characters
		 * (.*?) and \s+ ends at the first space (one or more spaces)*/
        Pattern pattern = Pattern.compile("^(.*?)\\s+");
        //matcher to apply pattern to filename 
        Matcher matcher = pattern.matcher(fileName);
        //return everything before 1st space
        if (matcher.find()) {
            return matcher.group(1);
        }
        return ""; 
    }
    
    private static String[][] processFile(File file) {
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.trim().split("\\s+");
                lines.add(columns); 
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String[][] result = new String[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            result[i] = lines.get(i);
        }
        return result;
    }
    
    private static String[][] organizeDataForLocations(Map<String, String[][]> emailToDataMap) {
    	//determines the size of the schedule by iterating through each person's availability of data.
        int numRows = 0, numCols = 0;
        for (String[][] data : emailToDataMap.values()) {
            numRows = data.length;
            numCols = data[0].length;
        }
        //creates a final 2D array for final schedule starting everything with " "
        String[][] finalArray = new String[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                finalArray[i][j] = " ";
            }
        }
        //this map assignmentCount keep track of how many times each email is assigned and set to 0 initially 
        //(Refer to Criterion A - Success Critera)
        Map<String, Integer> assignmentCount = new HashMap<>();
        for (String email : emailToDataMap.keySet()) {
            assignmentCount.put(email, 0);
        }
        //creates a Map<Integer, List<String>> to associate each location 
        //(column index with the people available to that location)
        Map<Integer, List<String>> locationToEmails = new HashMap<>();
        for (Map.Entry<String, String[][]> entry : emailToDataMap.entrySet()) {
            String email = entry.getKey();
            String[][] data = entry.getValue();
            for (int j = 0; j < numCols; j++) {
                for (int i = 0; i < numRows; i++) {
                    if ("X".equals(data[i][j])) {
                    	//if the cell is "X", the same "X" copied to the final array
                    	//because it indicate that that location doesn't need to be scheduled
                        finalArray[i][j] = "X";
                    } else if ("Y".equals(data[i][j])) {
                    	//if "Y" then the email is added to the locationToEmails:
                    	//meaning that they are available at that specific location
                        locationToEmails.computeIfAbsent(j, k -> new ArrayList<>()).add(email);
                    }
                }
            }
        }
        /*convert locationToEmails into a list of entries and sort it by number of people available in 
         * ascending order (ensures that locations with fewer available space are filled first, minimizing 
         * schedulling conflicts)*/
        List<Map.Entry<Integer, List<String>>> sortedLocations = new ArrayList<>(locationToEmails.entrySet());
        sortedLocations.sort(Comparator.comparingInt(e -> e.getValue().size()));
        /*priority queue is used to distribute work evenly.
         * The queue is sorted based on assignmentCount, 
         * ensuring that the least-assigned person is selected first*/
        PriorityQueue<String> emailQueue = new PriorityQueue<>(Comparator.comparingInt(assignmentCount::get));
        /*loop through each location in sorted order and gets the column index (locationindex) 
         * and the list of people available for that location.
         * It also clears the emailQueue and refills it with available people for that location*/
        for (Map.Entry<Integer, List<String>> locationEntry : sortedLocations) {
            int locationIndex = locationEntry.getKey();
            List<String> emailsAtLocation = locationEntry.getValue();
            emailQueue.clear();
            emailQueue.addAll(emailsAtLocation);
            //iterate through all rows to assign people at given location and skip location for "X"
            for (int i = 0; i < numRows; i++) {
                if (!"X".equals(finalArray[i][locationIndex])) { 
                	/*Polls the least-assigned person from the queue and check if they are available 
                	 * at that specific location. If so, assign them into the final array and increase their
                	 * assignment count. Breaks the loop once a person is assigned, and move onto the next row*/
                    while (!emailQueue.isEmpty()) {
                        String email = emailQueue.poll();
                        if ("Y".equals(emailToDataMap.get(email)[i][locationIndex])) {
                            finalArray[i][locationIndex] = email;
                            assignmentCount.put(email, assignmentCount.get(email) + 1);
                            break;
                        }
                    }
                    /*if no one is assigned in a location, this means that no one is available
                     * set the cell to "X" which enable Admin to edit that spot*/
                    if (finalArray[i][locationIndex].equals(" ")) { 
                        finalArray[i][locationIndex] = "X";
                    }
                }
            }
        }
        //return the final Array
        return finalArray;
    }


}
