//Tibo Vanheule
package timetable.settings;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.Controller;
import timetable.Main;
import timetable.config.Config;
import timetable.settings.database.DatabaseController;
import timetable.settings.periods.PeriodsController;

import java.util.Properties;

public class SettingsController {
    //array met mogelijkheden voor de standaard lijsten uitbreiding mogelijk
    private static final String[] defaultList = {"students", "teacher", "location"}, cities = {"koksijde", "oostende", "gent", "brugge", "brussel", "leuven", "Antwerpen"};
    public CheckBox windowSize;
    public ComboBox<String> defaultStartup, weatherCity;
    private Stage stage;
    private Boolean canClose = true;
    public Properties properties = new Properties();
    private Controller mainController;

    public void setStageAndSetupListeners(Stage stage, Controller main) {
        //Krijg de stage
        this.stage = stage;
        //krijg de mainController
        this.mainController = main;
    }

    public void initialize() {
        //Laad het configuratie bestand in
        Config config = new Config();
        properties = config.getproperties();

        //Initialisatie van de velden
        //Toeveogen elementen voor keuze van de standaard lijst
        defaultStartup.getItems().addAll(defaultList);
        defaultStartup.setValue(properties.getProperty("standard.schedule"));

        weatherCity.getItems().addAll(cities);
        weatherCity.setValue(properties.getProperty("weather.city"));


        //veld true of vals
        windowSize.setSelected(Boolean.parseBoolean(properties.getProperty("startMaximized")));

        //De luisteraars
        //Als er een wordt geklikt of geselecteerd voeg de functie in de lamba uit
        windowSize.selectedProperty().addListener(o -> startMaximized());
        defaultStartup.getSelectionModel().selectedItemProperty().addListener(o -> startupSchedule());
        weatherCity.getSelectionModel().selectedItemProperty().addListener(o -> city());



    }

    private void startMaximized() {
        //ValueOf gebruikt doordat toString een Null pointer Exception kan geven
        properties.setProperty("startMaximized", String.valueOf(windowSize.isSelected()));
    }

    private void startupSchedule() {
        //Selectie in property steken
        properties.setProperty("standard.schedule", defaultStartup.getSelectionModel().getSelectedItem());
        mainController.model.standardSchedule = defaultStartup.getSelectionModel().getSelectedItem();
    }

    private void city() {
        //Selectie in property steken
        properties.setProperty("weather.city", weatherCity.getSelectionModel().getSelectedItem());
    }

    public void dbSettings() {
        try {
            canClose = false;
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("settings/database/database.fxml"));
            Parent root = loader.load();
            DatabaseController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 450, 450);
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage, mainController, this, properties);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void period() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("settings/periods/periods.fxml"));
            Parent root = loader.load();
            PeriodsController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 450, 450);
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage, mainController, this, properties);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.requestFocus();
        canClose = true;
    }

    public void close() {
        //doorgeven aan config klasse om op te slaan
        Config config = new Config();
        config.saveProperties(properties);

        if (canClose) {
            //Stage afsluiten
            stage.close();
        }
    }
}
