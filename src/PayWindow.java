import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PayWindow extends Stage {
    
    public PayWindow(ArrayList<WorkShift> shifts, YearMonth yearMonth, int start, int end, double tax){
        LocalDate startDate = yearMonth.atDay(start);
        LocalDate endDate = yearMonth.atDay(end);
        
        Pay pay = new Pay();
        WorkHours hours = new WorkHours();

        // Käy läpi vuorot jotka ovat palkka välillä
        for (WorkShift shift : shifts){
            if (!startDate.isAfter(shift.getStart().toLocalDate()) &&
             !endDate.isBefore(shift.getStart().toLocalDate())){
                pay.addNormalPay(shift.getPay().getNormalPay());
                pay.addEveningPay(shift.getPay().getEveningPay());
                pay.addNigthPay(shift.getPay().getNigthPay());
                pay.addExtraPay(shift.getPay().getExtraPay());
                pay.addOvertimePay(shift.getPay().getOvertimePay());

                hours.addNormal(shift.getWorkHours().getNormal()); 
                hours.addEvening(shift.getWorkHours().getEvening()); 
                hours.addNigth(shift.getWorkHours().getNigth()); 
                hours.addOverTimeNormal(shift.getWorkHours().getOverTimeNormal()); 
            }
        }
 
        // Otsikko
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = startDate.format(dateFormatter) + " - " + endDate.format(dateFormatter);
        Label label = new Label(formattedDate);

        // Palkka grid
        PayGrid paygrid = new PayGrid(pay, hours, tax);

        VBox vBox = new VBox(label,paygrid);
        Scene scene = new Scene(vBox, 300, 360);
        setScene(scene);
        setTitle("Palkka");
        show();
    }
}
