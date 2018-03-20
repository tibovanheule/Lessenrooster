//Tibo Vanheule
package timetable.settings;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import timetable.Config;
import timetable.Main;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsController {
    private Stage stage;
    public CheckBox windowSize;
    public ComboBox<String> defaultStartup;
    public CheckBox mysql;
    private Properties properties = new Properties();
    //array met mogelijkheden voor de standaard lijsten uitbreiding mogelijk
    private static final String[] defaultList = {"students", "teacher", "location"};

    public void setStageAndSetupListeners(Stage stage){
        //Krijg de stage
        this.stage = stage;
    }

    public void initialize() throws IOException {

        //Laad het configuratie bestand in
        Config config = new Config();
        properties =  config.getproperties();

        //Initialisatie van de velden
        //Toeveogen elementen voor keuze van de standaard lijst
        defaultStartup.getItems().addAll(defaultList);
        defaultStartup.setValue(properties.getProperty("standard.schedule"));

        mysql.setSelected(Boolean.parseBoolean(properties.getProperty("DB.use")));

        //veld true of vals
        windowSize.setSelected(Boolean.parseBoolean(properties.getProperty("startMaximized")));

        //De luisteraars
        //Als er een wordt geklikt of geselecteerd voeg de functie in de lamba uit
        windowSize.selectedProperty().addListener(o -> startMaximized());
        defaultStartup.getSelectionModel().selectedItemProperty().addListener(o -> startupSchedule());
        mysql.selectedProperty().addListener(o -> mysql());

        //Probeersel
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

    private void mysql(){
        //ValueOf gebruikt doordat toString een Null pointer Exception kan geven
        properties.setProperty("DB.use",String.valueOf(mysql.isSelected()));
    }
    private void startMaximized(){
        //ValueOf gebruikt doordat toString een Null pointer Exception kan geven
        properties.setProperty("startMaximized",String.valueOf(windowSize.isSelected()));


    }

    public void startupSchedule(){

        //Selectie in property steken
        properties.setProperty("standard.schedule",defaultStartup.getSelectionModel().getSelectedItem().toString());

    }

    public void close(){
        //doorgeven aan config klasse om op te slaan
        Config config = new Config();
        config.saveProperties(properties);

        //Stage afsluiten
        stage.close();
    }
}
