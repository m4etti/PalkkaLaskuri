import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class App extends Application {

    private Label monthYearLabel;
    private YearMonth yearMonth;
    private GridPane calendarGrid;
    private ArrayList <WorkShift> shifts = new ArrayList<>();
    

    @Override
    public void start(Stage primaryStage) {
        shifts.add(new WorkShift(LocalDateTime.now(), LocalDateTime.now()));
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
                    LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), dayOfMonth);
                    
                    // Lisää nappi päivälle
                    Button button = new Button(Integer.toString(dayOfMonth));
                    button.setPrefSize(40, 30);
                    int[] shiftIndex = {-1};
                    //onko vuoroa tälle päivälle
                    for (int i=0; i < this.shifts.size(); i++){
                        if (date.equals(this.shifts.get(i).getStart().toLocalDate())){
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

    private void dayButtonAction(Stage primaryStage, LocalDate date, int shiftIndex){
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

        vBox.getChildren().addAll(startLabel,startH,startM,endLabel,endH,endM);
        
        // napit   
        HBox buttons = new HBox();
        //lisää vuoro
        Button addWorkshift = new Button("Lisää työvuoro");        
        //poista vuoro
        Button removeShift = new Button("Poista tyävuoro");        
        //muokkaa vuoroa
        Button editShift = new Button("Muokkaa tyävuora");  
        //tallenna
        Button save = new Button("Tallenna");

        // ei vuoroa
        if (shiftIndex == -1){
            //näytä lisää nappi
            buttons.getChildren().add(addWorkshift);          
        }
        // on vuoro
        else{
            // aseta kenttiin arvot vuorosta ja lukitse kentät
            startH.setText(Integer.toString(shifts.get(shiftIndex).getStart().getHour()));
            startM.setText(Integer.toString(shifts.get(shiftIndex).getStart().getMinute()));
            endH.setText(Integer.toString(shifts.get(shiftIndex).getEnd().getHour()));
            endM.setText(Integer.toString(shifts.get(shiftIndex).getEnd().getMinute()));
            startH.setDisable(true);
            startM.setDisable(true);
            endH.setDisable(true);
            endM.setDisable(true);

            //näytä poista ja muokkaa napit
            buttons.getChildren().addAll(removeShift, editShift);
        }

        // nappien toiminnot
        addWorkshift.setOnAction(e -> {
            // muokkaa syötteet LocalTime muotoon
            LocalTime startTime = LocalTime.of(Integer.parseInt(startH.getText()), Integer.parseInt(startM.getText()));
            LocalTime endTime = LocalTime.of(Integer.parseInt(endH.getText()), Integer.parseInt(endM.getText()));

            //luo uusi työvuoro
            WorkShift newShift = new WorkShift(LocalDateTime.of(date,startTime),LocalDateTime.of(date,endTime));
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

            //muokkaa  työvuoroa
            this.shifts.get(shiftIndex).setTimes(LocalDateTime.of(date,startTime),LocalDateTime.of(date,endTime));
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


    public static void main(String[] args) {
        launch(args);
    }
}
