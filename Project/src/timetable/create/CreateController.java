//Tibo Vanheule
package timetable.create;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import timetable.config.Config;

import java.io.IOException;
import java.util.Properties;

public class CreateController {
    private Stage stage;
    @FXML
    private TextField name;
    @FXML
    private AnchorPane rootPane;

    public void setStageAndSetupListeners(Stage stage ) {
        //krijgen van de stage
        this.stage = stage;
    }

    public void initialize() {


    }
    public void student(){
        try {
            AnchorPane pane = FXMLLoader.load(CreateController.class.getResource("student.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void saveStudent(){

    }

    public void menu(){
        try {
            AnchorPane pane = FXMLLoader.load(CreateController.class.getResource("create.fxml"));
            rootPane.getChildren().setAll(pane);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void close() {
        //afsluiten stage
        stage.close();
    }
}
