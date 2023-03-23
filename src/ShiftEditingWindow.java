import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShiftEditingWindow extends Stage {
    private ArrayList<WorkShift> shifts;

    public ShiftEditingWindow(LocalDate date, int shiftIndex, ArrayList<WorkShift> shifts, Settings settings,
            Runnable onClosedCallback) {
        this.shifts = shifts;
        DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        initModality(Modality.APPLICATION_MODAL);
        VBox vBox = new VBox();

        // syöttökentät
        Label shiftTimeLabel = new Label("Työvuoro ajalla:");
        Label dashLabel = new Label("-");
        TextField shiftTimeStart = new TextField();
        shiftTimeStart.setPromptText("HH:MM");
        shiftTimeStart.setPrefWidth(80);
        TextField shiftTimeEnd = new TextField();
        shiftTimeEnd.setPromptText("HH:MM");
        shiftTimeEnd.setPrefWidth(80);
        HBox hBox = new HBox(shiftTimeStart, dashLabel, shiftTimeEnd);

        vBox.getChildren().addAll(shiftTimeLabel, hBox);

        // Napit
        HBox buttons = new HBox();
        // Lisää vuoro.
        Button addWorkshift = new Button("Lisää työvuoro");
        // Poista vuoro.
        Button removeShift = new Button("Poista tyävuoro");
        // Muokkaa vuoroa.
        Button editShift = new Button("Muokkaa tyävuora");
        // Tallenna.
        Button save = new Button("Tallenna");

        // Ei vuoroa.
        if (shiftIndex == -1) {
            // Näytä lisää nappi.
            buttons.getChildren().add(addWorkshift);
        }
        // On vuoro
        else {
            // Aseta kenttiin arvot vuorosta ja lukitse kentät.
            shiftTimeStart.setText(shifts.get(shiftIndex).getStart().format(localTimeFormatter));
            shiftTimeEnd.setText(shifts.get(shiftIndex).getEnd().format(localTimeFormatter));
            shiftTimeStart.setDisable(true);
            shiftTimeEnd.setDisable(true);

            // Näytä poista ja muokkaa napit.
            buttons.getChildren().addAll(removeShift, editShift);
        }

        // Nappien toiminnot.
        addWorkshift.setOnAction(e -> {
            try {
                // Muokkaa syötteet LocalTime muotoon.
                LocalTime startTime = LocalTime.parse(shiftTimeStart.getText());
                LocalTime endTime = LocalTime.parse(shiftTimeEnd.getText());

                // luo uusi työvuoro
                WorkShift newShift = new WorkShift(LocalDateTime.of(date, startTime), LocalDateTime.of(date, endTime),
                        settings);
                this.shifts.add(newShift);
                onClosedCallback.run();
                close();
            } catch (DateTimeParseException exeption) {
                Label errorLabel = new Label("Virhe syätteissä!!!");
                errorLabel.setTextFill(Color.RED);
                vBox.getChildren().add(errorLabel);
            }

        });
        removeShift.setOnAction(e -> {
            shifts.remove(shiftIndex);
            onClosedCallback.run();
            close();
        });
        editShift.setOnAction(e -> {
            shiftTimeStart.setDisable(false);
            shiftTimeStart.setDisable(false);
            buttons.getChildren().remove(editShift);
            buttons.getChildren().add(save);
        });
        save.setOnAction(e -> {
            try {
                LocalTime startTime = LocalTime.parse(shiftTimeStart.getText());
                LocalTime endTime = LocalTime.parse(shiftTimeEnd.getText());

                // muokkaa työvuoroa
                this.shifts.get(shiftIndex).setTimes(LocalDateTime.of(date, startTime),
                        LocalDateTime.of(date, endTime));
                close();
            } catch (DateTimeParseException exeption) {
                Label errorLabel = new Label("Virhe syätteissä!!!");
                errorLabel.setTextFill(Color.RED);
                vBox.getChildren().add(errorLabel);
            }
        });

        vBox.getChildren().add(buttons);
        Scene scene = new Scene(vBox, 300, 300);
        setScene(scene);
        // otsikkona päivämäärä
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(dateFormatter);
        setTitle(formattedDate);
        show();
    }

    public ArrayList<WorkShift> getShifts() {
        return this.shifts;
    }
}
