import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserCurrentweekpage extends JFrame {
    private static final String[] COLUMN_HEADERS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private static final String[] ROW_HEADERS = {"Breakfast", "1st Lunch", "2nd Lunch (Downstairs)", "2nd Lunch (Stairs)", "Supper"};
    private JLabel titlelabel;
    private JTable table;
    private DefaultTableModel tablemodel;
    private String date;

    public UserCurrentweekpage(String date, User user) {
        this.date = date;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Schedule Table");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        String foldername = date + " week";
        String filename = foldername + "/" + date + " week final";
        File filefinal = new File(filename);
        //if week not scheduled
        if (!filefinal.exists()) {
        	//show error
            JOptionPane.showMessageDialog(
                    this,
                    "Week not scheduled",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            //end the page
            this.dispose();
            return;
        }
        //process the scheduled file
        String[][] data = processFile(filefinal);
        //label the title of the page
        titlelabel = new JLabel("Current week - Starting Date: " + date, JLabel.CENTER);
        titlelabel.setFont(new Font("Serif", Font.BOLD, 24));
        //table model configuration (Refer to Criterion B - User Current week page)
        tablemodel = new DefaultTableModel(data.length, COLUMN_HEADERS.length + 1) {
        	//Column_Headers.length + 1 because of headers
            @Override
            public String getColumnName(int column) {
                return (column == 0) ? "Meal Time" : COLUMN_HEADERS[column - 1];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
            	//In this class for User, who are not allowed to edit 
            	//Refer to Criterion A - Success Criteria
            	//disable edit for cell in the table
                return false;
            }
        };
        //enter data into JTable for visualize
        for (int i = 0; i < data.length; i++) {
            tablemodel.setValueAt(ROW_HEADERS[i], i, 0);
            for (int j = 0; j < data[i].length; j++) {
                tablemodel.setValueAt(data[i][j], i, j + 1);
            }
        }

        table = new JTable(tablemodel);
        table.setRowHeight(50);
        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Disable selection and focus to make it fully non-editable
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        this.add(titlelabel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private static String[][] processFile(File file) {
        List<String[]> lines = new ArrayList<>();
        /*use buffered reader to read the file line by line 
         * and add them into the lines array that elements separated with " "*/
        //use try and catch function to handle exception
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim().split("\\s+"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return array
        return lines.toArray(new String[0][]);
    }
}
