//Tibo vanheule
package timetable;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.ImageView;
import timetable.Objects.Item;
import timetable.Objects.Lecture;
import timetable.about.AboutController;
import timetable.db.Db;
import timetable.db.Mysql;
import timetable.db.Sqlite;
import timetable.settings.SettingsController;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;

public class Controller {
    public Label day;
    public Label date;
    public Label time;
    public MenuButton menu;
    public ListView<String> list;
    public TextField searchText;
    public MenuItem windowSizeText;
    public ListView<String> monday;
    public ImageView dbLogo;
    private Stage stage;
    private String standardSchedule;
    private Db database;
    private ArrayList<Item> listElements = new ArrayList<>();
    private double xOffset = 0;
    private double yOffset = 0;




    public void setStageAndSetupListeners(Stage controller){
        this.stage = controller;
    }

    public void initialize(){
        Config config = new Config();
        Properties properties =  config.getproperties();

        //als de property true is gebruik dan mysql
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))){
            database = new Db(new Mysql());
            Image image = new Image(getClass().getResourceAsStream("Resouces/images/mysql.png"));
            dbLogo.setImage(image);
        }else {
            //in elk ander geval, valt het terug op Sqlite
            database = new Db(new Sqlite());
            Image image = new Image(getClass().getResourceAsStream("Resouces/images/sqlite.png"));
            dbLogo.setImage(image);
        }


        //sla op in een veld zodat het in het verdere bestand gebruikt kan worden
        standardSchedule = properties.getProperty("standard.schedule");



        // TODO: 14/03/2018
        //zet deze tijden  om in in oproepbare methode
        //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        //de huidige datum
        LocalDateTime now = LocalDateTime.now();


        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("H:mm");

        //formatteer de datum (dag)
        DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter dayOfweek = DateTimeFormatter.ofPattern("E");
        //geef nieuwe waarde aan de label met id:day
        //formateer datum
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("d LLLL");




        date.setText(now.format(formatDate));
        day.setText(now.format(formatDay));
        time.setText(now.format(formatTime));


        listElements.addAll(database.getList(standardSchedule));
        for (Item item :listElements  ) {
            list.getItems().add(item.getName());
        }

        list.getSelectionModel().selectedItemProperty().addListener(o -> getRooster());
        searchText.textProperty().addListener(o -> search());


        
    }





    public void search(){
        //als textfield leeg is keer dan terug naar de standaard lijst
        if (searchText.getText().isEmpty()){
            list.getItems().clear();
            listElements.clear();
            listElements.addAll(database.getList(standardSchedule));
            for (Item item :listElements  ) {
                list.getItems().add(item.getName());
            }
        } else{
            // zo niet haal de gefilterde lijst op
            list.getItems().clear();
            listElements.clear();
            listElements.addAll(database.getFilteredList(searchText.getText()));
            for (Item item :listElements  ) {
                list.getItems().add(item.getName());
            }
        }

    }

    public void getRooster(){
        // TODO: 18/03/2018  
        
        //wanneer men klikt op de buttons students teachers wordt de listener ook getriggerd
        //erwordt dan een null waarde geproduceerd door
        //list.getSelectionModel().getSelectedItem()
        if (list.getSelectionModel().getSelectedItem() != null){
            Item selected = null;
            for (Item item:listElements){
                if (item.getName() == list.getSelectionModel().getSelectedItem()){
                    selected = item;
                }
            }
            monday.getItems().clear();
            for(Lecture lecture:database.getRooster(selected.getSort(),selected.getName())){
                if (lecture.getDay()==1){
                    monday.getItems().add(lecture.getCourse());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateList(){

        //textbox leeg maken
        searchText.setText("");
        //listview leeg maken voor nieuwe items
        list.getItems().clear();
        //toevoegen van nieuwe elementen
        listElements.clear();
        listElements.addAll(database.getList("students"));
        for (Item item :listElements  ) {
            list.getItems().add(item.getName());
        }

    }

    public void maximize() {
        //is de stage gemaximaliseerd?
        if (stage.isMaximized()){
            //Zet menuItem tekst naar maximize
            // _ voor de mnemonic parsing
            windowSizeText.setText("_Maximize");
            //minimaliseer
            stage.setMaximized(false);
        }else{
            //Zet menuItem tekst naar minimize
            // _ voor de mnemonic parsing
            windowSizeText.setText("_Minimize");
            //maximaliseer
            stage.setMaximized(true);
        }
    }
}