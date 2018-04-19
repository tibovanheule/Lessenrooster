/*Tibo Vanheule*/
package timetable.lecture;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.Main;
import timetable.db.DataAccessProvider;
import timetable.lecture.editLecture.EditLecture;
import timetable.objects.Lecture;

public class LectureController {
    public Label text, course;
    private Stage stage;
    private Lecture lecture;
    public Boolean canClose = true;
    private DataAccessProvider dataAccessProvider;

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
        try {
            course.setText(lecture.getCourse());
            text.setText(days[lecture.getDay() - 1] + "\n"
                    + "Duration: " + lecture.getDuration() + " hours\n"
                    + "Block: " + lecture.getBlock() + "\n"
                    + "Start hour" + lecture.getTime() + "\n"
                    + "For students: " + lecture.getStudent() + "\n"
                    + "Teacher: " + lecture.getTeacher() + "\n"
                    + "Location: " + lecture.getLocation() + "\n"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStageAndSetupListeners(Stage stage, DataAccessProvider dataAccessProvider) {
        this.stage = stage;
        this.dataAccessProvider = dataAccessProvider;
        System.out.println(dataAccessProvider);
    }

    public void edit() {
        try {
            canClose = false;
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("lecture/editLecture/editLecture.fxml"));
            Parent root = loader.load();
            EditLecture controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            controller.setStageAndSetupListeners(stage, lecture, this, this.dataAccessProvider);
            stage.show();
        } catch (Exception e) {
            canClose = true;
            e.printStackTrace();
        }
    }

    public void close() {
        if (canClose) {
            stage.close();
        }
    }
}
