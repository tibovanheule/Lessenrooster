//Tibo vanheule
package timetable;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
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
import timetable.objects.Item;
import timetable.objects.Lecture;
import timetable.objects.Weather;
import timetable.settings.SettingsController;
import timetable.views.SortButtons;
import timetable.weather.WeatherController;
import timetable.weather.WeatherScraper;

import java.awt.event.ActionEvent;
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
    private DataAccessProvider dataAccessProvider;
    private ListView[] days = {monday,tuesday,wednesday,thursday,friday};
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

        Platform.runLater(this::getWeather);

        //wanneer men op een andere lijst klikt de slectie wissen in de huidige lijst
        try {
            monday.focusedProperty().addListener(o -> monday.getSelectionModel().clearSelection());
            tuesday.focusedProperty().addListener(o -> tuesday.getSelectionModel().clearSelection());
            wednesday.focusedProperty().addListener(o -> wednesday.getSelectionModel().clearSelection());
            thursday.focusedProperty().addListener(o -> thursday.getSelectionModel().clearSelection());
            friday.focusedProperty().addListener(o -> friday.getSelectionModel().clearSelection());
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
            //map maken om later iffen te vermijden :)
            // TODO: 29/03/2018 zie beneden
            //map naar lijst
            HashMap<Integer, ListView<String>> lists = new HashMap<>();
            lists.put(1, monday);
            lists.put(2, tuesday);
            lists.put(3, wednesday);
            lists.put(4, thursday);
            lists.put(5, friday);
            for (Map.Entry<Integer, ListView<String>> entry : lists.entrySet()) {
                entry.getValue().getItems().clear();
            }
            try {
                HashMap<Integer, ArrayList<Lecture>> days = database.getRooster(selected.getSort(), selected.getName());
                for (int i = 1; i < 6; i++) {
                    ArrayList<Lecture> dayList = days.get(i);
                    for (Lecture lecture : dayList) {
                        lists.get(i).getItems().add(lecture.getBlock() + " " + lecture.getCourse());
                        // TODO: 27/03/2018
                        //if (lecture.getConflict()){
                        //cell.getStyleClass().add("conflict");
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