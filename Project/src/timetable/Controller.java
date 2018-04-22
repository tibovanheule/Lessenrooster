//Tibo vanheule
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import timetable.config.Config;
import timetable.create.CreateController;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Item;
import timetable.objects.Weather;
import timetable.settings.SettingsController;
import timetable.views.SortButtons;
import timetable.weather.WeatherController;
import timetable.weather.WeatherScraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    @FXML
    private Label day;

    public Label getDay() {
        return day;
    }

    public Label getDate() {
        return date;
    }

    public Label getTime() {
        return time;
    }

    public ImageView getDbLogo() {
        return dbLogo;
    }

    @FXML
    private Label date;
    @FXML
    private Label time;
    @FXML
    private Label appname;
    @FXML
    private TextField searchText;
    @FXML
    private ListView<Item> list;
    @FXML
    private ImageView dbLogo, weatherIcon;
    @FXML
    private SortButtons students, teachers, loc, lecture;

    AnchorPane getDraw() {
        return draw;
    }

    @FXML
    private AnchorPane draw;
    private Stage stage;

    public MainModel getModel() {
        return model;
    }

    public MainModel model;

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

        appname.setText(properties.getProperty("program.name"));


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

        //wanneer men op een andere lijst (de dagen) klikt de slectie wissen in de huidige lijst
        //in alle lijsten is er steeds maar 1 selectie (bugs vermijden)
        //day.focusedProperty().addListener(o -> day.getSelectionModel().clearSelection());
        //listeners opzetten


        /*day.getSelectionModel().selectedItemProperty().addListener(o -> lecture(day.getSelectionModel().getSelectedItem()));
         */

    }

    private void getWeather() {
        WeatherScraper weatherscraper = new WeatherScraper();
        Weather weather = weatherscraper.getWeather();

        if (weather.getConnection()) {
            try {
                Image image = new Image(getClass().getResourceAsStream("resources/images/weather/" + weather.getIcon() + ".png"));
                weatherIcon.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                //omdat de fout meestal komt door een verkeerd getypt icon in de resources
                // is het handig de icon name te printen ;)
                System.out.println(weather.getIcon());
            }
        }
    }

    /*wanneer er op het kruisje wordt gedrukt programma afsluiten en jvm afsluiten met foutcode (0 in dit geval)*/
    public void exit() {
        Platform.exit();
        System.exit(0);
    }

    /*functie om het weerbericht op te roepen*/
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

    public void create() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("create/create.fxml"));
            Parent root = loader.load();
            CreateController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            controller.setStageAndSetupListeners(stage);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*functie om de settings scherm op te roepen*/
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

    /*functie om de maximaliseer button te laten werken (maximaliseer als het klein is en omgekeerd)*/
    public void maximize() {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }

    /*functie voor de drawer te laten verschijnen of verdwijnen. werkt met een fade en translate transition die
     * tegelijker tijd wordt afgespeeld door de parallel transition.
     */
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