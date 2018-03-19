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
    public CheckBox windowSize;
    public ComboBox<String> defaultStartup;
    private Properties properties = new Properties();
    //array met mogelijkheden voor de standaard lijsten uitbreiding mogelijk
    private static final String[] defaultList = {"students", "teachers", "location"};

    public void setStageAndSetupListeners(Stage stage){
        //Krijg de stage
        this.stage = stage;
    }

    public void initialize() throws IOException {

        //Laad het configuratie bestand in
        properties.load(Main.class.getResourceAsStream("lessenrooster.properties"));

        //Initialisatie van de velden
        //Toeveogen elementen voor keuze van de standaard lijst
        defaultStartup.getItems().addAll(defaultList);
        //veld true of vals
        windowSize.setSelected(Boolean.parseBoolean(properties.getProperty("startMaximized")));

        //De luisteraars
        //Als er een wordt geklikt of geselecteerd voeg de functie in de lamba uit
        windowSize.selectedProperty().addListener(o -> startMaximized());
        defaultStartup.getSelectionModel().selectedItemProperty().addListener(o -> startupSchedule());

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
    private void startMaximized(){
        // TODO: 18/03/2018
        System.out.println("test");
        //ValueOf gebruikt doordat toString een Null pointer Exception kan geven
        properties.setProperty("startMaximized",String.valueOf(windowSize.isSelected()));


    }

    public void startupSchedule(){

        //Selectie in property steken
        properties.setProperty("standard.shedule",defaultStartup.getSelectionModel().getSelectedItem().toString());

    }

    public void close(){
        //Probeer property's op te slaan in nieuw bestand
        try{
            properties.store(new FileOutputStream("lessenrooster.properties"), "Tibo Vanheule");
        }catch (IOException e){
            System.out.println(e);
        }

        //Stage afsluiten
        stage.close();
    }
}
