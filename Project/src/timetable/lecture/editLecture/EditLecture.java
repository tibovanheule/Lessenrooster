/*Tibo Vanheule*/
package timetable.lecture.editLecture;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timetable.lecture.LectureController;
import timetable.objects.Lecture;

public class EditLecture {

    private Stage stage;
    private Lecture lecture;
    private LectureController controller;
    public ComboBox<Integer> block,duration;
    public ComboBox<String> teacher,students,day,loc;
    public TextField name;
    private static final String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};

    public void setStageAndSetupListeners(Stage stage, Lecture lecture, LectureController controller) {
        this.stage = stage;
        this.lecture = lecture;
        this.controller = controller;
        block.setValue(lecture.getBlock());
        duration.setValue(lecture.getDuration());
        teacher.setValue(lecture.getTeacher());
        students.setValue(lecture.getStudent());
        day.setValue(days[lecture.getDay()-1]);
        loc.setValue(lecture.getLocation());

    }

    public void initialize(){
        day.getItems().addAll(days);
    }

    public void close(){
        // TODO: 10/04/2018 onderliggende stage realtime aanpassen
        controller.canClose = true;
        stage.close();
    }
}
