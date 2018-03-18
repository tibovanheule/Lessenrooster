//Tibo Vanheule
package timetable.about;

import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.Properties;

public class AboutController {
    public Label text;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;

    }

    public void initialize() throws IOException {
        Properties properties = new Properties();
        properties.load(AboutController.class.getResourceAsStream("../lessenrooster.properties"));
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

        try{
            stage.focusedProperty().addListener(o -> close());
        }
        catch (Exception e){
            System.out.print(e);
        }
    }

    public void close(){
        stage.close();
    }
}
