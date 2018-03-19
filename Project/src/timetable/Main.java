//Tibo Vanheule
package timetable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //laad configuratie bestand in
        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("schedule.properties"));

        //laad het fxml bestand in
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent root = loader.load();

        //vraag de controller op, zodat we de stage kunnen doorgeven
        Controller controller = loader.getController();

        //verander de stijl van de stage (zonder boord)
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root, 1000, 600);

        //bind de css bestand eraan
        scene.getStylesheets().add("timetable/Style.css");

        //geef de stage een icon
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("images/icon.png")));
        primaryStage.setScene(scene);

        //titel komt niet in boord van het programma sinds het een undecorated window is ,
        // maar is nog steeds zichtbaar in ondere andere windows taakbalk.
        primaryStage.setTitle(properties.getProperty("program.name"));

        //start maximized of niet
        primaryStage.setMaximized(Boolean.parseBoolean(properties.getProperty("startMaximized")));

        //geef de stage door aan de Controller
        controller.setStageAndSetupListeners(primaryStage);

        //toon eindelijk de stage :)
        primaryStage.show();
    }


    public static void main(String[] args) {
        //Controle lengte argumenten
        if (args.length == 0){
            //Er zijn geen argumenten meegegeven, prog nrml opstarten
            launch(args);
        } else if (args.length == 1){
            // TODO: 14/03/2018
            // Moet geschreven worden naar std out
            // dus vervangen door een writer
            //Programma afsluiten na het uitschrijven van de lijst.
            Platform.exit();
            System.exit(0);
        } else if (args.length == 2){
            // TODO: 14/03/2018
            //openen van prog in met gewenst rooster
            //werken met een default rooster en die dan overschrijven of (twee methoden)
        }else if (args.length == 3 ){
            // TODO: 14/03/2018
            // nemen van een snapshot ???
        }else if ( args.length > 3 ){
            // TODO: 14/03/2018
            // foutboodschap
            //Platform.exit om de Javafx-applicatie af te sluiten
            Platform.exit();
            //sluit Java Virtual Machine af met error code 2
            System.exit(1);
        }


    }
}
