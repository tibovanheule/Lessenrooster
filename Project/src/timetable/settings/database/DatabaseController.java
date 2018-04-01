package timetable.settings.database;

import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import timetable.Controller;
import timetable.Main;
import timetable.config.Config;
import timetable.db.mysql.MysqlDataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.settings.SettingsController;

import java.util.Properties;

public class DatabaseController {
    private Controller mainController;
    private Properties properties;
    private Stage stage;
    private boolean dbChange;
    private SettingsController settingsController;
    public CheckBox mysql;
    public void initialize(){
        //Laad het configuratie bestand in
        Config config = new Config();
        properties = config.getproperties();

        mysql.setSelected(Boolean.parseBoolean(properties.getProperty("DB.use")));
    }



    public void setStageAndSetupListeners(Stage stage, Controller controller, SettingsController settingsController){
        this.stage = stage;
        this.mainController = controller;
        this.settingsController = settingsController;
    }

    public void mysql(){

        properties.setProperty("DB.use",String.valueOf(mysql.isSelected()));
        dbChange = true;
    }

    public void dragOver(DragEvent event){
        event.acceptTransferModes(TransferMode.ANY);
    }

    public void close(){
        //als de settings i.v.m de DB gewijzigd is, laat die metteen ook aan passen in de mainController
        //zodat een een herstart van programma niet nodig is.
        if (dbChange) {
            if (mysql.isSelected()) {
                //nieuwe data provider
                mainController.dataAccessProvider = new MysqlDataAccessProvider();
                //aanduiding aanpassen
                Image image = new Image(Main.class.getResourceAsStream("resources/images/mysql.png"));
                mainController.dbLogo.setImage(image);
            } else {
                //anders is het sqlite
                mainController.dataAccessProvider = new SqliteDataAccessProvider();
                Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
                mainController.dbLogo.setImage(image);
            }
        }
        settingsController.show();
        stage.close();
    }
}
