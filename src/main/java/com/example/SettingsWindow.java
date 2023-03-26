package com.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.example.data.Settings;
import com.example.data.TimeOfDayBonus;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The SettingsWindow class creates a modal window for changing the settings of
 * the pay calculations.
 * SetingsWindow extends the JavaFX Stage class
 * 
 * @author Matti Voutilainen
 */
public class SettingsWindow extends Stage {
    /**
     * The settings object containing the current settings of the pay calculations.
     */
    private Settings settings;

    /**
     * Creates a new SettingsWindow with the specified settings
     * 
     * @param settings the Settings object containing the current settings of the
     *                 pay calculations.
     */
    public SettingsWindow(Settings settings) {
        this.settings = settings;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // modal window
        initModality(Modality.APPLICATION_MODAL);

        // Set up grid
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // Hourly wage
        Label hourlyWageLabel = new Label("Tunti palkka:");
        TextField hourlyWageInput = new TextField();
        hourlyWageInput.setText(Double.toString(this.settings.getHourlyWage()));
        gridPane.add(hourlyWageLabel, 0, 0);
        gridPane.add(hourlyWageInput, 1, 0);

        // Extra pay
        Label extraWageLabel = new Label("Lisä:");
        TextField extraWageInput = new TextField();
        extraWageInput.setText(Double.toString(this.settings.getExtra()));
        gridPane.add(extraWageLabel, 0, 1);
        gridPane.add(extraWageInput, 1, 1);

        // Evening bonus
        Label eveningBonusLabel = new Label("Iltalisä:");
        TextField eveningBonusInput = new TextField();
        eveningBonusInput.setText(Double.toString(this.settings.getEavningBonus().getBonus()));
        gridPane.add(eveningBonusLabel, 0, 2);
        gridPane.add(eveningBonusInput, 1, 2);

        // Evening bonus time
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

        // Nigth bonus
        Label nigthBonusLabel = new Label("Yölisä:");
        TextField nigthBonusInput = new TextField();
        nigthBonusInput.setText(Double.toString(this.settings.getNightBonus().getBonus()));
        gridPane.add(nigthBonusLabel, 0, 4);
        gridPane.add(nigthBonusInput, 1, 4);

        // Nigth bonus time
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

        // Tax
        Label taxLabel = new Label("Veroprosentti:");
        TextField taxInput = new TextField();
        taxInput.setText(Double.toString(this.settings.getTax() * 100));
        gridPane.add(taxLabel, 0, 6);
        gridPane.add(taxInput, 1, 6);

        // Save button
        Button save = new Button("Tallenna");
        save.setOnAction(e -> {
            // Read the values entered by the user and save them to the 'settings' variable.
            try {
                Double wage = Double.parseDouble(hourlyWageInput.getText());
                Double extraWage = Double.parseDouble(extraWageInput.getText());
                Double evningBonus = Double.parseDouble(eveningBonusInput.getText());
                LocalTime eveningStart = LocalTime.parse(eveningStartInput.getText());
                LocalTime eveningEnd = LocalTime.parse(eveningEndInput.getText());
                Double nigthBonus = Double.parseDouble(nigthBonusInput.getText());
                LocalTime nigthStart = LocalTime.parse(nigthStartInput.getText());
                LocalTime nightEnd = LocalTime.parse(nigthEndInput.getText());
                Double tax = Double.parseDouble(taxInput.getText()) / 100;

                TimeOfDayBonus evening = new TimeOfDayBonus(evningBonus, eveningStart, eveningEnd);
                TimeOfDayBonus nigth = new TimeOfDayBonus(nigthBonus, nigthStart, nightEnd);
                this.settings.setAll(wage, extraWage, evening, nigth, tax);
                close();

            }
            // Catch errors
            catch (DateTimeParseException | NumberFormatException exception) {
                Label errorLabel = new Label("Virhe syätteissä!!!");
                errorLabel.setTextFill(Color.RED);
                gridPane.add(errorLabel, 0, 7);
            }

        });
        gridPane.add(save, 0, 7);

        Scene scene = new Scene(gridPane, 270, 290);
        setScene(scene);
        setTitle("Asetukset");
        show();

    }

    /**
     * Retuns modified settings.
     * 
     * @return modified settings
     */
    public Settings getSettings() {
        return this.settings;
    }
}
