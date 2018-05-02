//Tibo Vanheule
package timetable.settings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.Controller;
import timetable.Main;
import timetable.config.Config;
import timetable.settings.database.DatabaseController;

import java.util.Properties;

/**
 * Class to let the user set the settings in the property file and change DB settings
 *
 * @author Tibo Vanheule
 */
public class SettingsController {
    /**
     * lists for the settings
     */
    private static final String[] defaultList = {"students", "teacher", "location"}, cities = {"koksijde", "oostende", "gent", "brugge", "brussel", "leuven", "Antwerpen"};
    @FXML
    private CheckBox windowSize;
    @FXML
    private ComboBox<String> defaultStartup, weatherCity;
    private Stage stage;
    private Boolean canClose = true;
    private Properties properties = new Properties();
    private Controller mainController;

    public void setCanClose(Boolean canClose) {
        this.canClose = canClose;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * sets the fields stage and mainController
     */
    public void setStageAndSetupListeners(Stage stage, Controller main) {
        this.stage = stage;
        this.mainController = main;
    }

    /**
     * Loads, read properties to set all settings options
     */
    public void initialize() {
        Config config = new Config();
        properties = config.getproperties();

        defaultStartup.getItems().addAll(defaultList);
        defaultStartup.setValue(properties.getProperty("standard.schedule"));

        weatherCity.getItems().addAll(cities);
        weatherCity.setValue(properties.getProperty("weather.city"));

        windowSize.setSelected(Boolean.parseBoolean(properties.getProperty("startMaximized")));

        windowSize.selectedProperty().addListener(o -> startMaximized());
        defaultStartup.getSelectionModel().selectedItemProperty().addListener(o -> startupSchedule());
        weatherCity.getSelectionModel().selectedItemProperty().addListener(o -> city());
    }

    /**
     * saves property, start program maximized.
     */
    private void startMaximized() {
        properties.setProperty("startMaximized", String.valueOf(windowSize.isSelected()));
    }

    /**
     * saves property, show program knows wich list to show at startup.
     */
    private void startupSchedule() {
        properties.setProperty("standard.schedule", defaultStartup.getSelectionModel().getSelectedItem());
        mainController.getModel().setStandardSchedule(defaultStartup.getSelectionModel().getSelectedItem());
    }

    /**
     * saves property, city for weather updates
     */
    private void city() {
        properties.setProperty("weather.city", weatherCity.getSelectionModel().getSelectedItem());
    }

    /**
     * opens stage for the DBsettings
     */
    public void dbSettings() {
        try {
            canClose = false;
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("settings/database/database.fxml"));
            Parent root = loader.load();
            DatabaseController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 450, 450);
            controller.setStageAndSetupListeners(stage, mainController, this, properties);
            stage.setScene(scene);
            stage.initOwner(this.stage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.requestFocus();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * shows stage, gives it focus
     */
    public void show() {
        stage.requestFocus();
        canClose = true;
    }

    /**
     * saves properties and closes if allowed
     */
    public void close() {
        Config config = new Config();
        config.saveProperties(properties);

        if (canClose) {
            stage.close();
        }
    }
}
