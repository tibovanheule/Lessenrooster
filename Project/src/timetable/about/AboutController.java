//Tibo Vanheule
package timetable.about;

import javafx.stage.Stage;
import javafx.scene.control.Label;
import timetable.Main;
import java.io.IOException;
import java.util.Properties;

public class AboutController {
    public Label text;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage){
        //krijgen van de stage
        this.stage = stage;
    }

    public void initialize() throws IOException {

        //inladen configuratie bestand
        //properties variable kan lokaal zijn want wordt enkel hier gebruikt
        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("lessenrooster.properties"));
        //bewust alle tekst in 1 veld gestoken
        //kleinere fxml en tekst staat dan ook altijd mooi onder elkaar, dankzij de new-line
        text.setText ("Version: " + properties.getProperty("program.version")+
                "\nCopyright: " + properties.getProperty("programmer.name") +
                "\nMade by: " + properties.getProperty("programmer.name") +
                "\nEmail: " + properties.getProperty("programmer.email") +
                "\nWebsite: " + properties.getProperty("programmer.site") +
                "\nProjects github: " + properties.getProperty("program.github") +
                "\nPrivate repo, send an email with github username for access!" +
                "\nLayout (Collorpallet) based on: \n" + properties.getProperty("layout.basedOn")
        );
    }

    public void close(){
        //afsluiten stage
        stage.close();
    }
}
