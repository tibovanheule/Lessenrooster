/*Tibo Vanheule*/
package timetable.lecture.editLecture;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timetable.MainModel;
import timetable.StdError;
import timetable.comboboxes.ItemCombobox;
import timetable.comboboxes.PeriodsCombobox;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.lecture.LectureController;
import timetable.objects.Item;
import timetable.objects.Lecture;
import timetable.objects.Period;

/**
 * class to let the user edit, update an lecture.
 *
 * @author Tibo Vanheule
 */
public class EditLecture {

    private static final String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
    private Boolean canClose = true;
    private Stage stage;
    private Lecture lecture;
    private LectureController controller;
    @FXML
    private ComboBox<Integer> duration;
    @FXML
    private PeriodsCombobox period;
    @FXML
    private ComboBox<String> day;
    @FXML
    private ItemCombobox teacher, students, loc;
    @FXML
    private TextField name;
    private MainModel model;

    /**
     * set up fields lecture stage controllermodel, add elements of database to the comboboxes, set values
     */
    public void setStageAndSetupListeners(Stage stage, Lecture lecture, LectureController controller, MainModel model) {
        this.stage = stage;
        this.lecture = lecture;
        this.controller = controller;
        this.model = model;
        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
            dac.getStudentsDAO().get().forEach(o -> students.getItems().add(o));
            dac.getTeacherDAO().get().forEach(o -> teacher.getItems().add(o));
            dac.getLocationDAO().get().forEach(o -> loc.getItems().add(o));
            dac.getPeriodDAO().getPeriods().forEach(o -> period.getItems().add(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText(lecture.getCourse());
        duration.setValue(lecture.getDuration());
        teacher.setValue(new Item("teacher", lecture.getTeacher(), lecture.getTeacherId()));
        students.setValue(new Item("student", lecture.getStudent(), lecture.getStudentId()));
        loc.setValue(new Item("location", lecture.getLocation(), lecture.getLocationId()));
        day.setValue(days[lecture.getDay() - 1]);
        period.setValue(new Period(lecture.getBlock(), lecture.getHour(), lecture.getMinute()));

    }

    /**
     * set up combobox day and duration
     */
    public void initialize() {
        day.getItems().addAll(days);
        duration.getItems().addAll(1, 2, 3, 4);
    }

    /**
     * close and save the updated lecture
     */
    public void saveClose() {
        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
            if (name.getText().trim().isEmpty()) {
                new StdError("Error", "Empty name", "Pls, add a name for the course!", Alert.AlertType.ERROR);
                throw new IllegalArgumentException("the name is empty");
            }

            Lecture newLecture = new Lecture(
                    students.getSelectionModel().getSelectedItem().getName(),
                    teacher.getSelectionModel().getSelectedItem().getName(),
                    loc.getSelectionModel().getSelectedItem().getName(),
                    name.getText(),
                    day.getSelectionModel().getSelectedIndex() + 1,
                    period.getSelectionModel().getSelectedItem().getId(),
                    duration.getSelectionModel().getSelectedItem(),
                    period.getSelectionModel().getSelectedItem().getHour(),
                    period.getSelectionModel().getSelectedItem().getMinute(),
                    students.getSelectionModel().getSelectedItem().getId(),
                    teacher.getSelectionModel().getSelectedItem().getId(),
                    loc.getSelectionModel().getSelectedItem().getId()
            );

            lecture.getConflicts().forEach((lecture) -> newLecture.addConflict(lecture));
            dac.getLectureDoa().update(newLecture, lecture);
            controller.setLecture(newLecture);
            model.refresh();
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            /*empty name*/
        }
        stage.close();
        controller.setCanClose(true);

    }

    /**
     * close stage
     */
    public void close() {
        if (canClose) {
            stage.close();
            controller.setCanClose(true);
        }
    }
}
