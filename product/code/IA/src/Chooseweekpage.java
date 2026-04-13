import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

public class Chooseweekpage extends JFrame {
    private JTable schedule;
    private DefaultTableModel tableModel;
    private JButton goback;
    private JLabel scheduleWeek;
    private User user;

    public Chooseweekpage(User user) {
        this.user = user;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Ensures the application closes when the window is closed
        this.setLayout(new BorderLayout()); // Sets the layout to BorderLayout
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the frame to cover the entire screen
        this.setTitle("Choose Week:"); // Sets the title of the window

        // Sets up the label at the top of the frame
        scheduleWeek = new JLabel("Schedule New Week", JLabel.CENTER);
        scheduleWeek.setFont(new Font("Serif", Font.BOLD, 50)); // Customizes font style and size

        // Back button to return to the previous page
        goback = new JButton("<- Back");
        goback.addActionListener(e -> {
            this.dispose(); // Closes the current frame
            new MainFrame(user); // Opens the main frame
        });

        // Panel to hold the back button and title label
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(goback, BorderLayout.WEST); // Adds the back button to the left
        topPanel.add(scheduleWeek, BorderLayout.CENTER); // Centers the title label

        // Initializes the table model with column headers
        tableModel = new DefaultTableModel(new Object[]{"Date", "Week Number", "Week A/B", "Open Schedule"}, 0);
        schedule = new JTable(tableModel);
        schedule.setRowHeight(40); // Sets the height of each row
        schedule.setDefaultEditor(Object.class, null); // Makes the table non-editable

        generateScheduleWeekData(); // Populates the table with schedule data

        // Sets custom renderers and editors for the "Open Schedule" column to use buttons
        schedule.getColumn("Open Schedule").setCellRenderer(new ButtonRenderer());
        schedule.getColumn("Open Schedule").setCellEditor(new ButtonEditor(new JCheckBox()));

        // Adds the top panel and table to the frame
        this.add(topPanel, BorderLayout.NORTH); // Adds the panel at the top
        this.add(new JScrollPane(schedule), BorderLayout.SOUTH); // Adds the table with scroll support
        this.setVisible(true); // Makes the frame visible
    }

    private void generateScheduleWeekData() {
        // Formats dates into "yyyy-MM-dd" format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Gets the start date of the next week
        Calendar calendar = Calendar.getInstance();
        LocalDate today = LocalDate.now();
        DayOfWeek dayinweek = today.getDayOfWeek(); // Gets the day of the week (e.g., Monday, Tuesday)
        int daynumber = dayinweek.getValue(); // Gets the day number (Monday = 1, ..., Sunday = 7)
        today = today.plusDays(7); // Moves to the same day in the next week
        today = today.minusDays(daynumber - 1); // Moves to the start of that week
        int year = today.getYear();
        int month = today.getMonthValue() - 1; // Calendar uses 0-based months
        int day = today.getDayOfMonth();
        calendar.set(year, month, day); // Sets the start date in the calendar

        // Generates schedule data for the next 4 weeks
        for (int i = 1; i <= 4; i++) {
            String date = sdf.format(calendar.getTime()); // Formats the current date
            int weekNumber = i; // Week number (1 to 4)
            String abWeek = (i % 2 == 1) ? "A" : "B"; // Alternates between "A" and "B" weeks
            JButton button = new JButton("-> Schedule"); // Button for opening the schedule
            tableModel.addRow(new Object[]{date, weekNumber, abWeek, button}); // Adds a row to the table
            calendar.add(Calendar.DAY_OF_MONTH, 7); // Moves to the next week
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        /* Extends DefaultCellEditor (responsible for editing cell 
         * when a user interacts with it inside a JTable)
         */
        private JButton button;
        private String date;
        private int weekNumber;
        private String weekAB;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true); // Ensures the button is visible
            /* Adds ActionListener to the button to allow it to open
             * a schedule table page for the user with the selected data
             */
            button.addActionListener(e -> {
                new Scheduletablepage(date, weekNumber, weekAB, user);
            });
        }

        /* Retrieves data from the row and column of the table when a 
         * button is clicked, and prepares the button component for editing
         */
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                button.setText(((JButton) value).getText()); // Sets the button text
                date = (String) table.getValueAt(row, 0); // Gets the date from the table
                weekNumber = (Integer) table.getValueAt(row, 1); // Gets the week number
                weekAB = (String) table.getValueAt(row, 2); // Gets the week type (A/B)
            }
            return button; // Returns the button component
        }

        @Override
        public Object getCellEditorValue() {
            return button; // Returns the button as the editor value
        }
    }

    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        // Extends TableCellRenderer to display buttons in a table
        public ButtonRenderer() {
            setOpaque(true); // Ensures the button is visible
        }

        /* Determines which component (e.g., a button) will be displayed 
         * in the table cell
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                return (JButton) value; // Returns the button for rendering
            }
            return this; // Default component if not a button
        }
    }
}
