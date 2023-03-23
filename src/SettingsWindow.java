import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

public class SettingsWindow extends Stage {
    private Settings settings;

    public SettingsWindow(Settings settings) {
        this.settings = settings;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // Uusi modal ikkuna
        initModality(Modality.APPLICATION_MODAL);

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
                close();

            } catch (DateTimeParseException | NumberFormatException exeption) {
                Label errorLabel = new Label("Virhe syätteissä!!!");
                errorLabel.setTextFill(Color.RED);
                gridPane.add(errorLabel, 0, 7);
            }

        });
        gridPane.add(save, 0, 6);

        Scene scene = new Scene(gridPane, 300, 300);
        setScene(scene);
        setTitle("Asetukset");
        show();

    }

    public Settings getSettings() {
        return this.settings;
    }
}
