package com.example;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.ArrayList;

import com.example.data.Settings;
import com.example.data.WorkShift;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * A custom GridPane class that displays the days of the month in a calendar
 * format.
 * The grid consists of a header row showing the abbreviated names of the days
 * of the week (Monday, Tuesday, etc.), followed by a grid of buttons
 * representing each day of the month.
 * If the day has a work shift scheduled, the button is colored green.
 * Clicking a button opens a window for editing or creating a work shift for
 * that day.
 * 
 * @author Matti Voutilainen
 */
public class CalendarGrid extends GridPane {
    /**
     * An ArrayList of WorkShift objects to be displayed on the calendar grid.
     */
    private ArrayList<WorkShift> shifts;
    /**
     * a Settings object.
     */
    private Settings settings;
    /**
     * A YearMonth object representing the month to be displayed on the calendar
     * grid.
     */
    private YearMonth yearMonth;

    /**
     * Constructs a new CalendarGrid object with the given ArrayList of WorkShifts,
     * Settings object, and YearMonth object.
     * Calls buildCalendarGrid() method to build the calendar grid.
     * 
     * @param shifts    An ArrayList of WorkShift objects to be displayed on the
     *                  calendar grid
     * @param settings  A Settings object.
     * @param yearMonth A YearMonth object representing the month to be displayed on
     *                  the calendar grid.
     */
    public CalendarGrid(ArrayList<WorkShift> shifts, Settings settings, YearMonth yearMonth) {
        this.shifts = shifts;
        this.settings = settings;
        this.yearMonth = yearMonth;

        buildCalendarGrid();
    }

    /**
     * Changes the month displayed on the calendar grid to the specified YearMonth
     * object, and calls buildCalendarGrid() method
     * to rebuild the calendar grid with the new month.
     * 
     * @param yearMonth a YearMonth object representing the month to be displayed on
     *                  the calendar grid
     */
    public void changeMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
        buildCalendarGrid();
    }

    /**
     * Builds the calendar grid and adds it to the GridPane. The grid displays the
     * days of the month in a 7x6 layout,
     * with buttons for each day that can be clicked to edit work shifts. Also
     * displays the days of the week as labels
     * at the top of the grid.
     */
    private void buildCalendarGrid() {
        // Alusta grid
        this.getChildren().clear();
        this.setHgap(10);
        this.setVgap(10);

        // Luo otsikot päiville
        String[] daysOfWeek = { "Ma", "Ti", "Ke", "To", "Pe", "La", "Su" };
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label label = new Label(daysOfWeek[i]);
            this.add(label, i, 0);
        }

        // Lake päivien määrä
        int daysInMonth = yearMonth.lengthOfMonth();

        // Kuun ensinmäinen päivämäärä
        LocalDate firstDayOfMonth = yearMonth.atDay(1);

        // Kuun ensinmäinen viikonpäivä
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        // Lisää napit kaikille päiville
        int dayOfMonth = 1;
        for (int row = 1; row <= 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (row == 1 && col < firstDayOfWeek - 1 || dayOfMonth > daysInMonth) {
                    // Lisää tyhjää kuun alkuun ja loppuun
                    Label label = new Label("");
                    this.add(label, col, row);
                } else {
                    Button button = createButtonForDate(dayOfMonth);
                    this.add(button, col, row);
                    dayOfMonth++;
                }
            }
        }
    }

    /**
     * Creates a JavaFX button for the given day of the month.
     * 
     * @param dayOfMonth The day of the month for which the button is created.
     * @return A JavaFX Button object representing the specified day of the month.
     */
    private Button createButtonForDate(int dayOfMonth) {
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), dayOfMonth);

        // Lisää nappi päivälle
        Button button = new Button(Integer.toString(dayOfMonth));
        button.setPrefSize(40, 30);
        int[] shiftIndex = { -1 };
        // onko vuoroa tälle päivälle
        for (int i = 0; i < this.shifts.size(); i++) {
            if (date.equals(this.shifts.get(i).getStart().toLocalDate())) {
                button.setStyle("-fx-background-color: green;");
                shiftIndex[0] = i;
                break;
            }
        }

        // napin toiminto
        button.setOnAction(e -> {
            ShiftEditingWindow shiftEditingWindow = new ShiftEditingWindow(date, shiftIndex[0],
                    this.shifts, this.settings, this::onShiftEditingWindowClosed);
            shiftEditingWindow.showAndWait();
            this.shifts = shiftEditingWindow.getShifts();
        });

        return button;
    }

    /**
     * This method is called when the ShiftEditingWindow is closed to update the
     * calendar display.
     */
    private void onShiftEditingWindowClosed() {
        buildCalendarGrid();
    }
}
