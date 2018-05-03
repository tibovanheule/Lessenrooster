package timetable.settings.database;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import timetable.Controller;
import timetable.Main;
import timetable.StdError;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Period;
import timetable.settings.SettingsController;
import timetable.settings.database.table.CustomTableColumn;
import timetable.settings.database.table.PeriodButtonCell;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Class to let users change db settings
 *
 * @author Tibo Vanheule
 */
public class DatabaseController {
    private Controller mainController;
    private Properties properties;
    private Stage stage;
    private SettingsController settingsController;
    @FXML
    private CheckBox mysql;
    @FXML
    private ImageView drag;
    private String url;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Period> table;
    @FXML
    private CustomTableColumn hour, minute;
    @FXML
    private TableColumn<Period, Boolean> delete;

    public void initialize() {
        mysql.setDisable(true);
        mysql.setText("Use Mysql? (disabled not compiled with mysql lib)");
    }

    /**
     * sets up fields
     */
    public void setStageAndSetupListeners(Stage stage, Controller controller, SettingsController settingsController, Properties properties) {
        this.stage = stage;
        this.mainController = controller;
        this.settingsController = settingsController;
        this.properties = properties;
        drag.setOnDragOver(this::dragOver);
        drag.setOnDragDropped(this::dragDropped);
        /*mysql.setSelected(Boolean.parseBoolean(properties.getProperty("DB.use")));*/
    }

    /**
     * change to mysql
     */
    public void mysql() {
        if (mysql.isSelected()) {
            properties.setProperty("DB.use", "true");
            //nieuwe data provider
            /*mainController.model.setDataAccessProvider(new MysqlDataAccessProvider());*/
            mainController.setDbName("Online");
            close();
        }
    }

    /**
     * change to standard sqlite database
     */
    public void sqlite() {
        properties.setProperty("DB.use", "false");
        mysql.setSelected(false);
        mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider());
        mainController.setDbName("Lessenrooster(offline)");
        close();
    }

    /**
     * show the periods table
     */
    public void period() {
        try {
            FXMLLoader loader = new FXMLLoader(DatabaseController.class.getResource("periods.fxml"));
            loader.setController(this);
            AnchorPane pane = loader.load();
            rootPane.getChildren().addAll(pane);

            delete.setCellFactory(column -> {
                return new PeriodButtonCell(table, this);
            });
            hour.setup(mainController, new PropertyValueFactory<>("hour"));
            minute.setup(mainController, new PropertyValueFactory<>("minute"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            table.getItems().addAll(dac.getPeriodDAO().getPeriods());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * if a file is drag over, accept it.
     */
    private void dragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    /**
     * makes a new DB
     */
    public void newDb() {
        /*lege db met lege tabellen zit al in de prog, dus gewoon een kopie maken*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Database");
        FileChooser.ExtensionFilter db = new FileChooser.ExtensionFilter("Database files (*.db)", "*.db");
        FileChooser.ExtensionFilter sqlite = new FileChooser.ExtensionFilter("Sqlite Database files (*.sqlite)", "*.sqlite");
        fileChooser.getExtensionFilters().addAll(db, sqlite);
        fileChooser.setSelectedExtensionFilter(db);
        Path file = fileChooser.showSaveDialog(stage).toPath();

        try (InputStream stream = Main.class.getResourceAsStream("empty.db");
             OutputStream resStreamOut = new FileOutputStream(file.toFile())) {
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            if (setDatabase(file)) {
                period();
            }
        } catch (IOException ex) {
            new StdError(ex.getMessage());
        } catch (NullPointerException e) {
            /*user has canceled the filechooser dialog*/
        }
    }

    /**
     * invokes setDatabase when a files has been dropped
     */
    private void dragDropped(DragEvent event) {
        if (setDatabase(event.getDragboard().getFiles().get(0).toPath())) {
            close();
        }
    }

    /**
     * opens a choose dialog
     */
    public void chooseDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open Database");
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Database files (*.db)", "*.db");
        FileChooser.ExtensionFilter sqlite = new FileChooser.ExtensionFilter("Sqlite Database files (*.sqlite)", "*.sqlite");
        fileChooser.getExtensionFilters().addAll(ext, sqlite);
        fileChooser.setSelectedExtensionFilter(ext);
        try {
            if (setDatabase(fileChooser.showOpenDialog(stage).toPath())) {
                close();
            }
        } catch (NullPointerException e) {
            /*user has canceled*/
        }
    }

    /**
     * changes the data access provoider
     * checks if not null -> user canceled filechooser dialog
     * check if writable and readable
     * check if is sqlite database
     */
    private boolean setDatabase(Path file) {
        /*http://fileformats.archiveteam.org/wiki/DB_(SQLite) <- mime type gevonden*/
        try {
            if (file != null) {
                if (Files.isReadable(file) && Files.isWritable(file)) {
                    url = "jdbc:sqlite:" + file.toString();
                    try {
                        /*check sqlite, if error occurs , then the file is not an sqlite DB*/
                        DataAccessProvider dataAccessProvider = new SqliteDataAccessProvider(url);
                        dataAccessProvider.getDataAccessContext().getStudentsDAO().nameExists("test");
                        dataAccessProvider.getDataAccessContext().getPeriodDAO().getPeriods();
                        dataAccessProvider.getDataAccessContext().close();
                        /*set db file and dataAccessProvoider*/
                        properties.setProperty("DB.use", "false");
                        mainController.setDbName(file.getFileName().toString().replace(".db", ""));
                        mainController.getModel().setDataAccessProvider(dataAccessProvider);

                    } catch (Exception e) {
                        new StdError("Error", "Wrong file", "Not a valid Database", Alert.AlertType.ERROR);
                        return false;
                    }
                } else {
                    new StdError("Error", "File", "You're file is either not writable or is not readable", Alert.AlertType.ERROR);
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * closes stage and changes properties
     */
    public void close() {
        mainController.getModel().changeItems(mainController.getModel().getStandardSchedule());
        mainController.getModel().refresh();
        settingsController.setProperties(this.properties);
        stage.close();
        settingsController.setCanClose(true);
    }

    /**
     * create a period
     */
    public void createPeriod() {
        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            table.getItems().add(dac.getPeriodDAO().createPeriod());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a period
     */
    public void delete(Period period) {
        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            dac.getPeriodDAO().deletePeriods(period);
            table.getItems().remove(period);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}