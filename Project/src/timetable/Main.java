//Tibo Vanheule
package timetable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.config.Config;

import java.util.Properties;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //laad configuratie bestand in
        Config config = new Config();
        Properties properties =  config.getproperties();

        //laad het fxml bestand in
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent root = loader.load();

        //vraag de controller op, zodat we de stage kunnen doorgeven
        Controller controller = loader.getController();

        //verander de stijl van de stage (zonder boord)
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 1000, 600);

        //Volgende twee functie zorgen ervoor dat het programma verplaatsbaar is zonder boord
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        //bind de css bestand eraan
        scene.getStylesheets().add("timetable/Style.css");

        //geef de stage een icon
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/images/icon.png")));
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
            //WritableImage image = root.snapshot(new SnapshotParameters(), null);

            //File file = new File("chart.png");

            //try {
                //ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            //} catch (IOException e) {
                //System.out.println(e);
            //}
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
