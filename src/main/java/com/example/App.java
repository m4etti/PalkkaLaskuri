package com.example;

import com.example.data.Settings;
import com.example.data.TimeOfDayBonus;
import com.example.data.WorkShift;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;

/**
 * The App class is the main class of the Payroll application. It extends the
 * JavaFX Application class
 * and provides the user interface for the application.
 * Which includes a calendarThe UI includes a calendar, buttons to change the
 * month displayed
 * in the calendar, a button to access application settings,and a button to
 * calculate pay for a specific pay period based on user input.
 */
public class App extends Application {
    /**
     * The label for displaying the month and year in the UI.
     */
    private Label monthYearLabel;
    /**
     * The current year and month.
     */
    private YearMonth yearMonth;
    /**
     * The GridPane containing the calendar days in the UI.
     */
    private GridPane calendarGrid;
    /**
     * The path to the file where work shift data is saved.
     */
    private String shiftsFilePath;
    /**
     * An ArrayList of WorkShift objects representing the work shifts.
     */
    private ArrayList<WorkShift> shifts = new ArrayList<>();
    /**
     * The path to the file where application settings are saved.
     */
    private String settingsFilePath;
    /**
     * An object representing the application settings.
     */
    private Settings settings;

    /**
     * The start method is called after the init method when the application is
     * launched. It sets up
     * the user interface components, including the calendar, buttons, and text
     * fields, and sets up event
     * handlers for the buttons. It also loads data from files and creates default
     * settings if none are
     * found.
     * 
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // aseta polut
        shiftsFilePath = "src/main/resources/com/example/shifts.ser";
        settingsFilePath = "src/main/resources/com/example/settings.ser";
        // lataa vuorot tiedostosta
        loadData();

        // alusta UI komponentit
        calendarGrid = new GridPane();
        monthYearLabel = new Label();
        yearMonth = YearMonth.now();

        // aseta kuukausi/vuosi
        updateMonthYearLabel(yearMonth);

        // luo kalenteri grid
        buildCalendarGrid(yearMonth);

        // kuukauden vaihto napit
        Button previousMonth = new Button("<");
        Button nextMonth = new Button(">");
        previousMonth.setOnAction(e -> changeMonth(-1));
        nextMonth.setOnAction(e -> changeMonth(1));
        HBox changeMonth = new HBox(previousMonth, nextMonth);

        // asetukset nappi
        Button settingsButton = new Button("Asetukset");
        settingsButton.setOnAction(e -> {
            SettingsWindow settingsWindow = new SettingsWindow(this.settings);
            settingsWindow.showAndWait();
            this.settings = settingsWindow.getSettings();
        });

        // Palkkajakso
        Label payPeriodLabel = new Label("Palkka päiviltä:");
        TextField payPeriodStartInput = new TextField();
        payPeriodStartInput.setPrefWidth(30);
        Label dashLabel = new Label("-");
        TextField payPeriodEndInput = new TextField();
        payPeriodEndInput.setPrefWidth(30);

        Button calcPayButton = new Button("Laske palkka");
        calcPayButton.setOnAction(e -> {
            // Avaa palkka ikkuna
            PayWindow payWindow = new PayWindow(this.shifts, this.yearMonth,
                    Integer.parseInt(payPeriodStartInput.getText()),
                    Integer.parseInt(payPeriodEndInput.getText()), settings.getTax());
            payWindow.showAndWait();
        });

        HBox payPeriodHBox = new HBox(payPeriodStartInput, dashLabel, payPeriodEndInput, calcPayButton);
        payPeriodHBox.setSpacing(5);

        // Lisää UI komponentit pääasetteluun
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(monthYearLabel, changeMonth, calendarGrid, settingsButton, payPeriodLabel,
                payPeriodHBox);
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(10));

        // Aseta pääasettelu näkymän sisällöksi
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Palkka Laskuri");
        primaryStage.show();
    }

    /**
     * This method is called when the application is stopped and saves data to files
     * usiing serialization.
     */
    @Override
    public void stop() {
        // Tallenna data tiedostoon suljettaessa.
        // Vuorot
        try (FileOutputStream fileOut = new FileOutputStream(this.shiftsFilePath);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this.shifts);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Asetukset
        try (FileOutputStream fileOut = new FileOutputStream(this.settingsFilePath);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this.settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads data from files using deserializing.
     */
    private void loadData() {
        // Vuorot
        try (FileInputStream fileIn = new FileInputStream(this.shiftsFilePath);
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
            this.shifts = (ArrayList<WorkShift>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Asetukset
        try (FileInputStream fileIn2 = new FileInputStream(this.settingsFilePath);
                ObjectInputStream in2 = new ObjectInputStream(fileIn2)) {
            this.settings = (Settings) in2.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (this.settings == null) {
            TimeOfDayBonus evening = new TimeOfDayBonus(3.4, LocalTime.of(18, 0), LocalTime.of(22, 0));
            TimeOfDayBonus nigth = new TimeOfDayBonus(4.3, LocalTime.of(22, 0), LocalTime.of(6, 0));
            this.settings = new Settings(12.0, 2.0, evening, nigth, 20);
        }
    }

    /**
     * Changes the month displayed on the calendar by a specified number of months.
     *
     * @param i the number of months to change the calendar by; a negative value
     *          changes the month backwards
     */
    private void changeMonth(int i) {
        yearMonth = yearMonth.plusMonths(i);
        updateMonthYearLabel(yearMonth);
        buildCalendarGrid(yearMonth);
    }

    /**
     * Updates the label displaying the month and year.
     *
     * @param yearMonth the year and month to display
     */
    private void updateMonthYearLabel(YearMonth yearMonth) {
        String monthYearString = yearMonth.getMonth().toString() + " " + yearMonth.getYear();
        monthYearLabel.setText(monthYearString);
    }

    /**
     * Builds a grid to display the days of the month in a calendar format.
     * The grid consists of a header row showing the abbreviated names of the days
     * of the week (Monday, Tuesday, etc.), followed by a grid of buttons
     * representing
     * each day of the month. If the day has a work shift scheduled, the button is
     * colored green.
     * Clicking a button opens a window for editing or creating a work shift for
     * that day.
     * 
     * @param yearMonth the year and month to display in the calendar
     */
    private void buildCalendarGrid(YearMonth yearMonth) {
        // Alusta grid
        this.calendarGrid.getChildren().clear();
        this.calendarGrid.setHgap(10);
        this.calendarGrid.setVgap(10);

        // Luo otsikot päiville
        String[] daysOfWeek = { "Ma", "Ti", "Ke", "To", "Pe", "La", "Su" };
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label label = new Label(daysOfWeek[i]);
            calendarGrid.add(label, i, 0);
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
                    this.calendarGrid.add(label, col, row);
                } else {
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

                    button.setOnAction(e -> {
                        ShiftEditingWindow shiftEditingWindow = new ShiftEditingWindow(date, shiftIndex[0],
                                this.shifts, this.settings, this::onShiftEditingWindowClosed);
                        shiftEditingWindow.showAndWait();
                        this.shifts = shiftEditingWindow.getShifts();
                    });
                    this.calendarGrid.add(button, col, row);
                    dayOfMonth++;
                }
            }
        }
    }

    /**
     * This method is called when the ShiftEditingWindow is closed to update the
     * calendar display.
     */
    private void onShiftEditingWindowClosed() {
        buildCalendarGrid(this.yearMonth);
    }

    /**
     * Main method of the app that launches the app.
     * 
     * @param args An array of strings containing any command line arguments passed
     *             to the application during launch.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
