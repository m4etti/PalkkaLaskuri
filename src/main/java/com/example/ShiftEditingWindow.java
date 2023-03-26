package com.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import com.example.data.Settings;
import com.example.data.WorkShift;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The ShiftEditingWindow class represents a graphical window for editing work
 * shifts.
 * It allows the user to add, remove, and modify work shifts for a given date.
 * ShiftEditingWindow extends JavaFX class Stage.
 * 
 * @author Matti Voutilaine
 */
public class ShiftEditingWindow extends Stage {
    /**
     * The list of work shifts to be edited.
     */
    private ArrayList<WorkShift> shifts;

    /**
     * Constructs a new ShiftEditingWindow object.
     * 
     * @param date             the date of the work shifts
     * @param shiftIndex       the index of the work shift to be edited (-1 if no
     *                         work shift is selected)
     * @param shifts           the list of work shifts to be edited
     * @param settings         the settings object
     * @param onClosedCallback a callback function to be called when the window is
     *                         closed
     */
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
            WorkShift thisShift = shifts.get(shiftIndex);
            shiftTimeStart.setText(thisShift.getStart().format(localTimeFormatter));
            shiftTimeEnd.setText(thisShift.getEnd().format(localTimeFormatter));
            shiftTimeStart.setDisable(true);
            shiftTimeEnd.setDisable(true);

            // Näytä poista ja muokkaa napit.
            buttons.getChildren().addAll(removeShift, editShift);

            PayGrid payGrid = new PayGrid(thisShift);
            vBox.getChildren().add(payGrid);
        }

        // Nappien toiminnot.
        addWorkshift.setOnAction(e -> {
            try {
                // Muokkaa syötteet LocalDateTime muotoon.
                LocalDateTime startTime = LocalDateTime.of(date, LocalTime.parse(shiftTimeStart.getText()));
                LocalDateTime endTime = LocalDateTime.of(date, LocalTime.parse(shiftTimeEnd.getText()));
                if (startTime.isAfter(endTime)) {
                    endTime = endTime.plusHours(24);
                }

                // luo uusi työvuoro
                WorkShift newShift = new WorkShift(startTime, endTime, settings);
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
            shiftTimeEnd.setDisable(false);
            buttons.getChildren().remove(editShift);
            buttons.getChildren().add(save);
        });
        save.setOnAction(e -> {
            try {
                LocalDateTime startTime = LocalDateTime.of(date, LocalTime.parse(shiftTimeStart.getText()));
                LocalDateTime endTime = LocalDateTime.of(date, LocalTime.parse(shiftTimeEnd.getText()));
                if (startTime.isAfter(endTime)) {
                    endTime = endTime.plusDays(1);
                }

                // muokkaa työvuoroa
                this.shifts.get(shiftIndex).modify(startTime, endTime, settings);
                close();
            } catch (DateTimeParseException exeption) {
                Label errorLabel = new Label("Virhe syätteissä!!!");
                errorLabel.setTextFill(Color.RED);
                vBox.getChildren().add(errorLabel);
            }
        });

        vBox.getChildren().add(buttons);
        Scene scene = new Scene(vBox, 300, 360);
        setScene(scene);
        // otsikkona päivämäärä
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(dateFormatter);
        setTitle(formattedDate);
        show();
    }

    /**
     * Returns the list of work shifts after they have been edited.
     * 
     * @return the list of work shifts
     */
    public ArrayList<WorkShift> getShifts() {
        return this.shifts;
    }
}