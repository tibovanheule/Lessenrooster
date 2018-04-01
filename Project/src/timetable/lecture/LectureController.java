package timetable.lecture;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import timetable.objects.Lecture;


public class LectureController {
    public Label duration, hour, student, teacher, course, day;
    private Stage stage;

    public void setLecture(Lecture lecture) {
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
        try{
            duration.setText(lecture.getDuration() + "hours");
            hour.setText("block" + lecture.getBlock());
            //location.setText("test");
            student.setText(lecture.getStudent());
            teacher.setText(lecture.getTeacher());
            course.setText(lecture.getCourse());
            day.setText(days[lecture.getDay() - 1]);
        }catch (Exception e){
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
