package timetable.settings;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import timetable.Main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsController {
    private Stage stage;
    public CheckBox maximized;
    public ComboBox defaultStartup;
    private Properties properties = new Properties();
    private final String[] defaultList = {"students", "teachers", "location"};

    public void setStageAndSetupListeners(Stage stage){
        this.stage = stage;

    }

    public void initialize() throws IOException {
        //laad het configuratie bestand in
        properties.load(Main.class.getResourceAsStream("lessenrooster.properties"));
        //initialisatie van de velden
        defaultStartup.getItems().addAll(defaultList);
        maximized.setSelected(Boolean.parseBoolean(properties.getProperty("startMaximized")));

        maximized.selectedProperty().addListener(o -> startMaximized());
        defaultStartup.getSelectionModel().selectedItemProperty().addListener(o -> startupSchedule());
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
        // TODO: 18/03/2018
        System.out.println("test");
        //valueOf gebruikt doordat toString een Null pointer Exception kan geven
        properties.setProperty("startMaximized",String.valueOf(maximized.isSelected()));


    }

    public void startupSchedule(){
        properties.setProperty("standard.shedule",defaultStartup.getSelectionModel().getSelectedItem().toString());
        try{
            properties.store(new FileOutputStream("lessenrooster.properties"), null);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void close(){
        stage.close();
    }
}
