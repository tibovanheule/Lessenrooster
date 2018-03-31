package timetable.lecture;

import javafx.stage.Stage;
import timetable.objects.Lecture;
import javafx.scene.control.Label;


public class LectureController {
    private Stage stage;
    private Lecture lecture;
    public Label duration,hour,student,teacher,course,day;
    public void setStageAndSetupListeners(Stage stage, Lecture lecture){
        this.stage = stage;
        this.lecture = lecture;
    }
    public void initialize(){
        String[] days = {"monday","tuesday","wednesday","thursday","friday"};
        duration.setText(lecture.getDuration() + "hours");
        hour.setText("block" + lecture.getBlock());
        //location.setText("test");
        student.setText(lecture.getStudent());
        teacher.setText(lecture.getTeacher());
        course.setText(lecture.getCourse());
        day.setText(days[lecture.getDay()-1]);
    }
    public void close(){
        stage.close();
    }
}
