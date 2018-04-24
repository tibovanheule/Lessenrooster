//Tibo Vanheule
package timetable.create;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import timetable.Controller;
import timetable.db.DataAccessContext;

import java.io.IOException;

public class CreateController {
    private Stage stage;
    @FXML
    private TextField name;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane rootPane2;
    private Controller mainController;
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

    private void page(String ui) {
        /*dynamisch laden van fxml*/
        try {
            FXMLLoader loader = new FXMLLoader(CreateController.class.getResource(ui + ".fxml"));
            loader.setController(this);
            AnchorPane pane = loader.load();
            rootPane.getChildren().setAll(pane);
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
}
