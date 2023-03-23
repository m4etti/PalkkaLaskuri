import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShiftEditingWindow extends Stage {
    private ArrayList<WorkShift> shifts;
    private Runnable onClosedCallback;

    public ShiftEditingWindow(LocalDate date, int shiftIndex, ArrayList<WorkShift> shifts, Runnable onClosedCallback) {
        this.shifts = shifts;
        this.onClosedCallback = onClosedCallback;

        initModality(Modality.APPLICATION_MODAL);
        VBox vBox = new VBox();

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
            onClosedCallback.run();
            close();
        });
        removeShift.setOnAction(e -> {
            shifts.remove(shiftIndex);
            onClosedCallback.run();
            close();
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
            close();
        });

        vBox.getChildren().add(buttons);
        Scene scene = new Scene(vBox, 300, 300);
        setScene(scene);
        // otsikkona päivämäärä
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);
        setTitle(formattedDate);
        show();
    }

    public ArrayList<WorkShift> getShifts() {
        return this.shifts;
    }
}
