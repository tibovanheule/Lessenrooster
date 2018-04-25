//Tibo Vanheule
package timetable.create;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import timetable.Controller;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Item;

import java.io.IOException;

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
    private Button student, teacher, loc, lecture;

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
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    private void page(String ui) {
        /*dynamisch laden van fxml*/
        try {
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

            try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
                for (Item item : dac.getStudentsDAO().getStudent()) {
                    table.getItems().addAll(item);
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveStudent() {
        if (!name.getText().isEmpty()) {
            try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
                dac.getStudentsDAO().createStudent(name.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mainController.getModel().fireInvalidationEvent();
    }

    public void saveTeacher() {
        if (!name.getText().isEmpty()) {
            try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
                dac.getStudentsDAO().createStudent(name.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mainController.getModel().fireInvalidationEvent();
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
        stage.close();
    }

    private void delete(Item item) {
        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            dac.getStudentsDAO().deleteStudent(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
