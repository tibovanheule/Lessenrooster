package timetable.lecture;

import javafx.stage.Stage;
import timetable.objects.Lecture;

public class LectureController {
    private Stage stage;
    private Lecture lecture;
    public void setStageAndSetupListeners(Stage stage, Lecture lecture){
        this.stage = stage;
        this.lecture = lecture;
    }
    public void initialize(){

    }
    public void close(){
        stage.close();
    }
}
