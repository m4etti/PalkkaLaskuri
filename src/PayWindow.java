import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A window that displays the total pay for a given range of dates, based on a
 * list of work shifts, and the tax percentage.
 * PayWindow extends Stage class.
 * 
 * @author Matti Voutilainen
 */
public class PayWindow extends Stage {

    /**
     * Creates a new PayWindow instance based on a list of work shifts and the
     * specified date range, and displays it.
     *
     * @param shifts    The list of work shifts to include in the pay calculation.
     * @param yearMonth The year and month for the pay period.
     * @param start     The starting day of the pay period.
     * @param end       The ending day of the pay period.
     * @param tax       The tax percentage to apply to the total pay.
     */
    public PayWindow(ArrayList<WorkShift> shifts, YearMonth yearMonth, int start, int end, double tax) {
        LocalDate startDate = yearMonth.atDay(start);
        LocalDate endDate = yearMonth.atDay(end);

        Pay pay = new Pay();
        WorkHours hours = new WorkHours();

        // Käy läpi vuorot jotka ovat palkka välillä
        for (WorkShift shift : shifts) {
            if (!startDate.isAfter(shift.getStart().toLocalDate()) &&
                    !endDate.isBefore(shift.getStart().toLocalDate())) {
                pay.addNormalPay(shift.getPay().getNormalPay());
                pay.addEveningPay(shift.getPay().getEveningPay());
                pay.addNigthPay(shift.getPay().getNigthPay());
                pay.addExtraPay(shift.getPay().getExtraPay());
                pay.addOvertimePay(shift.getPay().getOvertimePay());

                hours.addNormal(shift.getWorkHours().getNormal());
                hours.addEvening(shift.getWorkHours().getEvening());
                hours.addNigth(shift.getWorkHours().getNight());
                hours.addOverTimeNormal(shift.getWorkHours().getOverTimeNormal());
            }
        }

        // Otsikko
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = startDate.format(dateFormatter) + " - " + endDate.format(dateFormatter);
        Label label = new Label(formattedDate);

        // Palkka grid
        PayGrid paygrid = new PayGrid(pay, hours, tax);

        VBox vBox = new VBox(label, paygrid);
        Scene scene = new Scene(vBox, 300, 360);
        setScene(scene);
        setTitle("Palkka");
        show();
    }
}
