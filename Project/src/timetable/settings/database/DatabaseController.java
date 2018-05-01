package timetable.settings.database;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.StageStyle;
import timetable.Controller;
import timetable.Main;
import timetable.StdError;
import timetable.create.CreateController;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Period;
import timetable.settings.SettingsController;

import java.io.*;
import java.util.Properties;

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

    public void setStageAndSetupListeners(Stage stage, Controller controller, SettingsController settingsController, Properties properties) {
        this.stage = stage;
        this.mainController = controller;
        this.settingsController = settingsController;
        this.properties = properties;
        drag.setOnDragOver(this::dragOver);
        drag.setOnDragDropped(this::dragDropped);
        /*mysql.setSelected(Boolean.parseBoolean(properties.getProperty("DB.use")));*/
    }

    public void mysql() {
        properties.setProperty("DB.use", String.valueOf(mysql.isSelected()));
        if (mysql.isSelected()) {
            //nieuwe data provider
            /*mainController.model.setDataAccessProvider(new MysqlDataAccessProvider());*/
            mainController.setDbName("Online");
        } else {
            // TODO: 30/04/2018 Sqlite standaard rooster moet duidlijker!
            //anders is het sqlite
            mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider());
            mainController.setDbName("Lessenrooster(offline)");
        }
    }

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

    private void dragOver(DragEvent event) {
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

        try (InputStream stream = Main.class.getResourceAsStream("empty.db");
             OutputStream resStreamOut = new FileOutputStream(file.getPath())) {
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            setDatabase(file);
            period();
        } catch (IOException ex) {
            new StdError(ex.getMessage());
        } catch (NullPointerException e) {
            /*user has canceled the filechooser dialog*/
        }
    }

    private void dragDropped(DragEvent event) {
        setDatabase(event.getDragboard().getFiles().get(0));
        close();
    }

    public void chooseDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open Database");
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Database files (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(ext);
        fileChooser.setSelectedExtensionFilter(ext);
        setDatabase(fileChooser.showOpenDialog(stage));
        close();
    }

    private void setDatabase(File file) {
        if (file != null) {
            mysql.setSelected(false);
            properties.setProperty("DB.use", "false");
            mainController.setDbName(file.getName().replace(".db", ""));
            url = "jdbc:sqlite:" + file.getPath();
            mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider(url));
        }
    }

    public void close() {
        mainController.getModel().changeItems(mainController.getModel().getStandardSchedule());
        settingsController.setProperties(this.properties);
        stage.close();
        settingsController.setCanClose(true);
        /*menu to add student and so on*/
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("create/create.fxml"));
            loader.setController(new CreateController());
            Parent root = loader.load();
            CreateController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            controller.setStageAndSetupListeners(stage, mainController);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPeriod() {
        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            table.getItems().add(dac.getPeriodDAO().createPeriod());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void delete(Period period) {
        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            dac.getPeriodDAO().deletePeriods(period);
            table.getItems().remove(period);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}