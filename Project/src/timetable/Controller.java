/*Tibo vanheule*/
package timetable;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import timetable.config.Config;
import timetable.create.CreateController;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Weather;
import timetable.settings.SettingsController;
import timetable.weather.WeatherController;
import timetable.weather.WeatherScraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main companion class
 * To close the windows, open a stage, time ...
 * @author Tibo Vanheule
 */
public class Controller {

    public MainModel model;
    @FXML
    private Label day, date, time;
    @FXML
    private Label dbName;
    @FXML
    private ImageView dbLogo, weatherIcon;
    @FXML
    private AnchorPane draw;
    private Stage stage;

    /**
     * Change the label text in the window, so user know wich database there are using.*/
    public void setDbName(String dbFile) {
        dbName.setText(dbFile);
    }


    Label getDay() {
        return day;
    }

    Label getDate() {
        return date;
    }

    Label getTime() {
        return time;
    }

    public ImageView getDbLogo() {
        return dbLogo;
    }

    AnchorPane getDraw() {
        return draw;
    }

    public MainModel getModel() {
        return model;
    }

    void setStageAndSetupListeners(Stage controller) {
        this.stage = controller;
    }

    public void initialize() {
        Config config = new Config();
        Properties properties = config.getproperties();

        //als de property true is gebruik dan mysql (voorlopig constant false)
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            /*model.setDataAccessProvider(new MysqlDataAccessProvider());*/
            //deze afbeelding is voor het gemak dan weten we op welke DB we draaien als we het prog draaien
            Image image = new Image(getClass().getResourceAsStream("resources/images/mysql.png"));
            dbLogo.setImage(image);
        } else {
            //in elk ander geval, valt het terug op sqlite
            model.setDataAccessProvider(new SqliteDataAccessProvider());
            Image image = new Image(getClass().getResourceAsStream("resources/images/sqlite.png"));
            dbLogo.setImage(image);
        }
        model.setStandardSchedule(properties.getProperty("standard.schedule"));
        model.changeItems(model.getStandardSchedule());

        dbName.setText("Lessenrooster (offline)");


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
                    LocalDateTime now = LocalDateTime.now();

                    DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("H:mm");
                    DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("EEEE");
                    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("d LLLL");

                    date.setText(now.format(formatDate));
                    day.setText(now.format(formatDay));
                    time.setText(now.format(formatTime));
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);

        //ophalen van het weerbericht gebeurd in een thread
        //voordeel: programma moet niet wachten achter de ophaling
        //nadeel: met zeer goede verbinding zal deze manier iets trager zijn

        Platform.runLater(this::getWeather);

        time.setWrapText(true);
        time.setTextAlignment(TextAlignment.JUSTIFY);

    }

    // TODO: 29/04/2018 Move buttons to views
    /**
     * Change the label text in the window, so user know wich database there are using.*/
    private void getWeather() {
        WeatherScraper weatherscraper = new WeatherScraper();
        Weather weather = weatherscraper.getWeather();

        if (weather.getConnection()) {
            try {
                Image image = new Image(getClass().getResourceAsStream("resources/images/weather/" + weather.getIcon() + ".png"));
                weatherIcon.setImage(image);
            } catch (Exception e) {
                /*e.printStackTrace();*/
            }
        }
    }

    /**
     * Close program with error code 0*/
    public void exit() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Stage to show extra weather info*/
    public void weather() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("weather/weather.fxml"));
            Parent root = loader.load();
            WeatherController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 220));
            controller.setStageAndSetupListeners(stage);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new stage, Stage to create a student or teacher or location.*/
    public void create() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("create/create.fxml"));
            loader.setController(new CreateController());
            Parent root = loader.load();
            CreateController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            controller.setStageAndSetupListeners(stage, this);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new stage and view the settings window*/
    public void settings() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("settings/settings.fxml"));
            Parent root = loader.load();
            SettingsController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            controller.setStageAndSetupListeners(stage, this);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maximize or minimize the window*/
    public void maximize() {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }

    /**
     * Method to show or to hide the right menu drawer*/
    public void drawerAction() {
        FadeTransition ft = new FadeTransition(Duration.millis(300), draw);
        ft.setCycleCount(1);
        TranslateTransition drawerOpen = new TranslateTransition(new Duration(300), draw);
        drawerOpen.setToX(0);
        TranslateTransition drawerClose = new TranslateTransition(new Duration(300), draw);

        //alle children's opacity property verbinden met die van de drawer
        for (Node node : draw.getChildren()) {
            node.opacityProperty().bind(draw.opacityProperty());
        }

        if (draw.isVisible()) {
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(o -> draw.setVisible(false));
            drawerClose.setToX(+(draw.getWidth()));
            ParallelTransition parallelTransition = new ParallelTransition(ft, drawerClose);
            parallelTransition.play();
        } else {
            draw.setVisible(true);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            if (draw.getTranslateX() != 0) {
                ParallelTransition parallelTransition = new ParallelTransition(ft, drawerOpen);
                parallelTransition.play();
            }
        }
    }
}