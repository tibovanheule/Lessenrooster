package sample.about;

import javafx.beans.value.ChangeListener;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.Properties;

public class aboutController {
    public Label version;
    public Label mail;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;

    }


    public void initialize() throws Exception{
        //Properties properties = new Properties();
        //properties.load(aboutController.class.getResourceAsStream("sample/myapp.properties"));
        //version.setText ("Version: " + properties.getProperty("version"));
        //mail.setText(properties.getProperty("email"));
        //stage.focusedProperty().addListener(o -> close());
        //stage.focusedProperty();
    }

    public void close(){
        stage.close();
    }
}
