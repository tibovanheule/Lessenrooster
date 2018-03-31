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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import timetable.about.AboutController;
import timetable.config.Config;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.db.ItemsDAO;
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
    public ListView<Lecture> monday, tuesday, wednesday, thursday, friday;
    public ListView<Item> list;
    public ImageView dbLogo, weatherIcon;
    public SortButtons students, teachers, loc;
    public AnchorPane draw;
    public DataAccessProvider dataAccessProvider;
    private ArrayList<ListView<Lecture>> lists = new ArrayList<>();
    private Stage stage;
    private String standardSchedule;

    void setStageAndSetupListeners(Stage controller) {
        this.stage = controller;
    }

    public void initialize() {
        lists.add(monday);
        lists.add(tuesday);
        lists.add(wednesday);
        lists.add(thursday);
        lists.add(friday);

        Config config = new Config();
        Properties properties = config.getproperties();

        appname.setText(properties.getProperty("program.name"));
        //sla op in een veld zodat het in het verdere bestand opgevraagd kan worden
        standardSchedule = properties.getProperty("standard.schedule");

        //als de property true is gebruik dan mysql
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            dataAccessProvider = new MysqlDataAccessProvider();
            //deze afbeelding is voor het gemak dan weten we op welke DB we draaien als we het prog draaien
            Image image = new Image(getClass().getResourceAsStream("resources/images/mysql.png"));
            dbLogo.setImage(image);
        } else {
            //in elk ander geval, valt het terug op sqlite
            dataAccessProvider = new SqliteDataAccessProvider();
            Image image = new Image(getClass().getResourceAsStream("resources/images/sqlite.png"));
            dbLogo.setImage(image);
        }

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

        updateList(standardSchedule);

        list.getSelectionModel().selectedItemProperty().addListener(o -> getRooster(list.getSelectionModel().getSelectedItem()));
        searchText.textProperty().addListener(o -> search());
        students.setOnAction(o -> updateList(students.getUserData().toString()));
        teachers.setOnAction(o -> updateList(teachers.getUserData().toString()));
        loc.setOnAction(o -> updateList(loc.getUserData().toString()));

        //ophalen van het weerbericht gebeurd in een thread
        //voordeel: programma moet niet wachten achter de ophaling
        //nadeel: met zeer goede verbinding zal deze manier iets trager zijn
        Platform.runLater(this::getWeather);

        list.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Item> call(ListView<Item> myObjectListView) {
                ListCell<Item> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Item item, boolean b) {
                        super.updateItem(item, b);
                        if (b || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                        setWrapText(true); }};
                return cell;
            }
        });

        try {
            for (ListView<Lecture> day : lists) {
                //wanneer men op een andere lijst (de dagen) klikt de slectie wissen in de huidige lijst
                //in alle lijsten is er steeds maar 1 selectie (bugs vermijden)
                day.focusedProperty().addListener(o -> day.getSelectionModel().clearSelection());
                //listeners opzetten
                day.getSelectionModel().selectedItemProperty().addListener(o -> lecture(day.getSelectionModel().getSelectedItem()));
                day.setCellFactory(new Callback<>() {
                    @Override
                    public ListCell<Lecture> call(ListView<Lecture> myObjectListView) {
                        ListCell<Lecture> cell = new ListCell<>() {
                            @Override
                            protected void updateItem(Lecture lecture, boolean b) {
                                super.updateItem(lecture, b);
                                if (b || lecture == null) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    setText(lecture.getCourse());
                                    if(lecture.getConflict()){
                                        getStyleClass().add("conflict");
                                    }
                                }
                                setWrapText(true);
                            }
                        };
                        return cell;
                    }
                });
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
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

    public void search() {
        list.getItems().clear();
        if (searchText.getText().isEmpty()) {
            //als textfield leeg is keer dan terug naar de standaard lijst
            updateList(standardSchedule);
        } else {
            // zo niet haal de gefilterde lijst op
            try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                ItemsDAO itemsDAO = dac.getItemDoa();
                for (Item item : itemsDAO.getFilterdList(searchText.getText())) {
                    list.getItems().add(item);
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }

    void getRooster(Item selected) {
        //wanneer men klikt op de buttons students teachers wordt de listener ook getriggerd
        //erwordt dan een null waarde geproduceerd door
        //list.getSelectionModel().getSelectedItem()
        try {
            //array maken om later iffen te vermijden :)
            for (ListView<Lecture> list : lists) {
                list.getItems().clear();
            }
            try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                for (Map.Entry<Integer, ArrayList<Lecture>> entry : dac.getLectureDoa().getWeek(selected).entrySet()) {
                    List<Lecture> lectures = entry.getValue();
                    // Sortering (lessen in de juiste volgorde zetten)
                    lectures.sort(Comparator.comparing(Lecture::getBlock));
                    for (Lecture lecture : lectures) {
                        lists.get(lecture.getDay() - 1).getItems().add(lecture);
                        // TODO: 27/03/2018
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lecture(Lecture selected) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("lecture/lecture.fxml"));
            Parent root = loader.load();
            LectureController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 450, 450);
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage, selected);
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
            stage.setScene(scene);
            controller.setStageAndSetupListeners(stage, this);
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateList(String whatList) {
        //listview leeg maken voor nieuwe items
        list.getItems().clear();
        try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
            ItemsDAO itemsDAO = dac.getItemDoa();
            for (Item item : itemsDAO.getList(whatList)) {
                list.getItems().add(item);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
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
        for (Node node : draw.getChildren()) {
            node.opacityProperty().bind(draw.opacityProperty());
        }

        if (draw.isVisible()) {
            //fadeout op einde onzichtbaar zetten
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