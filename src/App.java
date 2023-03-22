import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class App extends Application {

    private Label monthYearLabel;
    private YearMonth yearMonth;
    private GridPane calendarGrid;
    private ArrayList <WorkShift> shifts;

    @Override
    public void start(Stage primaryStage) {
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

        // Lisää UI komponentit pääasetteluun
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(monthYearLabel, changeMonth, calendarGrid);
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(10));

        // Aseta pääasettelu näkymän sisällöksi
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Palkka Laskuri");
        primaryStage.show();
    }

    private void cahngeMonth(int i, Stage primaryStage){
        yearMonth = yearMonth.plusMonths(i);
        updateMonthYearLabel(yearMonth);
        buildCalendarGrid(yearMonth,primaryStage);
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
                    // Lisää nappi päivälle
                    WorkShift shiftToday;
                    LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), dayOfMonth);
                    //onko vuoroa tälle päivälle
                    for (WorkShift shift: this.shifts){
                        if (shift.getStart().toLocalDate() == date){
                            shiftToday = shift;
                            break;
                        }
                    }
                    Button button = new Button(Integer.toString(dayOfMonth));
                    button.setPrefSize(40, 30);

                    button.setOnAction(e -> {
                        dayButtonAction(primaryStage, date);
                    });
                    this.calendarGrid.add(button, col, row);
                    dayOfMonth++;
                }
            }
        }
    }

    private void dayButtonAction(Stage primaryStage, LocalDate date){
        // Uusi modal ikkuna päivälle
        Stage dayWindow = new Stage();
        dayWindow.initModality(Modality.APPLICATION_MODAL);
        dayWindow.initOwner(primaryStage);       

        Button adWorksift = new Button("Lisää työvuoro");
        HBox buttons = new HBox(adWorksift);

        VBox vBox = new VBox(buttons);
        Scene scene = new Scene(vBox, 300, 100);
        dayWindow.setScene(scene);
        // otsikkona päivämäärä
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);
        dayWindow.setTitle(formattedDate);
        dayWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
