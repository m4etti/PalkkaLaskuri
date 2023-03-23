import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        buildCalendarGrid(yearMonth, primaryStage);

        // kuukauden vaihto napit
        Button previousMonth = new Button("<");
        Button nextMonth = new Button(">");
        previousMonth.setOnAction(e -> {
            cahngeMonth(-1, primaryStage);
        });
        nextMonth.setOnAction(e -> {
            cahngeMonth(1, primaryStage);
        });
        HBox changeMonth = new HBox(previousMonth, nextMonth);

        // asetukset nappi
        Button settingsButton = new Button("Asetukset");
        settingsButton.setOnAction(e -> {
            settingsAction(primaryStage);
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

    private void cahngeMonth(int i, Stage primaryStage) {
        yearMonth = yearMonth.plusMonths(i);
        updateMonthYearLabel(yearMonth);
        buildCalendarGrid(yearMonth, primaryStage);
    }

    private void updateMonthYearLabel(YearMonth yearMonth) {
        String monthYearString = yearMonth.getMonth().toString() + " " + yearMonth.getYear();
        monthYearLabel.setText(monthYearString);
    }

    private void buildCalendarGrid(YearMonth yearMonth, Stage primaryStage) {
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
                        dayButtonAction(primaryStage, date, shiftIndex[0]);
                    });
                    this.calendarGrid.add(button, col, row);
                    dayOfMonth++;
                }
            }
        }
    }

    private void dayButtonAction(Stage primaryStage, LocalDate date, int shiftIndex) {
        // Uusi modal ikkuna päivälle
        Stage dayWindow = new Stage();
        VBox vBox = new VBox();
        dayWindow.initModality(Modality.APPLICATION_MODAL);
        dayWindow.initOwner(primaryStage);

        // syöttökentät
        Label startLabel = new Label("Vuoro alkoi:");
        TextField startH = new TextField();
        TextField startM = new TextField();
        Label endLabel = new Label("Vuoro loppui:");
        TextField endH = new TextField();
        TextField endM = new TextField();

        vBox.getChildren().addAll(startLabel, startH, startM, endLabel, endH, endM);

        // napit
        HBox buttons = new HBox();
        // lisää vuoro
        Button addWorkshift = new Button("Lisää työvuoro");
        // poista vuoro
        Button removeShift = new Button("Poista tyävuoro");
        // muokkaa vuoroa
        Button editShift = new Button("Muokkaa tyävuora");
        // tallenna
        Button save = new Button("Tallenna");

        // ei vuoroa
        if (shiftIndex == -1) {
            // näytä lisää nappi
            buttons.getChildren().add(addWorkshift);
        }
        // on vuoro
        else {
            // aseta kenttiin arvot vuorosta ja lukitse kentät
            startH.setText(Integer.toString(shifts.get(shiftIndex).getStart().getHour()));
            startM.setText(Integer.toString(shifts.get(shiftIndex).getStart().getMinute()));
            endH.setText(Integer.toString(shifts.get(shiftIndex).getEnd().getHour()));
            endM.setText(Integer.toString(shifts.get(shiftIndex).getEnd().getMinute()));
            startH.setDisable(true);
            startM.setDisable(true);
            endH.setDisable(true);
            endM.setDisable(true);

            // näytä poista ja muokkaa napit
            buttons.getChildren().addAll(removeShift, editShift);
        }

        // nappien toiminnot
        addWorkshift.setOnAction(e -> {
            // muokkaa syötteet LocalTime muotoon
            LocalTime startTime = LocalTime.of(Integer.parseInt(startH.getText()), Integer.parseInt(startM.getText()));
            LocalTime endTime = LocalTime.of(Integer.parseInt(endH.getText()), Integer.parseInt(endM.getText()));

            // luo uusi työvuoro
            WorkShift newShift = new WorkShift(LocalDateTime.of(date, startTime), LocalDateTime.of(date, endTime));
            this.shifts.add(newShift);
            buildCalendarGrid(this.yearMonth, primaryStage);
            dayWindow.close();
        });
        removeShift.setOnAction(e -> {
            shifts.remove(shiftIndex);
            buildCalendarGrid(this.yearMonth, primaryStage);
            dayWindow.close();
        });
        editShift.setOnAction(e -> {
            startH.setDisable(false);
            startM.setDisable(false);
            endH.setDisable(false);
            endM.setDisable(false);
            buttons.getChildren().remove(editShift);
            buttons.getChildren().add(save);
        });
        save.setOnAction(e -> {
            LocalTime startTime = LocalTime.of(Integer.parseInt(startH.getText()), Integer.parseInt(startM.getText()));
            LocalTime endTime = LocalTime.of(Integer.parseInt(endH.getText()), Integer.parseInt(endM.getText()));

            // muokkaa työvuoroa
            this.shifts.get(shiftIndex).setTimes(LocalDateTime.of(date, startTime), LocalDateTime.of(date, endTime));
            buildCalendarGrid(this.yearMonth, primaryStage);
            dayWindow.close();
        });

        vBox.getChildren().add(buttons);
        Scene scene = new Scene(vBox, 300, 300);
        dayWindow.setScene(scene);
        // otsikkona päivämäärä
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);
        dayWindow.setTitle(formattedDate);
        dayWindow.show();

    }

    private void settingsAction(Stage primaryStage) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // Uusi modal ikkuna
        Stage settingWindow = new Stage();
        settingWindow.initModality(Modality.APPLICATION_MODAL);
        settingWindow.initOwner(primaryStage);

        // Gridin asettelu
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // Tunti palkka
        Label hourlyWageLabel = new Label("Tunti palkka:");
        TextField hourlyWageInput = new TextField();
        hourlyWageInput.setText(Double.toString(this.settings.getHourlyWage()));
        gridPane.add(hourlyWageLabel, 0, 0);
        gridPane.add(hourlyWageInput, 1, 0);

        // Yleinen lisä
        Label extraWageLabel = new Label("Lisä:");
        TextField extraWageInput = new TextField();
        extraWageInput.setText(Double.toString(this.settings.getExtra()));
        gridPane.add(extraWageLabel, 0, 1);
        gridPane.add(extraWageInput, 1, 1);

        // Iltalisä
        Label eveningBonusLabel = new Label("Iltalisä:");
        TextField eveningBonusInput = new TextField();
        eveningBonusInput.setText(Double.toString(this.settings.getEavningBonus().getBonus()));
        gridPane.add(eveningBonusLabel, 0, 2);
        gridPane.add(eveningBonusInput, 1, 2);

        // Iltalisän aika
        Label eveningTimeLabel = new Label("Iltalisän aika:");
        TextField eveningStartInput = new TextField();
        eveningStartInput.setPrefWidth(70);
        TextField eveningEndInput = new TextField();
        eveningEndInput.setPrefWidth(70);
        Label dashLabel1 = new Label("-");
        HBox evningTimeHBox = new HBox(eveningStartInput, dashLabel1, eveningEndInput);
        eveningStartInput.setText(this.settings.getEavningBonus().getStart().format(formatter));
        eveningEndInput.setText(this.settings.getEavningBonus().getEnd().format(formatter));
        gridPane.add(eveningTimeLabel, 0, 3);
        gridPane.add(evningTimeHBox, 1, 3);

        // Yölisä
        Label nigthBonusLabel = new Label("Yölisä:");
        TextField nigthBonusInput = new TextField();
        nigthBonusInput.setText(Double.toString(this.settings.getNightBonus().getBonus()));
        gridPane.add(nigthBonusLabel, 0, 4);
        gridPane.add(nigthBonusInput, 1, 4);

        // YöLisän aika
        Label nigthTimeLabel = new Label("Yölisän aika:");
        TextField nigthStartInput = new TextField();
        nigthStartInput.setPrefWidth(70);
        TextField nigthEndInput = new TextField();
        nigthEndInput.setPrefWidth(70);
        Label dashLabel2 = new Label("-");
        HBox nigthTimeHBox = new HBox(nigthStartInput, dashLabel2, nigthEndInput);
        nigthStartInput.setText(this.settings.getNightBonus().getStart().format(formatter));
        nigthEndInput.setText(this.settings.getNightBonus().getEnd().format(formatter));
        gridPane.add(nigthTimeLabel, 0, 5);
        gridPane.add(nigthTimeHBox, 1, 5);

        // Talenna
        Button save = new Button("Tallenna");
        save.setOnAction(e -> {
            Double wage;
            Double extraWage;
            Double evningBonus;
            LocalTime eveningStart;
            LocalTime eveningEnd;
            Double nigthBonus;
            LocalTime nigthStart;
            LocalTime nightEnd;

            try {
                wage = Double.parseDouble(hourlyWageInput.getText());
                extraWage = Double.parseDouble(extraWageInput.getText());
                evningBonus = Double.parseDouble(eveningBonusInput.getText());
                eveningStart = LocalTime.parse(eveningStartInput.getText());
                eveningEnd = LocalTime.parse(eveningEndInput.getText());
                nigthBonus = Double.parseDouble(nigthBonusInput.getText());
                nigthStart = LocalTime.parse(nigthStartInput.getText());
                nightEnd = LocalTime.parse(nigthEndInput.getText());

                TimeOfDayBonus evening = new TimeOfDayBonus(evningBonus, eveningStart, eveningEnd);
                TimeOfDayBonus nigth = new TimeOfDayBonus(nigthBonus, nigthStart, nightEnd);
                this.settings.setAll(wage, extraWage, evening, nigth);
                settingWindow.close();

            } catch (DateTimeParseException | NumberFormatException exeption) {
                Label errorLabel = new Label("Virhe syätteissä!!!");
                errorLabel.setTextFill(Color.RED);
                gridPane.add(errorLabel, 0, 7);
            }

        });
        gridPane.add(save, 0, 6);

        Scene scene = new Scene(gridPane, 300, 300);
        settingWindow.setScene(scene);
        settingWindow.setTitle("Asetukset");
        settingWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
