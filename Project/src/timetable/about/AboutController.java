//Tibo Vanheule
package timetable.about;

import javafx.application.Application;
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
public class AboutController extends Application {
    @FXML
    private Label text;
    private Stage stage;
    private Properties properties;

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
        this.properties = config.getproperties();
        //bewust alle tekst in 1 veld gestoken
        //kleinere fxml en tekst staat dan ook altijd mooi onder elkaar, dankzij de new-line
        text.setText("Version: " + properties.getProperty("program.version") +
                "\nCopyright: " + properties.getProperty("programmer.name") +
                "\nEmail: " + properties.getProperty("programmer.email") +
                "\nLayout (Collorpallet) based on: \n" + properties.getProperty("layout.basedOn") +
                "\nWeather icons from:\n" + properties.getProperty("layout.weather.icons") +
                "\nDocumentation,manual and a mysql-version can be found here: " +
                "\n" + properties.getProperty("programmer.site") + "/artifacts/"
        );
    }

    /**
     * Close the stage
     */
    public void close() {
        stage.close();
    }

    /**
     * Show the manual in browser
     */
    public void manual() {
        getHostServices().showDocument("http://www.tibovanheule.space/artifacts/");
    }

    /**
     * Opens the javadoc in browser
     */
    public void javadoc() {
        getHostServices().showDocument("http://www.tibovanheule.space/artifacts/javadoc");
    }

    /**
     * opens github project in browser
     */
    public void github() {
        getHostServices().showDocument(properties.getProperty("program.github"));
    }

    /**
     * Just so we can extend Application and use it in other methods
     */
    @Override
    public void start(Stage primaryStage) {

    }
}