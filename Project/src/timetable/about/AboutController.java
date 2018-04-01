//Tibo Vanheule
package timetable.about;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import timetable.config.Config;

import java.util.Properties;

public class AboutController {
    public Label text;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage){
        //krijgen van de stage
        this.stage = stage;
    }

    public void initialize(){

        //inladen configuratie bestand
        Config config = new Config();
        Properties properties =  config.getproperties();
        //bewust alle tekst in 1 veld gestoken
        //kleinere fxml en tekst staat dan ook altijd mooi onder elkaar, dankzij de new-line
        text.setText ("Version: " + properties.getProperty("program.version")+
                "\nCopyright: " + properties.getProperty("programmer.name") +
                "\nMade by: " + properties.getProperty("programmer.name") +
                "\nEmail: " + properties.getProperty("programmer.email") +
                "\nWebsite: " + properties.getProperty("programmer.site") +
                "\nProjects github: " + properties.getProperty("program.github") +
                "\nPrivate repo, send an email with github username for access!" +
                "\nLayout (Collorpallet) based on: \n" + properties.getProperty("layout.basedOn") +
                "\nWeather icons from:\n" + properties.getProperty("layout.weather.icons") +
                "\nmysql Connection warning. The account has very little privileges and" +
                "\nit can only connect to one DB. An audit log file is also in place :)"
        );

    }

    public void close(){
        //afsluiten stage
        stage.close();
    }
}
