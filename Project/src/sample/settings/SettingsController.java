package sample.settings;

import javafx.stage.Stage;
import java.io.IOException;
import java.util.Properties;

public class SettingsController {
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;

    }

    public void initialize() throws IOException {
        Properties properties = new Properties();
        properties.load(SettingsController.class.getResourceAsStream("../myapp.properties"));

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
