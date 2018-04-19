package timetable.settings.database;

import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import timetable.Controller;
import timetable.Main;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.settings.SettingsController;

import java.io.*;
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
        mysql.setDisable(true);
        mysql.setText("Use Mysql? (disabled not compiled with mysql lib)");
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

    public void newDb() {
        /*lege db met lege tabellen zit al in de prog, dus gewoon een kopie maken*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Database");
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Database files (*.db)", "*.db");

        fileChooser.getExtensionFilters().add(ext);

        fileChooser.setSelectedExtensionFilter(ext);
        File file = fileChooser.showSaveDialog(stage);

        InputStream stream = null;
        OutputStream resStreamOut = null;
        String folder;
        try {
            stream = Main.class.getResourceAsStream("empty.db");
            if (stream == null) {
                throw new Exception("Couldn't create new db.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            folder = file.toURI().getPath();
            resStreamOut = new FileOutputStream(folder);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (resStreamOut != null) {
                    resStreamOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        dbChange = true;
        mysql.setSelected(false);
        /*we gaan enkel de mysql uitzetten, we gaan geen absolute paden in onze config gaan zetten omdat er geen
         * garantie is dat het programma altijd op dezelfde pc gaat draaien */
        properties.setProperty("DB.use", "false");
        url = "jdbc:sqlite:" + file.getPath();
        close();

    }

    public void dragDropped(DragEvent event) {
        dbChange = true;
        mysql.setSelected(false);
        /*we gaan enkel de mysql uitzetten, we gaan geen absolute paden in onze config gaan zetten omdat er geen
         * garantie is dat het programma altijd op dezelfde pc gaat draaien */
        properties.setProperty("DB.use", "false");
        List<File> files = event.getDragboard().getFiles();
        url = "jdbc:sqlite:" + files.get(0).getPath();
    }

    public void chooseDB() {

    }

    public void close() {
        //als de settings i.v.m de DB gewijzigd is, laat die metteen ook aan passen in de mainController
        //zodat een een herstart van programma niet nodig is.
        if (dbChange) {
            if (mysql.isSelected()) {
                //nieuwe data provider
                // TODO: 18/04/2018 geter en seters 
                /*mainController.model.setDataAccessProvider(new MysqlDataAccessProvider());*/
                //aanduiding aanpassen
                Image image = new Image(Main.class.getResourceAsStream("resources/images/mysql.png"));
                mainController.dbLogo.setImage(image);
            } else if (url != null) {
                mainController.model.setDataAccessProvider(new SqliteDataAccessProvider(url));
                Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
                mainController.dbLogo.setImage(image);
            } else {
                //anders is het sqlite
                mainController.model.setDataAccessProvider(new SqliteDataAccessProvider());
                Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
                mainController.dbLogo.setImage(image);
            }
        }
        settingsController.properties = this.properties;
        settingsController.show();
        stage.close();
    }

}
