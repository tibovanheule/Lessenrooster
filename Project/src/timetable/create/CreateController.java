//Tibo Vanheule
package timetable.create;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import timetable.Controller;
import timetable.MainModel;
import timetable.StdError;
import timetable.db.DAO;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Item;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/**
 * Create controller or Create companion
 * This class dynamically loads Fxml files when when of the buttons is clicked (Student button, Teacher button and Loaction Button).
 * these files contain a table view
 *
 * @author Tibo Vanheule
 */
public class CreateController {
    private Stage stage;
    @FXML
    private TableColumn<Item, String> name;
    @FXML
    private TableColumn<Item, Boolean> delete;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane rootPane2;
    private Controller mainController;
    @FXML
    private TableView<Item> table;
    @FXML
    private Button student, teacher, loc, lecture, create;
    private Boolean canClose = true;
    private String sort;
    private MainModel model;


    /**
     * Setup of stage maincontrooler and model fields. So it can be used in the whole class
     */
    public void setStageAndSetupListeners(Stage stage, Controller mainController) {
        this.stage = stage;
        this.mainController = mainController;
        this.model = mainController.getModel();
    }

    /**
     * intialization, setup of the buttons
     */
    public void initialize() {
        student.setOnAction(o -> page(student.getUserData().toString()));
        loc.setOnAction(o -> page(loc.getUserData().toString()));
        teacher.setOnAction(o -> page(teacher.getUserData().toString()));
    }

    /**
     * Function that loads correct Fxml and sets up the table and fill it
     */
    private void page(String sort) {
        try {
            this.sort = sort;
            FXMLLoader loader = new FXMLLoader(CreateController.class.getResource(sort + ".fxml"));
            loader.setController(this);
            AnchorPane pane = loader.load();
            rootPane.getChildren().setAll(pane);

            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            name.setCellFactory(column -> new TextFieldTableCell<>(new DefaultStringConverter()));
            name.setOnEditCommit(event -> updateName(event.getRowValue(), event.getNewValue()));
            delete.setCellFactory(column -> new ButtonCellItem(table, this));

            try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
                HashMap<String, DAO> daos = new HashMap<>();
                daos.put("student", dac.getStudentsDAO());
                daos.put("teacher", dac.getTeacherDAO());
                daos.put("location", dac.getLocationDAO());
                DAO dao = daos.get(sort);
                dao.get().forEach((item) -> table.getItems().add(item));
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to create a new student, teacher or location. and adds it to the table
     */
    public void create() {
        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
            HashMap<String, DAO> daos = new HashMap<>();
            daos.put("student", dac.getStudentsDAO());
            daos.put("teacher", dac.getTeacherDAO());
            daos.put("location", dac.getLocationDAO());
            Integer i = 2;
            if (daos.get(sort).nameExists(sort)) {
                while (daos.get(sort).nameExists(sort + " (" + i + ")")) {
                    i++;
                }
                table.getItems().add(daos.get(sort).create(sort + " (" + i + ")"));
            } else {
                table.getItems().add(daos.get(sort).create(sort));
            }
            model.changeItems(model.getStandardSchedule());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Go back to the menu, let user choose between a teacher, location and a student.
     */
    public void menu() {
        try {
            FXMLLoader loader = new FXMLLoader(CreateController.class.getResource("create.fxml"));
            loader.setController(this);
            AnchorPane pane = loader.load();
            rootPane2.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * close stage, if no alert is open.
     */
    public void close() {
        if (canClose) {
            stage.close();
        }
    }

    /**
     * Display an alert, then if user pressed ok, delete the selected teacher, location or student
     */
    public void delete(Item item) {
        /*don't close window when alert is displayed (alert causes to shift the focus wich closes the stage)*/
        canClose = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure");
        alert.setContentText("If a " + sort + " is used in a lecture,\nthen that lecture gets deleted too ");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // user chose OK, Delete
            try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
                HashMap<String, DAO> daos = new HashMap<>();
                daos.put("student", dac.getStudentsDAO());
                daos.put("teacher", dac.getTeacherDAO());
                daos.put("location", dac.getLocationDAO());
                daos.get(sort).delete(item);
                mainController.getModel().changeItems(mainController.getModel().getStandardSchedule());
                table.getItems().remove(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        canClose = true;
    }

    /**
     * Function update a name of a location student or teacher when a editCommit happens.
     */
    private void updateName(Item item, String name) {

        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            HashMap<String, DAO> daos = new HashMap<>();
            daos.put("student", dac.getStudentsDAO());
            daos.put("teacher", dac.getTeacherDAO());
            daos.put("location", dac.getLocationDAO());
            DAO dao = daos.get(sort);
            if (!dao.nameExists(name)) {
                item.setName(name);
                dao.updateName(item);
                mainController.getModel().changeItems(mainController.getModel().getStandardSchedule());
            } else {
                canClose = false;
                new StdError("Warning", "Duplicates", "The name you have entered is already\n in the database", Alert.AlertType.WARNING);
                canClose = true;
            }
            table.getItems().clear();
            dao.get().forEach((item2) -> table.getItems().add(item2));

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}