//Tibo vanheule
package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller {
    public Label day;
    public Label date;
    public Label time;
    public void initialize(){
        // TODO: 14/03/2018  
        //zet deze tijden  om in in oproepbare methode
        //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        //de huidige datum
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("H:mm");
        time.setText(now.format(formatTime));
        //formatteer de datum (dag)
        DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("EEEE");
        //geef nieuwe waarde aan de label met id:day
        day.setText(now.format(formatDay));
        //formateer datum
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("d LLLL");
        //geef nieuwe waarde aan de label met id:date
        date.setText(now.format(formatDate));
    }
    public void exit(){
        Platform.exit();
        System.exit(0);
    }

}
