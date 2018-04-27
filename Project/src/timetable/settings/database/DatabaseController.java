package timetable.settings.database;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private boolean dbChange;
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
        drag.setOnDragOver(this::dragOver);
        drag.setOnDragDropped(this::dragDropped);

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
                    dbChange = true;
                    mysql.setSelected(false);
                    /*we gaan enkel de mysql uitzetten, we gaan geen absolute paden in onze config gaan zetten omdat er geen
                     * garantie is dat het programma altijd op dezelfde pc gaat draaien */
                    properties.setProperty("DB.use", "false");
                    url = "jdbc:sqlite:" + file.getPath();
                    mainController.setDbName(file.getName());
                    mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider(url));
                    Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
                    mainController.getDbLogo().setImage(image);
                    /*Dynamisch inladen van de periods */
                    try {
                        FXMLLoader loader = new FXMLLoader(DatabaseController.class.getResource("periods.fxml"));
                        loader.setController(this);
                        AnchorPane pane = loader.load();
                        rootPane.getChildren().addAll(pane);
                        class ButtonCell extends TableCell<Period, Boolean> {
                            private final Button cellButton = new Button();

                            private ButtonCell() {
                                cellButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent t) {
                                        int selectdIndex = getTableRow().getIndex();
                                        //Create a new table show details of the selected item
                                        Period selectedRecord = table.getItems().get(selectdIndex);
                                        delete(selectedRecord);
                                    }
                                });
                            }

                            @Override
                            protected void updateItem(Boolean t, boolean empty) {
                                super.updateItem(t, empty);
                                if (!empty) {
                                    setGraphic(cellButton);
                                }
                            }
                        }
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
                        hour.setOnEditCommit(event -> updatehour(event.getRowValue(), event.getNewValue()));
                        minute.setCellValueFactory(new PropertyValueFactory<>("minute"));
                        minute.setCellFactory(column -> {
                            TableCell<Period, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter());
                            cell.setAlignment(Pos.CENTER);
                            return cell;
                        });
                        minute.setOnEditCommit(event -> updatemin(event.getRowValue(), event.getNewValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
                        table.getItems().addAll(dac.getPeriodDAO().getPeriods());
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dragDropped(DragEvent event) {
        dbChange = true;
        mysql.setSelected(false);
        /*we gaan enkel de mysql uitzetten, we gaan geen absolute paden in onze config gaan zetten omdat er geen
         * garantie is dat het programma altijd op dezelfde pc gaat draaien */
        properties.setProperty("DB.use", "false");
        File file = event.getDragboard().getFiles().get(0);
        mainController.setDbName(file.getName());
        url = "jdbc:sqlite:" + file.getPath();
        this.close();
    }

    public void chooseDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open Database");
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("Database files (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(ext);
        fileChooser.setSelectedExtensionFilter(ext);
        File file = fileChooser.showOpenDialog(stage);
        dbChange = true;
        mysql.setSelected(false);
        properties.setProperty("DB.use", "false");
        mainController.setDbName(file.getName());
        url = "jdbc:sqlite:" + file.getPath();
        this.close();
    }

    public void close() {
        //als de settings i.v.m de DB gewijzigd is, laat die metteen ook aan passen in de mainController
        //zodat een een herstart van programma niet nodig is.
        if (dbChange) {
            if (mysql.isSelected()) {
                //nieuwe data provider
                /*mainController.model.setDataAccessProvider(new MysqlDataAccessProvider());*/
                //aanduiding aanpassen
                Image image = new Image(Main.class.getResourceAsStream("resources/images/mysql.png"));
                mainController.getDbLogo().setImage(image);
                mainController.setDbName("Online");
            } else if (url == null) {
                //anders is het sqlite
                mainController.getModel().setDataAccessProvider(new SqliteDataAccessProvider());
                Image image = new Image(Main.class.getResourceAsStream("resources/images/sqlite.png"));
                mainController.getDbLogo().setImage(image);
                mainController.setDbName("Lessenrooster(offline)");
            }
            mainController.getModel().changeItems(mainController.getModel().getStandardSchedule());
        }
        settingsController.setProperties(this.properties);
        settingsController.show();
        stage.close();
    }

    private void updatehour(Period period, Integer hour) {
        period.setHour(hour);
        System.out.println("id: " + period.getId() + " hour: " + period.getHour() + " minute: " + period.getMinute());

        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            dac.getPeriodDAO().updatePeriods(period);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatemin(Period period, Integer min) {
        period.setMinute(min);
        System.out.println("id: " + period.getId() + " hour: " + period.getHour() + " minute: " + period.getMinute());

        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            dac.getPeriodDAO().updatePeriods(period);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void period() {
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
