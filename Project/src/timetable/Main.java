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
import timetable.stdout.StdoutList;
import timetable.stdout.StdoutSchedule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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


        /*scene.focusOwnerProperty().addListener(o ->{
                    try {
                        System.out.println(scene.getFocusOwner().getId());
                        System.out.println(scene.getFocusOwner().getParent().getId());
                        if(!scene.getFocusOwner().getId().equals("list")){
                                controller.drawerAction();
                        }
                    }catch (Exception e){
                        System.out.println(e);
                    }
                });
         */


        //toon eindelijk de stage :)
        primaryStage.show();
    }


    public static void main(String[] args) {
        //Controle lengte argumenten
        if (args.length == 0){
            //Er zijn geen argumenten meegegeven, prog nrml opstarten
            launch(args);
        } else if (args.length == 1){
            StdoutList getList = new StdoutList(args[0]);
            Platform.exit();
            System.exit(0);
        } else if (args.length == 2){
            // TODO: 28/03/2018
            //FOUT OPDRACHT VERKEERD BEGREPEN :( OPEN app niet op std schrijven
            // openen van prog in met gewenst rooster
            StdoutSchedule getschedule = new StdoutSchedule(args[0], args[1]);
            Platform.exit();
            System.exit(0);
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
        }else if (args.length >= 4){
            try (BufferedWriter error = new BufferedWriter(new OutputStreamWriter(System.err))){
                 error.write("Invalid! please don't give more than 3 arguments! :) \n");
                 error.flush();
            }catch (IOException e) {
                System.out.println(e);
            }
            //Platform.exit om de Javafx-applicatie af te sluiten
            Platform.exit();
            //sluit Java Virtual Machine af met error code 2
            System.exit(1);
        }
    }

}
