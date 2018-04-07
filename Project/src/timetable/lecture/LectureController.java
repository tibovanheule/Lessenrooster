package timetable.lecture;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import timetable.objects.Lecture;


public class LectureController {
    public Label text, course;
    private Stage stage;

    public void setLecture(Lecture lecture) {
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
        try {

            course.setText(lecture.getCourse());
            text.setText(days[lecture.getDay() - 1] + "\n"
                    + "Duration: " + lecture.getDuration() + " hours\n"
                    + "Block: " + lecture.getBlock() + "\n"
                    + "Start hour" + lecture.getTime() + "\n"
                    + "For students: " + lecture.getStudent() + "\n"
                    + "Teacher: " + lecture.getTeacher() + "\n");
            //location.setText("test");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStageAndSetupListeners(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        stage.close();
    }
}
