//Tibo vanheule
package timetable;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.about.AboutController;
import timetable.db.Db;
import timetable.db.Mysql;
import timetable.db.Sqlite;
import timetable.settings.SettingsController;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Controller {
    public Label day;
    public Label date;
    public Label time;
    public MenuButton menu;
    public ListView<String> list;
    public TextField searchText;
    public MenuItem windowSizeText;
    private Stage stage;
    private String standardSchedule;
    private Db database;


    public void setStageAndSetupListeners(Stage controller){
        this.stage = controller;
    }

    public void initialize() throws IOException{
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("schedule.properties"));

        //als de property gelijk is aan mysql gebruik dan mysql
        if (properties.getProperty("DB.use").equals("Mysql")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Database");
            alert.setHeaderText("Database info");
            alert.setContentText("You are now using the Mysql database");
            alert.showAndWait();
            database = new Db(new Mysql());
        }else {
            //in elk ander geval, valt het terug op Sqlite
            database = new Db(new Sqlite());
        }


        //sla op in een veld zodat het in het verdere bestand gebruikt kan worden
        standardSchedule = properties.getProperty("standard.schedule");
        list.getItems().addAll(database.getList(standardSchedule));
        // TODO: 14/03/2018
        //zet deze tijden  om in in oproepbare methode
        //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        //de huidige datum
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("H:mm");
        //formatteer de datum (dag)
        DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("EEEE");
        //geef nieuwe waarde aan de label met id:day
        //formateer datum
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("d LLLL");
        date.setText(now.format(formatDate));
        day.setText(now.format(formatDay));
        time.setText(now.format(formatTime));

        list.getSelectionModel().selectedItemProperty().addListener(o -> getRooster());
        searchText.textProperty().addListener(o -> search());


        
    }
    public void search(){
        //als textfield leeg is keer dan terug naar de standaard lijst
        if (searchText.getText().isEmpty()){
            list.getItems().clear();
            list.getItems().addAll(database.getList(standardSchedule));
        } else{
            // zo niet haal de gefilterde lijst op
            list.getItems().clear();
            list.getItems().addAll(database.getFilteredList(searchText.getText()));
        }

    }

    public void getRooster(){
        // TODO: 18/03/2018  
        
        //wanneer men klikt op de buttons students teachers wordt de listener ook getriggerd
        //erwordt dan een null waarde geproduceerd door
        //list.getSelectionModel().getSelectedItem()
        if (list.getSelectionModel().getSelectedItem() != null){
            // onderste lijn voor debugging
            System.out.println(list.getSelectionModel().getSelectedItem());
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
            list.getItems().add(e.toString());
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
        list.getItems().addAll(database.getList("students"));

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