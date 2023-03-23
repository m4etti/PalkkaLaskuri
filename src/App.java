import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class App extends Application {

    private Label monthYearLabel;
    private YearMonth yearMonth;
    private GridPane calendarGrid;
    private String shiftsFilePath = "shifts.ser";
    private ArrayList<WorkShift> shifts = new ArrayList<>();
    private String settingsFilePath = "settings.ser";
    private Settings settings;

    @Override
    public void start(Stage primaryStage) {
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
        previousMonth.setOnAction(e -> cahngeMonth(-1));
        nextMonth.setOnAction(e -> cahngeMonth(1));
        HBox changeMonth = new HBox(previousMonth, nextMonth);

        // asetukset nappi
        Button settingsButton = new Button("Asetukset");
        settingsButton.setOnAction(e -> {
            SettingsWindow settingsWindow = new SettingsWindow(this.settings);
            settingsWindow.showAndWait();
            this.settings = settingsWindow.getSettings();
        });

        // Lisää UI komponentit pääasetteluun
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(monthYearLabel, changeMonth, calendarGrid, settingsButton);
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(10));

        // Aseta pääasettelu näkymän sisällöksi
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Palkka Laskuri");
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Tallenna data tiedostoon suljettaessa.
        // Vuorot
        try {
            FileOutputStream fileOut = new FileOutputStream(this.shiftsFilePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.shifts);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Asetukset
        try {
            FileOutputStream fileOut2 = new FileOutputStream(this.settingsFilePath);
            ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);
            out2.writeObject(this.settings);
            out2.close();
            fileOut2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadData() {
        // Vuorot
        try {
            FileInputStream fileIn1 = new FileInputStream(this.shiftsFilePath);
            ObjectInputStream in1 = new ObjectInputStream(fileIn1);
            this.shifts = (ArrayList<WorkShift>) in1.readObject();
            in1.close();
            fileIn1.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }

        // Asetukset
        try {
            FileInputStream fileIn2 = new FileInputStream(this.settingsFilePath);
            ObjectInputStream in2 = new ObjectInputStream(fileIn2);
            this.settings = (Settings) in2.readObject();
            in2.close();
            fileIn2.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        if (this.settings == null) {
            TimeOfDayBonus evening = new TimeOfDayBonus(1.0, LocalTime.of(1, 0), LocalTime.of(2, 0));
            TimeOfDayBonus nigth = new TimeOfDayBonus(1.0, LocalTime.of(3, 0), LocalTime.of(4, 0));
            this.settings = new Settings(10.0, 10.0, evening, nigth);
        }
    }

    private void cahngeMonth(int i) {
        yearMonth = yearMonth.plusMonths(i);
        updateMonthYearLabel(yearMonth);
        buildCalendarGrid(yearMonth);
    }

    private void updateMonthYearLabel(YearMonth yearMonth) {
        String monthYearString = yearMonth.getMonth().toString() + " " + yearMonth.getYear();
        monthYearLabel.setText(monthYearString);
    }

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
                                this.shifts, this::onShiftEditingWindowClosed);
                        shiftEditingWindow.showAndWait();
                        this.shifts = shiftEditingWindow.getShifts();
                    });
                    this.calendarGrid.add(button, col, row);
                    dayOfMonth++;
                }
            }
        }
    }

    private void onShiftEditingWindowClosed() {
        buildCalendarGrid(this.yearMonth);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
