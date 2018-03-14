//Tibo Vanheule
package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    //Exit code deze wordt doorgegeven aan System.exit() achter de Platform.exit de Javafx-processen heeft afgesloten
    private static int exitCode = 0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
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
            // system print is voor testing purposes
            System.out.print("TEST");
            //Programma afsluiten na het uitschrijven van de lijst.
            Platform.exit();
        } else if (args.length == 2){
            // TODO: 14/03/2018
            //openen van prog in met gewenst rooster
            //werken met een default rooster en die dan overschrijven of (twee methoden)
        }else if (args.length == 3 ){
            // TODO: 14/03/2018
            // nemen van een snapshot ???
        }else if (args.length > 3 ){
            // TODO: 14/03/2018
            // foutboodschap
            // is dat op stderr??? of met een venster
            exitCode = 1;
            Platform.exit();
        }
        System.exit(exitCode);

    }
}
