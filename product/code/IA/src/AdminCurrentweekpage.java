import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminCurrentweekpage extends JFrame implements ActionListener {
    // Column headers representing days of the week
    private static final String[] COLUMN_HEADERS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    // Row headers representing meal times
    private static final String[] ROW_HEADERS = {"Breakfast", "1st Lunch", "2nd Lunch (Downstairs)", "2nd Lunch (Stairs)", "Supper"};
    
    private JLabel titlelabel; // Title displayed at the top of the frame
    private JTable table; // JTable to display the schedule
    private DefaultTableModel tablemodel; // Table model for managing data
    private JButton saveButton; // Button to save changes to the schedule
    private String date; // Starting date of the current week

    public AdminCurrentweekpage(String date, Admin admin) {
        this.date = date;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Schedule Table");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Define folder and file names for the schedule
        String foldername = date + " week";
        String filename = foldername + "/" + date + " week final";
        File filefinal = new File(filename);

        // Check if the file for the current week exists
        if (!filefinal.exists()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Week not scheduled",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            this.dispose();
            return;
        }

        // Load the schedule data from the file
        String[][] data = processFile(filefinal);

        // Create and style the title label
        titlelabel = new JLabel("Current week - Starting Date: " + date, JLabel.CENTER);
        titlelabel.setFont(new Font("Serif", Font.BOLD, 24));

        // Initialize the table model with the given data
        tablemodel = new DefaultTableModel(data.length, COLUMN_HEADERS.length + 1) {
            @Override
            public String getColumnName(int column) {
                return (column == 0) ? "Meal Time" : COLUMN_HEADERS[column - 1];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow admin to edit all cells except the first column (row headers)
                return column != 0;
            }
        };

        // Populate the table model with row and column data
        for (int i = 0; i < data.length; i++) {
            tablemodel.setValueAt(ROW_HEADERS[i], i, 0); // Set row headers
            for (int j = 0; j < data[i].length; j++) {
                tablemodel.setValueAt(data[i][j], i, j + 1); // Set schedule values
            }
        }

        // Create and style the JTable
        table = new JTable(tablemodel);
        table.setRowHeight(50); // Set row height for better readability
        table.setFont(new Font("Serif", Font.PLAIN, 16)); // Set font for table content
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18)); // Set font for table headers

        // Center-align table cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Add table to a scroll pane for scrolling functionality
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        // Create and configure the save button
        saveButton = new JButton("Save Changes");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.addActionListener(this);

        // Add the save button to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        // Add components to the frame
        this.add(titlelabel, BorderLayout.NORTH); // Add title label to the top
        this.add(scrollPane, BorderLayout.CENTER); // Add table to the center
        this.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom
        this.setVisible(true); // Make the frame visible
    }

    /**
     * Reads the file and processes its content into a 2D array.
     *
     * @param file The file to process.
     * @return A 2D array containing the file's data.
     */
    private static String[][] processFile(File file) {
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split each line by whitespace and add to the list
                lines.add(line.trim().split("\\s+"));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace if an error occurs
        }
        return lines.toArray(new String[0][]); // Convert the list to a 2D array
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            // Define folder and file names for saving the schedule
            String foldername = date + " week";
            String filename = foldername + "/" + date + " week final";

            try (FileWriter writer = new FileWriter(filename)) {
                // Save the updated schedule data to the file
                for (int i = 0; i < ROW_HEADERS.length; i++) {
                    for (int j = 1; j <= COLUMN_HEADERS.length; j++) {
                        writer.write(tablemodel.getValueAt(i, j) + " ");
                    }
                    writer.write("\n");
                }
                // Display a success message
                JOptionPane.showMessageDialog(this, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e1) {
                e1.printStackTrace(); // Print stack trace if an error occurs
            }
        }
    }
}
