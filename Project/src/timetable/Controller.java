//Tibo vanheule
package timetable;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import timetable.about.AboutController;
import timetable.config.Config;
import timetable.db.*;
import timetable.db.mysql.MysqlDataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.lecture.LectureController;
import timetable.objects.Item;
import timetable.objects.Lecture;
import timetable.objects.Weather;
import timetable.settings.SettingsController;
import timetable.views.SortButtons;
import timetable.weather.WeatherController;
import timetable.weather.WeatherScraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller {

    public Label day, date, time, appname;
    public TextField searchText;
    public ListView<String> monday, tuesday, wednesday, thursday, friday, list;
    public ImageView dbLogo, weatherIcon;
    public SortButtons students, teachers, loc;
    public AnchorPane draw;

    private Stage stage;
    private String standardSchedule;
    private Db database;
    public DataAccessProvider dataAccessProvider;
    private HashMap<Integer, ArrayList<Lecture>> schedule;
    public HashMap<String,Item> listElements = new HashMap<>();

    public void setStageAndSetupListeners(Stage controller) {
        this.stage = controller;
    }

    public void initialize() {
        Config config = new Config();
        Properties properties = config.getproperties();

        appname.setText(properties.getProperty("program.name"));
        //sla op in een veld zodat het in het verdere bestand opgevraagd kan worden
        standardSchedule = properties.getProperty("standard.schedule");

        //als de property true is gebruik dan mysql
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            database = new Db(new Mysql());
            dataAccessProvider = new MysqlDataAccessProvider();
            //deze afbeelding is voor het gemak dan weten we op welke DB we draaien als we het prog draaien
            Image image = new Image(getClass().getResourceAsStream("resources/images/mysql.png"));
            dbLogo.setImage(image);
        } else {
            //in elk ander geval, valt het terug op sqlite
            database = new Db(new Sqlite());
            dataAccessProvider = new SqliteDataAccessProvider();
            Image image = new Image(getClass().getResourceAsStream("resources/images/sqlite.png"));
            dbLogo.setImage(image);
        }

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( () -> {
                    //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
                    //de huidige datum
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

        listElements = database.getList(standardSchedule);
        for (Map.Entry<String,Item> entry: listElements.entrySet()) {
            list.getItems().add(entry.getKey());
        }

        list.getSelectionModel().selectedItemProperty().addListener(o -> getRooster(listElements.get(list.getSelectionModel().getSelectedItem())));
        searchText.textProperty().addListener(o -> search());
        students.setOnAction(o -> updateList(students.getUserData().toString()));
        teachers.setOnAction(o -> updateList(teachers.getUserData().toString()));
        loc.setOnAction(o -> updateList(loc.getUserData().toString()));

        //ophalen van het weerbericht gebeurd in een thread
        //voordeel: programma moet niet wachten achter de ophaling
        //nadeel: met zeer goede verbinding zal deze manier iets trager zijn
        Platform.runLater(this::getWeather);

        //wanneer men op een andere lijst (de dagen) klikt de slectie wissen in de huidige lijst
        try {
            monday.focusedProperty().addListener(o -> monday.getSelectionModel().clearSelection());
            tuesday.focusedProperty().addListener(o -> tuesday.getSelectionModel().clearSelection());
            wednesday.focusedProperty().addListener(o -> wednesday.getSelectionModel().clearSelection());
            thursday.focusedProperty().addListener(o -> thursday.getSelectionModel().clearSelection());
            friday.focusedProperty().addListener(o -> friday.getSelectionModel().clearSelection());
            monday.getSelectionModel().selectedItemProperty().addListener(o -> lecture(1,monday.getSelectionModel().getSelectedItem()));
            tuesday.getSelectionModel().selectedItemProperty().addListener(o -> lecture(2,tuesday.getSelectionModel().getSelectedItem()));
            wednesday.getSelectionModel().selectedItemProperty().addListener(o -> lecture(3,wednesday.getSelectionModel().getSelectedItem()));
            thursday.getSelectionModel().selectedItemProperty().addListener(o -> lecture(4,thursday.getSelectionModel().getSelectedItem()));
            friday.getSelectionModel().selectedItemProperty().addListener(o -> lecture(5,friday.getSelectionModel().getSelectedItem()));

        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void getWeather(){
        WeatherScraper weatherscraper = new WeatherScraper();
        Weather weather = weatherscraper.getWeather();

        if (weather.getConnection()) {
            try {
                Image image = new Image(getClass().getResourceAsStream("resources/images/weather/" + weather.getIcon() + ".png"));
                weatherIcon.setImage(image);
            } catch (Exception e) {
                System.out.println(weather.getIcon());
            }
        }
    }

    public void search() {
        list.getItems().clear();
        listElements.clear();
        if (searchText.getText().isEmpty()) {
            //als textfield leeg is keer dan terug naar de standaard lijst
            listElements = database.getList(standardSchedule);
        } else {
            // zo niet haal de gefilterde lijst op
            listElements = database.getFilteredList(searchText.getText());
        }
        for (Map.Entry<String,Item> entry: listElements.entrySet()) {
            list.getItems().add(entry.getKey());
        }
    }

    public void getRooster(Item selected) {
        //wanneer men klikt op de buttons students teachers wordt de listener ook getriggerd
        //erwordt dan een null waarde geproduceerd door
        //list.getSelectionModel().getSelectedItem()
        try {
            //array maken om later iffen te vermijden :)
            ListView[] lists = {monday,tuesday,wednesday,thursday,friday};
            for (ListView list : lists) {
                list.getItems().clear();
            }
            try {
                schedule = database.getRooster(selected.getSort(), selected.getName());
                for (Map.Entry<Integer,ArrayList<Lecture>> entry:schedule.entrySet()) {
                    ArrayList<Lecture> dayList = entry.getValue();
                    for (Lecture lecture : dayList) {
                        lists[lecture.getDay()-1].getItems().add(lecture.getBlock() + " " + lecture.getCourse());
                        // TODO: 27/03/2018
                        //if (lecture.getConflict()){
                          //  lists[lecture.getDay()-1].getStyleClass().add("conflict");
                        //}
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch (Exception e){
            System.out.println(e);
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
            scene.getStylesheets().add("about/about.css");
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            //list.getItems().add(e.toString());
            e.printStackTrace();
        }
    }

    public void lecture(Integer day, String selected) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("lecture/lecture.fxml"));
            Parent root = loader.load();
            LectureController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 450, 450);
            stage.setScene(scene);
            Lecture lecture = schedule.get(day).get(0);
            controller.setStageAndSetupListeners(stage,lecture);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
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
            Scene scene = new Scene(root, 450, 220);
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
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
            scene.getStylesheets().add("settings/settings.css");
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage,this);
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
        HashMap<String, Item> items = new HashMap<>();
        try(DataAccessContext dac = dataAccessProvider.getDataAccessContext()){
            ItemsDAO itemsDAO = dac.getItemDoa();
            for(Item item: itemsDAO.findItem(whatList)){
                listElements.put(item.getName(), item);
            }
        }catch (DataAccessException e){
            System.out.println(e);
        }

        for (Map.Entry<String,Item> item : listElements.entrySet()) {
            list.getItems().add(item.getKey());
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
        FadeTransition ft = new FadeTransition(Duration.millis(300), draw);
        ft.setCycleCount(1);
        TranslateTransition drawerOpen = new TranslateTransition(new Duration(300), draw);
        drawerOpen.setToX(0);
        TranslateTransition drawerClose = new TranslateTransition(new Duration(300), draw);

        //alle children opacity property verbinden met die van de drawer
        for (Node node:draw.getChildren()){
            node.opacityProperty().bind(draw.opacityProperty());
        }

        if (draw.isVisible()) {
            //fadeout op einde onzichtbaar zetten
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(o -> draw.setVisible(false));
            drawerClose.setToX(+(draw.getWidth()));
            ParallelTransition parallelTransition = new ParallelTransition(ft,drawerClose);
            parallelTransition.play();
        } else {
            draw.setVisible(true);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            if (draw.getTranslateX() != 0) {
                ParallelTransition parallelTransition = new ParallelTransition(ft,drawerOpen);
                parallelTransition.play();
            }
        }
    }
}