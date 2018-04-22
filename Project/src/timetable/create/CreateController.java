//Tibo Vanheule
package timetable.create;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import timetable.config.Config;

import java.util.Properties;

public class CreateController {
    public Label text;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage) {
        //krijgen van de stage
        this.stage = stage;
    }

    public void initialize() {



    }

    public void close() {
        //afsluiten stage
        stage.close();
    }
}
