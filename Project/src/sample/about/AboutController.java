package sample.about;

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
        properties.load(AboutController.class.getResourceAsStream("../myapp.properties"));
        //bewust alle text in veld gestoken
        //kleinere fxml en tekst staat dan ook altijd mooi onder elkaar, dankzij de new-line
        text.setText ("Version: " + properties.getProperty("version")+
                "\nMade by: " + properties.getProperty("name") +
                "\nEmail: " + properties.getProperty("email"));

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
