import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.swing.*;

//whole class similar to the ChooseWeekViewPage for User but for admin instead
public class AdminChooseWeekViewPage extends JFrame implements ActionListener{
    private Admin admin;
    private JButton currentWeekButton;
    private JButton nextWeekButton;
    private JButton goBackButton;
    private JPanel buttonpanel;

    
    public AdminChooseWeekViewPage(Admin admin) {
        this.admin = admin;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Choose Week");

        JLabel titleLabel = new JLabel("Choose a Week to View", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
   
        goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Arial", Font.PLAIN, 16));
        goBackButton.addActionListener(this);


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(goBackButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        
        currentWeekButton = new JButton("Current Week");
        currentWeekButton.setFont(new Font("Arial", Font.BOLD, 18));
        currentWeekButton.setPreferredSize(new Dimension(200, 80));
        currentWeekButton.addActionListener(this);
        
        
        nextWeekButton = new JButton("Next Week");
        nextWeekButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextWeekButton.setPreferredSize(new Dimension(200, 80));
        nextWeekButton.addActionListener(this);
        
        buttonpanel = new JPanel();
        buttonpanel.add(currentWeekButton);
        buttonpanel.add(nextWeekButton);
        
        this.add(topPanel, BorderLayout.NORTH);
        this.add(buttonpanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == goBackButton) {
			this.dispose();
			new AdminMainFrame(admin);
		}
		if(e.getSource() == currentWeekButton) {
			LocalDate today = LocalDate.now();
			DayOfWeek dayinweek = today.getDayOfWeek();
			int daynumber = dayinweek.getValue();
	        LocalDate startofthisweek = today.minusDays(daynumber - 1);
			
			String Startofcurrentweekdate = startofthisweek.toString();
			
			new AdminCurrentweekpage(Startofcurrentweekdate, admin);
		}
		if(e.getSource() == nextWeekButton) {
			LocalDate today = LocalDate.now();
			DayOfWeek dayinweek = today.getDayOfWeek();
			int daynumber = dayinweek.getValue();
			LocalDate startofnextweek = today.plusDays(7);
			startofnextweek = startofnextweek.minusDays(daynumber - 1);
			String Startofcurrentweekdate = startofnextweek.toString();
			
			new AdminCurrentweekpage(Startofcurrentweekdate, admin);
		}
	}
	
}
