//Tibo Vanheule
package timetable.about;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import timetable.config.Config;

import java.util.Properties;

/**
 * Class to display information about creator and program
 *
 * @author Tibo Vanheule
 */
public class AboutController {
    @FXML
    private Label text;
    private Stage stage;

    /**
     * get stage to use later
     */
    public void setStageAndSetupListeners(Stage stage) {
        //krijgen van de stage
        this.stage = stage;
    }

    /**
     * set the text, read from the properties
     */
    public void initialize() {

        Config config = new Config();
        Properties properties = config.getproperties();
        //bewust alle tekst in 1 veld gestoken
        //kleinere fxml en tekst staat dan ook altijd mooi onder elkaar, dankzij de new-line
        text.setText("Version: " + properties.getProperty("program.version") +
                "\nCopyright: " + properties.getProperty("programmer.name") +
                "\nMade by: " + properties.getProperty("programmer.name") +
                "\nEmail: " + properties.getProperty("programmer.email") +
                "\nWebsite: " + properties.getProperty("programmer.site") +
                "\nProjects github: " + properties.getProperty("program.github") +
                "\nPrivate repo, send an email with github username for access!" +
                "\nLayout (Collorpallet) based on: \n" + properties.getProperty("layout.basedOn") +
                "\nWeather icons from:\n" + properties.getProperty("layout.weather.icons") +
                "\n\nDocumentation and manual: " +
                "\n" + properties.getProperty("programmer.site") + "/artifacts/"
        );
    }

    /**
     * Close the stage
     */
    public void close() {
        stage.close();
    }
}