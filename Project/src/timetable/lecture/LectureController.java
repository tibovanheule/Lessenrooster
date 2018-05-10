/*Tibo Vanheule*/
package timetable.lecture;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.Main;
import timetable.MainModel;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.lecture.editLecture.EditLecture;
import timetable.objects.Lecture;

/**
 * Class to display information about the lecture
 *
 * @author Tibo Vanheule
 */
public class LectureController {


    @FXML
    private Label text, course;
    @FXML
    private ConflictsListview conflicts;
    @FXML
    private Label conflictText;
    private Stage stage;
    private Lecture lecture;
    private Boolean canClose = true;
    private MainModel model;

    /**
     * sets boolean to decide if stage may be closed
     */
    public void setCanClose(Boolean canClose) {
        this.canClose = canClose;
    }

    /**
     * shows information of lecture
     */
    public void setLecture(Lecture lecture) {
        try {
            String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
            course.setText(lecture.getCourse());
            text.setText(days[lecture.getDay() - 1] + "\n"
                    + "Duration: " + lecture.getDuration() + " hours\n"
                    + "Start hour: " + lecture.getTime() + "\n"
                    + "For students: " + lecture.getStudent() + "\n"
                    + "Teacher: " + lecture.getTeacher() + "\n"
                    + "Location: " + lecture.getLocation() + "\n"
            );

            if (lecture.getConflict()) {
                conflicts.getItems().clear();
                conflicts.getItems().addAll(lecture.getConflicts());
            } else {
                conflictText.setText("There are no conflicts.");
                conflicts.setVisible(false);
            }

            this.lecture = lecture;

        } catch (NullPointerException e) {
            /*ignore empty lecture*/
        }
    }

    /**
     * sets up fields stage and model
     */
    public void setStageAndSetupListeners(Stage stage, MainModel model) {
        this.stage = stage;
        this.model = model;
        conflicts.setOnMouseClicked(o -> setLecture(conflicts.getSelectionModel().getSelectedItem()));
    }

    /**
     * load new stage to edit current lecture
     */
    public void edit() {
        try {
            canClose = false;
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("lecture/editLecture/editLecture.fxml"));
            Parent root = loader.load();
            EditLecture controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            controller.setStageAndSetupListeners(stage, lecture, this, model);
            stage.initOwner(this.stage);
            stage.requestFocus();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * closes stage, except when editing
     */
    public void close() {
        if (canClose) {
            stage.close();
        }
    }

    /**
     * delete the lecture
     */
    public void delete() {
        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext();) {
            dac.getLectureDoa().delete(lecture);
            model.refresh();
            close();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
