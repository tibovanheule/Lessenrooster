package timetable.settings.database;

import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import timetable.Controller;
import timetable.Main;
import timetable.db.mysql.MysqlDataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.settings.SettingsController;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class DatabaseController {
    private Controller mainController;
    private Properties properties;
    private Stage stage;
    private boolean dbChange;
    private SettingsController settingsController;
    public CheckBox mysql;
    private String url;

    public void initialize() {

    }


    public void setStageAndSetupListeners(Stage stage, Controller controller, SettingsController settingsController, Properties properties) {
        this.stage = stage;
        this.mainController = controller;
        this.settingsController = settingsController;
        this.properties = properties;
        mysql.setSelected(Boolean.parseBoolean(properties.getProperty("DB.use")));
    }

    public void mysql() {

        properties.setProperty("DB.use", String.valueOf(mysql.isSelected()));
        dbChange = true;
    }

    public void dragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void dragDropped(DragEvent event) {
        dbChange = true;
        mysql.setSelected(false);
        /*we gaan enkel de mysql uitzetten, we gaan geen absolute paden in onze config gaan zetten omdat er geen
        * garantie is dat het programma altijd op dezelfde pc gaat draaien */
        properties.setProperty("DB.use","false");
        List<File> files = event.getDragboard().getFiles();
        url = "jdbc:sqlite:"+files.get(0).getPath();
    }

    public void chooseDB() {

    }

    public void close() {
        //als de settings i.v.m de DB gewijzigd is, laat die metteen ook aan passen in de mainController
        //zodat een een herstart van programma niet nodig is.
        if (dbChange) {
            if (mysql.isSelected()) {
                //nieuwe data provider
                mainController.model.dataAccessProvider = new MysqlDataAccessProvider();
                //aanduiding aanpassen
                Image image = new Image(Main.class.getResourceAsStream("resources/images/mysql.png"));
                mainController.dbLogo.setImage(image);
            } else if (url != null) {
                mainController.model.dataAccessProvider = new SqliteDataAccessProvider(url);
                Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
                mainController.dbLogo.setImage(image);
            } else {
                //anders is het sqlite
                mainController.model.dataAccessProvider = new SqliteDataAccessProvider();
                Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
                mainController.dbLogo.setImage(image);
            }
        }
        settingsController.properties = this.properties;
        settingsController.show();
        stage.close();
    }
}
