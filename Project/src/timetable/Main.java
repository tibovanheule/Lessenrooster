//Tibo Vanheule
package timetable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.config.Config;
import timetable.objects.Item;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
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
        Properties properties = config.getproperties();

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


        if (getParameters().getRaw().size() == 0){
            //toon eindelijk de stage :)
            primaryStage.show();
        }
        else if (getParameters().getRaw().size() == 2){
            try {
                Runnable runnable = new Thread( () -> {
                Item item = new Item(getParameters().getRaw().get(0),getParameters().getRaw().get(1));
                controller.getRooster(item);
                controller.draw.setVisible(false);});
                Platform.runLater(runnable);
            }catch (Exception e){
                System.out.println(e);
            }
            primaryStage.show();
        }else if (getParameters().getRaw().size() == 3){
            try{
            Runnable runnable = new Thread( () -> {
                Item item = new Item(getParameters().getRaw().get(0),getParameters().getRaw().get(1));
                controller.getRooster(item);
                controller.draw.setVisible(false);});
            Platform.runLater(runnable);
            }catch (Exception e){
                System.out.println(e);
            }
            primaryStage.setMaximized(false);
            WritableImage image = root.snapshot(new SnapshotParameters(), null);

            // TODO: 29/03/2018 kijken als dit niet zonder file kan :)
            File file = new File(getParameters().getRaw().get(2));

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                System.out.println(e);
                Platform.exit();
                System.exit(2);
            }
            Platform.exit();
            System.exit(0);
        }


    }


    public static void main(String[] args) {
        //Controle lengte argumenten
        if (args.length == 0 || args.length == 2 || args.length == 3){
            //Start prog met argumenten -> normale start
            launch(args);
        } else if (args.length == 1){
            StdoutList getList = new StdoutList(args[0]);
            Platform.exit();
            System.exit(0);
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
