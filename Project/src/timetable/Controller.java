//Tibo vanheule
package timetable;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import timetable.about.AboutController;
import timetable.config.Config;
import timetable.db.Db;
import timetable.db.Mysql;
import timetable.db.Sqlite;
import timetable.objects.Item;
import timetable.objects.Lecture;
import timetable.objects.Weather;
import timetable.settings.SettingsController;
import timetable.weather.WeatherController;
import timetable.weather.WeatherScraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    public Label day;
    public Label date;
    public Label time;
    public ImageView weatherIcon;
    public ListView<String> list;
    public TextField searchText;
    public ListView<String> monday;
    public ListView<String> tuesday;
    public ListView<String> wednesday;
    public ListView<String> thursday;
    public ListView<String> friday;
    public ImageView dbLogo;
    public Button students;
    public Button teachers;
    public Button loc;
    public AnchorPane draw;
    private Stage stage;
    private String standardSchedule;
    private Db database;
    private ArrayList<Item> listElements = new ArrayList<>();

    public void setStageAndSetupListeners(Stage controller) {
        this.stage = controller;
    }

    public void initialize() {
        Config config = new Config();
        Properties properties = config.getproperties();

        //sla op in een veld zodat het in het verdere bestand opgevraagd kan worden
        standardSchedule = properties.getProperty("standard.schedule");

        //als de property true is gebruik dan mysql
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            database = new Db(new Mysql());
            Image image = new Image(getClass().getResourceAsStream("resources/images/mysql.png"));
            dbLogo.setImage(image);
        } else {
            //in elk ander geval, valt het terug op Sqlite
            database = new Db(new Sqlite());
            Image image = new Image(getClass().getResourceAsStream("resources/images/sqlite.png"));
            dbLogo.setImage(image);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
                        //de huidige datum
                        LocalDateTime now = LocalDateTime.now();

                        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("H:mm");
                        DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("EEEE");
                        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("d LLLL");

                        date.setText(now.format(formatDate));
                        day.setText(now.format(formatDay));
                        time.setText(now.format(formatTime));
                    }
                });
            }
        }, 0, 1000);

        listElements.addAll(database.getList(standardSchedule));
        for (Item item : listElements) {
            list.getItems().add(item.getName());
        }

        list.getSelectionModel().selectedItemProperty().addListener(o -> getRooster());
        searchText.textProperty().addListener(o -> search());
        students.setOnAction(o -> updateList(students.getUserData().toString()));
        teachers.setOnAction(o -> updateList(teachers.getUserData().toString()));
        loc.setOnAction(o -> updateList(loc.getUserData().toString()));
        draw.focusTraversableProperty().addListener(o->drawerAction());

        WeatherScraper weatherscraper = new WeatherScraper();
        Weather weather = weatherscraper.getWeather();

        if (weather.getConnection()){
            try {
                Image image = new Image(getClass().getResourceAsStream("resources/images/weather/"+weather.getIcon()+".png"));
                weatherIcon.setImage(image);
            }catch (Exception e){

            }

        }

    }

    public void search() {
        //als textfield leeg is keer dan terug naar de standaard lijst
        if (searchText.getText().isEmpty()) {
            list.getItems().clear();
            listElements.clear();
            listElements.addAll(database.getList(standardSchedule));
            for (Item item : listElements) {
                list.getItems().add(item.getName());
            }
        } else {
            // zo niet haal de gefilterde lijst op
            list.getItems().clear();
            listElements.clear();
            listElements.addAll(database.getFilteredList(searchText.getText()));
            for (Item item : listElements) {
                list.getItems().add(item.getName());
            }
        }
    }

    public void getRooster() {
        // TODO: 18/03/2018  

        //wanneer men klikt op de buttons students teachers wordt de listener ook getriggerd
        //erwordt dan een null waarde geproduceerd door
        //list.getSelectionModel().getSelectedItem()
        if (list.getSelectionModel().getSelectedItem() != null) {
            Item selected = null;
            for (Item item : listElements) {
                if (item.getName().equals(list.getSelectionModel().getSelectedItem())) {
                    selected = item;
                }
            }
            monday.getItems().clear();
            tuesday.getItems().clear();
            wednesday.getItems().clear();
            thursday.getItems().clear();
            friday.getItems().clear();
            for (Lecture lecture : database.getRooster(selected.getSort(), selected.getName())) {
                if (lecture.getDay() == 1) {
                    monday.getItems().add(lecture.getCourse());
                }
                if (lecture.getDay() == 2) {
                    tuesday.getItems().add(lecture.getCourse());
                }
                if (lecture.getDay() == 3) {
                    wednesday.getItems().add(lecture.getCourse());
                }
                if (lecture.getDay() == 4) {
                    thursday.getItems().add(lecture.getCourse());
                }
                if (lecture.getDay() == 5) {
                    friday.getItems().add(lecture.getCourse());
                }
            }
        }
    }

    public void exit() {
        Platform.exit();
        System.exit(0);
    }

    public void about() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("about/about.fxml"));
            Parent root = loader.load();
            AboutController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 450, 450);
            scene.getStylesheets().add("timetable/about/about.css");
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            //list.getItems().add(e.toString());
            e.printStackTrace();
        }
    }

    public void weather() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("weather/weather.fxml"));
            Parent root = loader.load();
            WeatherController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 450, 450);
            scene.getStylesheets().add("timetable/weather/weather.css");
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            //list.getItems().add(e.toString());
            e.printStackTrace();
        }
    }

    public void settings() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("settings/settings.fxml"));
            Parent root = loader.load();
            SettingsController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 450, 450);
            scene.getStylesheets().add("timetable/settings/settings.css");
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateList(String whatList) {

        //textbox leeg maken
        searchText.setText("");
        //listview leeg maken voor nieuwe items
        list.getItems().clear();
        //toevoegen van nieuwe elementen
        listElements.clear();
        listElements.addAll(database.getList(whatList));
        for (Item item : listElements) {
            list.getItems().add(item.getName());
        }
    }

    public void maximize() {
        //is de stage gemaximaliseerd?
        if (stage.isMaximized()) {
            //minimaliseer
            stage.setMaximized(false);
        } else {
            //maximaliseer
            stage.setMaximized(true);
        }
    }

    public void drawerAction() {
        if (draw.isVisible()) {
            FadeTransition ft = new FadeTransition(Duration.millis(300), draw);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setCycleCount(1);
            ft.play();
            ft.setOnFinished(o -> draw.setVisible(false));
        } else {
            draw.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(300), draw);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setCycleCount(1);
            ft.play();
        }
    }
}