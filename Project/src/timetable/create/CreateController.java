//Tibo Vanheule
package timetable.create;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import timetable.Controller;
import timetable.db.DAO;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Item;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

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
    private String ui;


    public void setStageAndSetupListeners(Stage stage, Controller mainController) {
        //krijgen van de stage
        this.stage = stage;
        this.mainController = mainController;
    }

    public void initialize() {
        student.setOnAction(o -> page(student.getUserData() + ""));
        loc.setOnAction(o -> page(loc.getUserData() + ""));
        teacher.setOnAction(o -> page(teacher.getUserData() + ""));
        lecture.setOnAction(o -> page(lecture.getUserData() + ""));


    }

    class ButtonCell extends TableCell<Item, Boolean> {
        private final Button cellButton = new Button();

        private ButtonCell() {

            cellButton.setText("Delete");
            cellButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {

                    int selectdIndex = getTableRow().getIndex();

                    //Create a new table show details of the selected item
                    Item selectedRecord = table.getItems().get(selectdIndex);
                    delete(selectedRecord);
                    table.getItems().remove(selectdIndex);
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (empty || t == null) {
                setText(null);
                setGraphic(null);
                setOnMouseClicked(null);
            }
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    private void page(String ui) {
        /*dynamisch laden van fxml*/
        try {
            this.ui = ui;
            FXMLLoader loader = new FXMLLoader(CreateController.class.getResource(ui + ".fxml"));
            loader.setController(this);
            AnchorPane pane = loader.load();
            rootPane.getChildren().setAll(pane);


            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            name.setCellFactory(column -> {
                TableCell<Item, String> cell = new TextFieldTableCell<>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            delete.setCellFactory(column -> {
                ButtonCell cell = new ButtonCell();
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            /*name.setOnEditCommit(event -> update(event.getRowValue(), event.getNewValue()));*/

            // TODO: 25/04/2018 hashmap

            try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
                HashMap<String, DAO> daos = new HashMap<>();
                daos.put("student", dac.getStudentsDAO());
                daos.put("teacher", dac.getTeacherDAO());
                daos.put("location", dac.getLocationDAO());
                daos.put("lecture", dac.getLectureDoa());
                DAO dao = daos.get(ui);
                for (Item item : dao.get()) {
                    table.getItems().addAll(item);
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void create() {

        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            HashMap<String, Item> daos = new HashMap<>();
            daos.put("student", dac.getStudentsDAO().createStudent("student"));
            daos.put("teacher", dac.getTeacherDAO().createTeacher("teacher"));
            table.getItems().add(daos.get(ui));
            mainController.getModel().changeItems(mainController.getModel().getStandardSchedule());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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

    public void close() {
        if (canClose) {
            stage.close();
        }
    }

    private void delete(Item item) {
        canClose = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure");
        alert.setContentText("If a student is used in a lecture,\n then that lecture gets deleted too ");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // ... user chose OK
            try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
                HashMap<String, DAO> daos = new HashMap<>();
                daos.put("student", dac.getStudentsDAO());
                daos.put("teacher", dac.getTeacherDAO());
                daos.put("location", dac.getLocationDAO());
                daos.put("lecture", dac.getLectureDoa());
                DAO dao = daos.get(ui);
                dao.delete(item);
                mainController.getModel().changeItems(mainController.getModel().getStandardSchedule());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        canClose = true;
    }
}
