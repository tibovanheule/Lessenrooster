package timetable.settings.database;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import timetable.Controller;
import timetable.Main;
import timetable.StdError;
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
    private TableColumn<Period, Integer> hour, minute;
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
        /*
        mysql.setSelected(Boolean.parseBoolean(properties.getProperty("DB.use")));*/
        /*deze zijn puur zodat intellij niet zou klagen  */
        table = null;
        hour = null;
        minute = null;
        delete = null;
    }

    public void mysql() {
        properties.setProperty("DB.use", String.valueOf(mysql.isSelected()));
        if (mysql.isSelected()) {
            //nieuwe data provider
            /*mainController.model.setDataAccessProvider(new MysqlDataAccessProvider());*/
            //aanduiding aanpassen
            Image image = new Image(Main.class.getResourceAsStream("resources/images/mysql.png"));
            mainController.getDbLogo().setImage(image);
            mainController.setDbName("Online");
        } else {
            //anders is het sqlite
            mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider());
            Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
            mainController.getDbLogo().setImage(image);
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
                ButtonCell cell = new ButtonCell();
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            hour.setCellValueFactory(new PropertyValueFactory<>("hour"));
            hour.setCellFactory(column -> {
                TableCell<Period, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter());
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            hour.setOnEditCommit(event -> update(event.getRowValue(), event.getNewValue(), event.getRowValue().getMinute()));
            minute.setCellValueFactory(new PropertyValueFactory<>("minute"));
            minute.setCellFactory(column -> {
                TableCell<Period, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter());
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            minute.setOnEditCommit(event -> update(event.getRowValue(), event.getRowValue().getHour(), event.getNewValue()));
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
            mysql.setSelected(false);
            /*we gaan enkel de mysql uitzetten, we gaan geen absolute paden in onze config gaan zetten omdat er geen
             * garantie is dat het programma altijd op dezelfde pc gaat draaien */
            properties.setProperty("DB.use", "false");
            url = "jdbc:sqlite:" + file.getPath();
            mainController.setDbName(file.getName());
            mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider(url));
            Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
            mainController.getDbLogo().setImage(image);
            period();
        } catch (IOException ex) {
            new StdError(ex.getMessage());
        } catch (NullPointerException e) {
            new StdError("Couldn't create new database.");
        }
    }

    private void dragDropped(DragEvent event) {
        mysql.setSelected(false);
        /*we gaan enkel de mysql uitzetten, we gaan geen absolute paden in onze config gaan zetten omdat er geen
         * garantie is dat het programma altijd op dezelfde pc gaat draaien */
        properties.setProperty("DB.use", "false");
        File file = event.getDragboard().getFiles().get(0);
        mainController.setDbName(file.getName());
        url = "jdbc:sqlite:" + file.getPath();
        mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider(url));
        Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
        mainController.getDbLogo().setImage(image);
        close();
    }

    public void chooseDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open Database");
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Database files (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(ext);
        fileChooser.setSelectedExtensionFilter(ext);
        File file = fileChooser.showOpenDialog(stage);
        mysql.setSelected(false);
        properties.setProperty("DB.use", "false");
        mainController.setDbName(file.getName());
        url = "jdbc:sqlite:" + file.getPath();
        mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider(url));
        Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
        mainController.getDbLogo().setImage(image);
        close();
    }

    public void close() {
        mainController.getModel().changeItems(mainController.getModel().getStandardSchedule());
        settingsController.setProperties(this.properties);
        stage.close();
        settingsController.setCanClose(true);

    }

    private void update(Period period, Integer hour, Integer minute) {
        period.setHour(hour);
        period.setMinute(minute);
        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            dac.getPeriodDAO().updatePeriods(period);
        } catch (DataAccessException e) {
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

    class ButtonCell extends TableCell<Period, Boolean> {
        private final Button cellButton = new Button();

        private ButtonCell() {
            cellButton.setText("Delete");
            cellButton.setOnAction((event) -> {
                int selectdIndex = getTableRow().getIndex();
                Period selectedRecord = table.getItems().get(selectdIndex);
                delete(selectedRecord);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (empty || t == null) {
                setText(null);
                setGraphic(null);
                setOnMouseClicked(null);
            }
            if (!empty) {
                setGraphic(cellButton);
            }
        }

    }

}
