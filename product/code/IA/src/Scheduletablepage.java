import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Scheduletablepage extends JFrame implements ActionListener{
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private JButton saveButton;
    private JPanel buttonpanel;
    private JPanel panel;
    private String[][] timeSlots;
    private String date;
    private int weekNumber;
    private String weekAB;
    private JLabel titlelabel;
    private JPanel titlepanel;
    private JLabel noticelabel;
    private JPanel noticepanel;
    private User user;

    public Scheduletablepage(String date, int weekNumber, String weekAB, User user) {
    	this.user = user;
        this.date = date;
        this.weekNumber = weekNumber;
        this.weekAB = weekAB;
        this.timeSlots = new String[5][7];

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Schedule Table");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        
        //initialize JLabel for heading of the page
        titlelabel = new JLabel("Schedule New Week - Week " + weekNumber + " (" + weekAB + ") - Starting Date: " + date, JLabel.CENTER);
        titlelabel.setFont(new Font("Serif", Font.BOLD, 24));
        titlepanel = new JPanel(new BorderLayout());
        titlepanel.add(titlelabel, BorderLayout.CENTER);
        
        //initialize JLabel for notice
        noticelabel = new JLabel("Notice: Sunday only allows brunch and tea duty for teachers", JLabel.CENTER);
        noticelabel.setFont(new Font("Serif", Font.BOLD, 16));
        noticelabel.setForeground(Color.RED);
        noticepanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        noticepanel.add(noticelabel);

        //row and column headers
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", 
        		"Friday", "Saturday", "Sunday"};
        String[] times = {"Breakfast", "1st Lunch", "2nd Lunch (Downstairs)", 
        		"2nd Lunch (Stairs)", "Supper"};
        timeSlots = new String[times.length][days.length];
        /*table model is initialized with rows corresponding to times.lengh
         * and columns corresponding to days.length+1
         * (additional column for time slot labels)*/
        tableModel = new DefaultTableModel(times.length, days.length + 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 7 && (row == 0 || row == 2 || row == 3)) {
                    return false; 
                }
                return false;
            }
        };
        //set column headers for table
        tableModel.setColumnIdentifiers(
        		new String[]{"", "Monday", "Tuesday", "Wednesday", 
        				"Thursday", "Friday", "Saturday", "Sunday"});
        /*Populates Jtable with initial values, setting time slots
         * int he first column and marking it "Available" across the grid*/
        for (int i = 0; i < times.length; i++) {
            tableModel.setValueAt(times[i], i, 0); 
            for (int j = 1; j <= days.length; j++) {
                if (j == 7 && (i == 0 || i == 2 || i == 3)) {
                    tableModel.setValueAt("", i, j);
                } else {
                    tableModel.setValueAt("Available", i, j);
                }
            }
        }
        scheduleTable = new JTable(tableModel);
        scheduleTable.setRowHeight(80);
        //modify how data is displayed in table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //applies a custom cell renderer (refer to figure 4.1) to the table
        scheduleTable.setDefaultRenderer(Object.class, new CustomCellRenderer());
        //add a mouse listener when a user clicks on certain cell in the table
        scheduleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                int row = scheduleTable.rowAtPoint(evt.getPoint());
                int col = scheduleTable.columnAtPoint(evt.getPoint());
                /*prevent user from clicking extra time slot on Sunday*/
                if (col == 7 && (row == 0 || row == 2 || row == 3)) {
                    return; 
                }
                /* Toggle between "Unavailable" and "Available" for user when they are clicking 
                 * on the table representing their availability on that specific time slot
                 * (Refer to Criterion A - Success Criteria)
                 * (Refer to Criterion B - Design of Table)*/
                if (col > 0 && row >= 0) {
                    String currentValue = (String) tableModel.getValueAt(row, col);
                    if ("Available".equals(currentValue)) {
                        tableModel.setValueAt("Unavailable", row, col);
                    } else {
                        tableModel.setValueAt("Available", row, col);
                    }
                    scheduleTable.repaint();
                }
            }
        });

        
        saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(400, 80));
        saveButton.addActionListener(this);
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(saveButton);
        
        
        buttonpanel = new JPanel();
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS));
        buttonpanel.add(noticepanel);
        buttonpanel.add(Box.createVerticalStrut(10));
        buttonpanel.add(panel);
        
        this.setLayout(new BorderLayout());
        this.add(titlepanel, BorderLayout.NORTH);
        this.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        this.add(buttonpanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }
    
	private class CustomCellRenderer extends DefaultTableCellRenderer {
        @Override//override the superclass to get default rendering for the cell
        public Component getTableCellRendererComponent(JTable table, 
        		Object value, boolean isSelected, boolean hasFocus, 
        		int row, int column) {
            Component cell = super.getTableCellRendererComponent(
            		table, value, isSelected, hasFocus, row, column);
            
            if (column == 7 && (row == 0 || row == 2 || row == 3)) {
            	//if sunday and unchangeable time slot set it always to grey
                cell.setBackground(Color.LIGHT_GRAY); 
                cell.setForeground(Color.DARK_GRAY); 
            } else if ("Available".equals(value)) {
            	//if available turn green
                cell.setBackground(Color.GREEN);
                cell.setForeground(Color.BLACK);
            } else if ("Unavailable".equals(value)) {
            	//if unavailable turn red
                cell.setBackground(Color.RED);
                cell.setForeground(Color.WHITE);
            } else {
            	//default colour
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
            }
            return cell;
        }
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == saveButton) {
			String[][] result = new String[5][7];
			for (int i = 0; i < timeSlots.length; i++) {
	            for (int j = 0; j < 7; j++) {
	            	if(scheduleTable.getValueAt(i,j+1).equals("Unavailable")) {
	            		//if user unavailable, put 'N' in the txt file
	            		result[i][j] = "N";
	            	}
	            	else if(scheduleTable.getValueAt(i, j+1).equals("Available")){
	            		//if user available, put 'Y'
	            		result[i][j] = "Y";
	            	}
	            	else {
	            		//else uneditable, put 'X'
	            		result[i][j] = "X";
	            	}
	            }
	        }
			//set folder and file names
			String foldername = date + " week";
			String filename = user.getEmail() + " " + date + " week";
			File folder = new File(foldername);
			//find the path to folder
			//if folder doesn't exist, create one
			if(!folder.exists()) {
				folder.mkdir();
			}
			/*create a txt file for schedule data storage. 
			 * File name contains start of week date and username 
			 * so easy for system to fetch data later*/
			File file = new File(foldername + "/" + filename);
			try(FileWriter writer = new FileWriter(file)) {
				for(String[] row : result) {
					for(String cell : row) {
						//use " " to separate values
						writer.write(cell + " ");
					}
					// next line for next row
					writer.write("\n");
				}
				//display successful saved file
				JOptionPane.showMessageDialog(
			            null,                          
			            "File saved successfully",      
			            "Success",                      
			            JOptionPane.INFORMATION_MESSAGE
			        );
				//user try and catch for exception handling
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
}
