package timetable.settings;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import timetable.Main;

import java.io.IOException;
import java.util.Properties;

public class SettingsController {
    private Stage stage;
    public CheckBox maximized;
    public ComboBox defaultStartup;
    public Properties properties = new Properties();
    public final String[] defaultList = {"students", "teachers", "location"};

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;

    }

    public void initialize() throws IOException {

        //laad het configuratie bestand in
        properties.load(Main.class.getResourceAsStream("lessenrooster.properties"));
        //initialisatie van de velden
        defaultStartup.getItems().addAll(defaultList);
        //maximized.setSelected(Boolean.parseBoolean(properties.getProperty("startMaximized")));

        maximized.selectedProperty().addListener(o -> startMaximized());
        defaultStartup.selectionModelProperty().addListener(o -> startup());
        focus();
    }
    private void focus(){
        try{
            stage.focusedProperty().addListener(o -> close());
        }
        catch (Exception e){
            System.out.print(e);
        }
    }
    private void startMaximized(){

        //valueOf gebruikt doordat toString een Null pointer Exception kan geven
        properties.put("startMaximized",String.valueOf(maximized.isSelected()));

    }

    public void startup(){
        properties.put("standard.shedule",defaultStartup.getSelectionModel().getSelectedItem().toString());
    }

    public void close(){
        stage.close();
    }
}
